package com.loong.common.utils;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.loong.common.bean.ImageBean;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class OSSUtil {
    // 鉴权信息
    public static String OSS_ACCESS_KEY_ID = "";
    public static String OSS_ACCESS_KEY_SECRET = "";
    // 地址信息
    public static String endpoint = "";
    public static String bucketName = "";
    public static String headUrl = ""; // 阿里云域名前缀

    public static final String OSS_IMAGE_PATH = "psdims/image/";
    public static final String OSS_PDF_PATH = "psdims/pdf/";
    public static final String OSS_VIDEO_PATH = "psdims/video/";
    public static final String OSS_VOICE_PATH = "psdims/voice/";

    private static OSS _oss = null;

    public static void init(String keyId, String keySecret, String endpoint, String bucketName, String headUrl) {
        OSS_ACCESS_KEY_ID = keyId;
        OSS_ACCESS_KEY_SECRET = keySecret;
        OSSUtil.endpoint = endpoint;
        OSSUtil.bucketName = bucketName;
        OSSUtil.headUrl = headUrl;
    }

    public static OSS getOSS() {
        if (OSS_ACCESS_KEY_ID == null || OSS_ACCESS_KEY_ID.isEmpty()) {
            Log.w("OSSUTIL","OSS 没有进行配置！！！");
            return null;
        }
        if (_oss == null) {
            synchronized (OSSUtil.class) {
                if (_oss == null) {

                    OSSCustomSignerCredentialProvider provider = new OSSCustomSignerCredentialProvider() {
                        @Override
                        public String signContent(String content) {

                            // 此处本应该是客户端将contentString发送到自己的业务服务器,然后由业务服务器返回签名后的content。关于在业务服务器实现签名算法
                            // 详情请查看http://help.aliyun.com/document_detail/oss/api-reference/access-control/signature-header.html。客户端
                            // 的签名算法实现请参考OSSUtils.sign(accessKey,screctKey,content)

                            String signedString = OSSUtils.sign(OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET, content);
                            return signedString;
                        }
                    };


                    // 配置类如果不设置，会有默认配置。
                    ClientConfiguration conf = new ClientConfiguration();
                    conf.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒。
                    conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒。
                    conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
                    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。

                    //OSSClient 是 OSS 服务的 Android 客户端，它为调用者提供了一系列的方法进行操作、管理存储空间（Bucket）
                    // 和文件（Object）等。在使用 SDK 发起对 OSS 的请求前，您需要初始化一个 OSSClient 实例，并对它进行一些必要设置。
                    //说明 生命周期和应用生命周期保持一致即可。在应用启动时创建一个全局的OSSClient，在应用结束时销毁即可。
                    _oss = new OSSClient(Utils.getApp(), endpoint, provider, conf);
                }
            }
        }
        return _oss;
    }

    public static List<ImageBean> getBeans(List<LocalMedia> medias) {
        List<ImageBean> beans = new ArrayList<>();
        if (medias != null) {
            for (LocalMedia media : medias) {
                beans.add(getBean(media));
            }
        }
        return beans;
    }

    public static ImageBean getBean(LocalMedia media) {
        ImageBean bean = new ImageBean();
        bean.setLocalPath(getPath(media));
        return bean;
    }

    public static <T> String getPath(T media) {
        String path = "";
        if (media instanceof LocalMedia) {
            path = ((LocalMedia) media).getCutPath();
            if (TextUtils.isEmpty(path)) {
                path = ((LocalMedia) media).getAndroidQToPath();
            }
            if (TextUtils.isEmpty(path)) {
                path = ((LocalMedia) media).getRealPath();
            }
            if (TextUtils.isEmpty(path)) {
                path = ((LocalMedia) media).getPath();
            }
        }
        return path;
    }

    /**
     * OSS上传封装
     *
     * @param curPath  文件路径
     * @param ossPath  oss路径
     * @param ossName  oss名称
     * @param listener 上传监听
     */
    private static void uploadFile(String curPath, String ossPath, String ossName, OSSListener listener) {
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(bucketName, ossPath + ossName, curPath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {

        });

        OSSAsyncTask task = getOSS().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("ReturnBody", request.getObjectKey());

                Log.e("上传成功", "--------");
                if (listener != null) {
                    listener.success(headUrl + request.getObjectKey());
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (listener != null) {
                    listener.failed("bad request" + request.getObjectKey());
                }

                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 单文件上传
     *
     * @param salt      盐-用户唯一标识
     * @param imageBean 上传文件
     * @param listener  事件侦听
     */
    public static void uploadSingleFile(String salt, ImageBean imageBean, OnUploadSingle listener) {
        String ossPaht = OSS_IMAGE_PATH;

        if (!TextUtils.isEmpty(imageBean.getLocalPath())) {
            String localPath = imageBean.getLocalPath();
            String ossName = salt + System.currentTimeMillis();
            try {
                ossName = salt + System.currentTimeMillis() + localPath.substring(localPath.lastIndexOf("."));
            } catch (Exception e) {

            }
            Timber.i("ossName:" + ossName);
            uploadFile(localPath, ossPaht, ossName, new OSSListener() {
                @Override
                public void success(String ossFilePath) {
                    listener.success(imageBean, ossFilePath);
                }

                @Override
                public void failed(String ossFilePath) {
                    listener.failed(imageBean);
                }
            });
        } else if (!TextUtils.isEmpty(imageBean.getNetPath())) {
            listener.success(imageBean, imageBean.getNetPath());
        }
    }

    /**
     * 单文件上传
     *
     * @param salt      盐-用户唯一标识
     * @param imageBean 上传文件
     * @param listener  事件侦听
     */
    public static <T> void uploadSingleFile(String salt, T imageBean, OnUploadSingleT<T> listener) {
        String ossPaht = OSS_IMAGE_PATH;

        String localPath = getPath(imageBean);
        String ossName = salt + System.currentTimeMillis();
        try {
            ossName = salt + System.currentTimeMillis() + localPath.substring(localPath.lastIndexOf("."));
        } catch (Exception e) {

        }
        Timber.i("ossName:" + ossName);
        uploadFile(localPath, ossPaht, ossName, new OSSListener() {
            @Override
            public void success(String ossFilePath) {
                listener.success(imageBean, ossFilePath);
            }

            @Override
            public void failed(String ossFilePath) {
                listener.failed(imageBean);
            }
        });
    }

    /**
     * 多文件上传
     *
     * @param salt       盐-用户唯一标识
     * @param imageBeans 图片列表
     * @param listener   上传侦听
     */
    public static void uploadMultipleFile(String salt, List<ImageBean> imageBeans, OnUploadMultiple listener) {
        ArrayList<ImageBean> mImageList_local = new ArrayList<>();

        Map<ImageBean, String> ossMaps = new HashMap<>();
        List<ImageBean> faileds = new ArrayList<>();
        // 区分本地图片和线上图片
        for (int i = 0; i < imageBeans.size(); i++) {
            ImageBean imageBean = imageBeans.get(i);
            if (imageBean != null) {
                if (!TextUtils.isEmpty(imageBean.getLocalPath())) {
                    mImageList_local.add(imageBean);
                } else if (!TextUtils.isEmpty(imageBean.getNetPath())) {
                    ossMaps.put(imageBean, imageBean.getNetPath());
                    listener.success(imageBean, imageBean.getNetPath());

                    if (ossMaps.size() + faileds.size() == imageBeans.size()) {
                        if (faileds.size() == 0) {
                            listener.allSuccess(ossMaps);
                        } else {
                            listener.allFinish(ossMaps, faileds);
                        }
                    }
                }
            }
        }
        if (mImageList_local.size() > 0) {
            String ossPaht = OSS_IMAGE_PATH;
            // 2021/12/8 上传本地图片
            for (ImageBean imageBean : mImageList_local) {
                String localPath = imageBean.getLocalPath();
                String ossName = salt + System.currentTimeMillis();
                try {
                    ossName = salt + System.currentTimeMillis() + localPath.substring(localPath.lastIndexOf("."));
                } catch (Exception e) {

                }
                Timber.i("ossName:" + ossName);
                uploadFile(localPath, ossPaht, ossName, new OSSListener() {
                    @Override
                    public void success(String ossFilePath) {
                        ossMaps.put(imageBean, ossFilePath);
                        listener.success(imageBean, ossFilePath);
                        if (ossMaps.size() + faileds.size() == imageBeans.size()) {
                            if (faileds.size() == 0) {
                                listener.allSuccess(ossMaps);
                            } else {
                                listener.allFinish(ossMaps, faileds);
                            }
                        }
                    }

                    @Override
                    public void failed(String ossFilePath) {
                        faileds.add(imageBean);
                        listener.failed(imageBean);
                        if (ossMaps.size() + faileds.size() == imageBeans.size()) {
                            if (faileds.size() == 0) {
                                listener.allSuccess(ossMaps);
                            } else {
                                listener.allFinish(ossMaps, faileds);
                            }
                        }
                    }
                });
            }

        }
    }

    /**
     * 多文件上传
     *
     * @param salt        盐-用户唯一标识
     * @param imageMedias 图片列表
     * @param listener    上传侦听
     */
    public static <T> void uploadMultipleFile(String salt, List<T> imageMedias, OnUploadMultipleT<T> listener) {

        Map<T, String> ossMaps = new HashMap<>();
        List<T> faileds = new ArrayList<>();

        if (imageMedias.size() > 0) {
            String ossPaht = OSS_IMAGE_PATH;
            // 2021/12/8 上传本地图片
            for (T imageBean : imageMedias) {
                String localPath = getPath(imageBean);
                String ossName = salt + System.currentTimeMillis();
                try {
                    ossName = salt + System.currentTimeMillis() + localPath.substring(localPath.lastIndexOf("."));
                } catch (Exception e) {

                }
                Timber.i("ossName:" + ossName);
                uploadFile(localPath, ossPaht, ossName, new OSSListener() {
                    @Override
                    public void success(String ossFilePath) {
                        Timber.e("success+" + ossFilePath);
                        ossMaps.put(imageBean, ossFilePath);
                        listener.success(imageBean, ossFilePath);
                        Timber.e("success ossMaps.size()" + ossMaps.size() + "");
                        Timber.e("success faileds.size()" + faileds.size() + "");
                        if ((ossMaps.size() + faileds.size()) == imageMedias.size()) {
                            if (faileds.size() == 0) {
                                listener.allSuccess(ossMaps);
                            } else {
                                listener.allFinish(ossMaps, faileds);
                            }
                        }
                    }

                    @Override
                    public void failed(String ossFilePath) {
                        faileds.add(imageBean);
                        listener.failed(imageBean);
                        Log.e("failed ossMaps.size()", ossMaps.size() + "");
                        Log.e("failed faileds.size()", faileds.size() + "");
                        if ((ossMaps.size() + faileds.size()) == imageMedias.size()) {
                            if (faileds.size() == 0) {
                                listener.allSuccess(ossMaps);
                            } else {
                                listener.allFinish(ossMaps, faileds);
                            }
                        }
                    }
                });
            }

        }
    }


    private interface OSSListener {
        void success(String ossFilePath);

        void failed(String ossFilePath);
    }

    /**
     * 单个文件上传侦听
     */
    public interface OnUploadSingle {
        /**
         * 上传成功
         */
        void success(ImageBean imageBean, String ossFilePath);

        /**
         * 上传失败
         */
        void failed(ImageBean imageBean);
    }

    /**
     * 多个文件上传侦听
     */
    public interface OnUploadMultiple extends OnUploadSingle {
        /**
         * 全部成功
         */
        void allSuccess(Map<ImageBean, String> ossFilePaths);

        /**
         * 完成上传[有失败]
         */
        void allFinish(Map<ImageBean, String> ossFilePaths, List<ImageBean> faiedBeans);
    }

    /**
     * 单个文件上传侦听
     */
    public interface OnUploadSingleT<T> {
        /**
         * 上传成功
         */
        void success(T imageBean, String ossFilePath);

        /**
         * 上传失败
         */
        void failed(T imageBean);
    }

    /**
     * 多个文件上传侦听
     */
    public interface OnUploadMultipleT<T> extends OnUploadSingleT<T> {
        /**
         * 全部成功
         */
        void allSuccess(Map<T, String> ossFilePaths);

        /**
         * 完成上传[有失败]
         */
        void allFinish(Map<T, String> ossFilePaths, List<T> faiedBeans);
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
}
