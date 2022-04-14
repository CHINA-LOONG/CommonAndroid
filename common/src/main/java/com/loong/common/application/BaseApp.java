package com.loong.common.application;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.loong.common.BuildConfig;
import com.loong.common.manager.ActivityManager;
import com.loong.common.network.NetworkManager;
import com.loong.common.utils.SharedPreferencesUtil;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.autosize.AutoSizeConfig;
import timber.log.Timber;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/7 21:36
 * @desc :
 */
public class BaseApp extends Application {
    private static BaseApp mInstance;
//    String CacheDiaPath = getBaseContext().getCacheDir().toString();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        // Log输出库
        if (BuildConfig.DEBUG||true) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        // SharedPreferences初始化
        SharedPreferencesUtil.init(getApplicationContext());

        registerActivityLifecycleCallbacks(ActivityManager.getInstance());

        ActivityManager.getInstance().registerListener(new ActivityManager.AppLifecycleCallbacks() {
            @Override
            public void onBackToApp() {
                Log.i("onBackToApp","BaseApp");
            }

            @Override
            public void onLeaveApp() {
                Log.i("onLeaveApp","BaseApp");
            }
        });

        setRxJavaErrorHandler();
    }


    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> {
            throwable.printStackTrace();
            Log.e("MyApplication", "MyApplication setRxJavaErrorHandler "  + throwable.getMessage());
        });
    }
    public static BaseApp getApp() {
        return mInstance;
    }


    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

//            FakeCrashLibrary.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t);
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t);
//                }
//            }
        }
    }
//    private static class FileLoggingTree extends Timber.Tree {
//        @Override
//        protected void log(int priority, String tag, String message, Throwable t) {
//            if (TextUtils.isEmpty(CacheDiaPath)) {
//                return;
//            }
//            File file = new File(CacheDiaPath + "/log.txt");
//            Log.v("dyp", "file.path:" + file.getAbsolutePath() + ",message:" + message);
//            FileWriter writer = null;
//            BufferedWriter bufferedWriter = null;
//            try {
//                writer = new FileWriter(file);
//                bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.write(message);
//                bufferedWriter.flush();
//
//            } catch (IOException e) {
//                Log.v("dyp", "存储文件失败");
//                e.printStackTrace();
//            } finally {
//                if (bufferedWriter != null) {
//                    try {
//                        bufferedWriter.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}

