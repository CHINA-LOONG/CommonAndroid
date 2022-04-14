package com.loong.common.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.loong.common.utils.FileProvider7;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 *    @author : Administrator
 *    @e-mail : xuelong9009@qq.com
 *    @date   : 2022/3/15
 *    desc   : 版本更新检测
 *    version: v1.0
 */
public class UpdateUtils implements OnUpdateListener {
    private static UpdateUtils mInstance;

    public static UpdateUtils getInstance() {
        if (mInstance == null) {
            synchronized (UpdateUtils.class) {
                if (mInstance == null) {
                    mInstance = new UpdateUtils();
                }
            }
        }
        return mInstance;
    }

    // 版本信息状态
    private final static int FORCE = 11;
    private final static int CANIGNOR = 12;
    private final static int NODO = 13;


    private final static int Error = 404;
    private final static int Success = 100;
    private final static int Progress = 200;

    public static final String down_apk_name = "PSD_IMS.apk";

    private Activity activity;

    private OnUpdateListener listener;

    private Handler downloadHandler;

    private File mApkFile = null;

    private VersionUpdateDialog mVersionUpdateDialog;//更新弹窗

    private Handler checkHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FORCE:
                    listener.needForceUpdate((UpdateBean) msg.obj);
                    break;
                case CANIGNOR:
                    listener.needUpdate((UpdateBean) msg.obj);
                    break;
                case NODO:
                    listener.noNeedUpdate();
                    break;
            }
            return false;
        }
    });

    public void autoUpdate(final Activity activity) {
        getUpdate(activity, this);
    }

    public void getUpdate(final Activity activity, OnUpdateListener onUpdateListener) {
        if (onUpdateListener == null) {
            checkHandler.sendEmptyMessage(NODO);
            throw new NullPointerException("升级回调不能为空");
        }
        if (activity == null) {
            checkHandler.sendEmptyMessage(NODO);
            throw new NullPointerException("回调的activity不能为空");
        }
        this.activity = activity;
        this.listener = onUpdateListener;

         downloadHandler = new Handler(activity.getMainLooper(), msg -> {
             switch (msg.what) {
                 case Error:
                     if (mVersionUpdateDialog != null) {
                         mVersionUpdateDialog.setButtonText("下载失败");
                     }
                     activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                     break;
                 case Success:
                     activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                     if (mVersionUpdateDialog != null) {
                         mVersionUpdateDialog.setButtonText("立即安装");
                     }
                     break;
                 case Progress:
                     if (mVersionUpdateDialog != null) {
                         mVersionUpdateDialog.setProgress(msg.arg1);
                         mVersionUpdateDialog.setButtonText("下载中" + msg.arg1 + "%");
                     }
                     break;
             }
             return false;
         });

        String url = getUrl(activity);
        String appid = getAppId(activity);

        new Thread(() -> {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(appid)) {
                checkHandler.sendEmptyMessage(NODO);
                return;
            }

            try {
                // 2022/3/14 同步网络请求
                String jsonStr = getJsonStr(url, appid, activity);
                Log.e("result", jsonStr);
                if (TextUtils.isEmpty(jsonStr)) {
                    checkHandler.sendEmptyMessage(NODO);
                    return;
                }
                Gson gson = new Gson();
                UpdateBean updateBean = gson.fromJson(jsonStr, UpdateBean.class);
                if (updateBean.isSuccess()) {
                    if (updateBean.getResult().getAppInfo().getVersionNo() > getVersion(activity)) {
                        String updateType = updateBean.getResult().getAppInfo().getUpdateType();
                        if (updateType != null && !updateBean.equals("")) {
                            if (updateType.equals("forceUpdate")) {
                                // 2022/3/15 强制更新 
                                Message message = checkHandler.obtainMessage();
                                message.obj = updateBean;
                                message.what = FORCE;
                                checkHandler.sendMessage(message);
                                return;
                            } else if (updateType.equals("ignoreUpdate")) {
                                // 2022/3/15 可忽略更新
                                Message message = checkHandler.obtainMessage();
                                message.obj = updateBean;
                                message.what = CANIGNOR;
                                checkHandler.sendMessage(message);
                                return;
                            }
                        }
                    }
                }
                checkHandler.sendEmptyMessage(NODO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 同步获取更新的json数据
     *
     * @param path    获取地址
     * @param appId   获取APP
     * @param context 界面上下文
     * @return 获取的版本json信息
     */
    private String getJsonStr(String path, String appId, Context context) {
        HttpURLConnection urlConnection = null;
        String jsonstr = null;
        Map<String, Object> requestParamsMap = new HashMap<String, Object>();
        requestParamsMap.put("appId", appId);
        requestParamsMap.put("versionNo", getVersion(context));
        PrintWriter printWriter = null;
        StringBuffer params = new StringBuffer();
        // 组织请求参数
        Iterator it = requestParamsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            params.append(element.getKey());
            params.append("=");
            params.append(element.getValue());
            params.append("&");
        }
        try {
            URL url = new URL(path.trim());
            //打开连接
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(urlConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(params.toString());
            // flush输出流的缓冲
            printWriter.flush();
            printWriter.close();
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                jsonstr = baos.toString("utf-8");
                baos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (urlConnection != null)
            urlConnection.disconnect();
        return jsonstr;
    }


    //region 文件下载
    /**
     * 浏览器下载
     *
     * @param url 下载链接
     */
    public void gotoUpdate(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    /**
     * 下载安装包文件
     * @param downUrl 下载链接
     */
    private void downloadApkFile(String downUrl) {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> Log.d("HttpLogInfo", message));
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuild = new OkHttpClient().newBuilder();
        okHttpClientBuild.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .addInterceptor(new ProgressInterceptor(new ProgressResponseBody.ProgressListener() {
                    @Override
                    public void update(String url, long bytesRead, long contentLength, boolean done) {

                        int progress = (int) (bytesRead * 1.0f / contentLength * 100);
                        Message message = downloadHandler.obtainMessage();
                        message.what = Progress;
                        message.arg1 = progress;
                        downloadHandler.sendMessage(message);

                    }
                }));
        //信任所有服务器地址
        okHttpClientBuild.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //设置为true
                return true;
            }
        });
        //创建管理器
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            //为OkHttpClient设置sslSocketFactory
            okHttpClientBuild.sslSocketFactory(sslContext.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = okHttpClientBuild.build();

        Request request = new Request.Builder()
                .url(downUrl)
                .build();

        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                Message message = downloadHandler.obtainMessage();
                message.what = Error;
                downloadHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 下载成功监听回调--创建下载文件
                File file = createDownFile(down_apk_name);
                Timber.e(file.getPath());
                if (file != null && file.exists()) {
                    saveFile(response.body(), file);
                } else {
                    Message message = downloadHandler.obtainMessage();
                    message.what = Error;
                    downloadHandler.sendMessage(message);
                }
            }
        });


    }

    /**
     * 生成 下载安装包的外部存储目录和文件
     * @param fileName  下载的文件名称
     * @return  下载的文件
     */
    private File createDownFile(String fileName) {
        // 判断存储是否可以用，获取存贮位置
        File rootFile = !isExternalStorageWritable() ? activity
                .getFilesDir() : activity.getExternalFilesDir("");

        File download_apk_fileStore = new File(rootFile.getAbsolutePath() + "/apk/");
        if (!download_apk_fileStore.exists()) {
            download_apk_fileStore.mkdir();
        }

        File download_apk_file = new File(download_apk_fileStore.getAbsolutePath() + "/" + fileName);

        //如果旧文件存在则删除
        if (download_apk_file.exists()) {
            deleteSingleFile(download_apk_file.getAbsolutePath());
        }

        try {
            download_apk_file.createNewFile();
            return download_apk_file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return download_apk_file;

    }

    /**
     * 将下载的安装包字节流写入本地文件
     * @param body
     * @param apkFile
     */
    public void saveFile(ResponseBody body, File apkFile) {
        InputStream srcInputStream = body.byteStream();
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(apkFile);
            while ((len = srcInputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            mApkFile = apkFile;
            Message message = downloadHandler.obtainMessage();
            message.what = Success;
            downloadHandler.sendMessage(message);
        } catch (Exception e) {
            Message message = downloadHandler.obtainMessage();
            message.what = Error;
            downloadHandler.sendMessage(message);

        } finally {

            try {
                if (srcInputStream != null) {
                    srcInputStream.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {

            }
        }
    }
    //endregion


    //region 默认安装方式
    @Override
    public void needForceUpdate(final UpdateBean updateBean) {
        mVersionUpdateDialog = VersionUpdateDialog.Builder(activity)
                .setUpdateBean(updateBean)
                .setOnUpdateClickListener(new VersionUpdateDialog.OnUpdateClickListener() {
                    @Override
                    public void onUpdate(String updateStr) {
                        if ("立即升级".equals(updateStr)) {
                            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            downloadApkFile(updateBean.getResult().getAppInfo().getDownloadUrl());
                        } else if ("立即安装".equals(updateStr)) {
                            checkPermisssionOrInstall(activity,mApkFile);
                        }

                    }
                })
                .setOnUpdateErrorListener(new VersionUpdateDialog.OnUpdateErrorListener() {
                    @Override
                    public void onUpdateError() {
                        Uri uri = Uri.parse(updateBean.getResult().getAppInfo().getDownloadUrl());
                        Intent intent = new Intent("android.intent.action.VIEW", uri);
                        activity.startActivity(intent);
                    }
                })
                .build();
        mVersionUpdateDialog.shown();
    }

    @Override
    public void needUpdate(final UpdateBean updateBean) {
        mVersionUpdateDialog = VersionUpdateDialog.Builder(activity)
                .setUpdateBean(updateBean)
                .setOnUpdateClickListener(new VersionUpdateDialog.OnUpdateClickListener() {
                    @Override
                    public void onUpdate(String updateStr) {
                        if ("立即升级".equals(updateStr)) {
                            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            downloadApkFile(updateBean.getResult().getAppInfo().getDownloadUrl());
                        } else if ("立即安装".equals(updateStr)) {
                            checkPermisssionOrInstall(activity,mApkFile);
                        }

                    }
                })
                .setOnUpdateErrorListener(new VersionUpdateDialog.OnUpdateErrorListener() {
                    @Override
                    public void onUpdateError() {
                        Uri uri = Uri.parse(updateBean.getResult().getAppInfo().getDownloadUrl());
                        Intent intent = new Intent("android.intent.action.VIEW", uri);
                        activity.startActivity(intent);
                    }
                })
                .build();
        mVersionUpdateDialog.shown();
    }

    @Override
    public void noNeedUpdate() {
        if (mVersionUpdateDialog != null && mVersionUpdateDialog.isShowing()) {
            mVersionUpdateDialog.dismiss();
        }
    }
    //endregion


    //region 版本验证--存储监测

    /**
     * 判断外部存储是否可用
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /**
     * 删除单个文件
     *
     * @param filePathName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePathName) {
        File file = new File(filePathName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 监测APK安装权限并安装
     * @param activity 启动安装上下文
     * @param file     安装的apk文件
     */
    private static void checkPermisssionOrInstall(Activity activity, File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (isHasInstallPermissionWithO(TeacherMainActivity.this)) {
//                installApk(activity, file);
//            } else {
//                UIUtil.toast("请给与我们安装应用的权限！");
//                startInstallPermissionSettingActivity();
//            }
//        } else {
            installApk(activity, file);
//        }
    }
    /**
     * 安装APK文件
     *
     * @param activity 启动安装上下文
     * @param file     安装的apk文件
     */
    private static void installApk(Activity activity, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider7.getUriForFile(activity, file);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        activity.startActivity(intent);
    }

    public static String getUrl(@NonNull Activity activity) {
        String url = "";
        ApplicationInfo info;
        try {
            info = activity.getPackageManager().getApplicationInfo(activity.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            url = info.metaData.getString("nk_url");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getAppId(@NonNull Activity activity) {
        String appid = "";
        ApplicationInfo info;
        try {
            info = activity.getPackageManager().getApplicationInfo(activity.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            appid = info.metaData.getString("nk_appid");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appid;
    }

    public static int getVersion(@NonNull Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }

    public static String getVersionName(@NonNull Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }
    //endregion
}
