package com.loong.common.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/7/156:03 PM
 * desc   : 界面生命周期管理
 * version: 1.0
 */
public class ActivityManager implements Application.ActivityLifecycleCallbacks {

    public interface AppLifecycleCallbacks{
        default void onBackToApp(){}
        default void onLeaveApp(){}
    }

    private static class ACTIVITY_MANAGER_INSTANCE {
        private static final ActivityManager ACTIVITY_MANAGER = new ActivityManager();
    }

    public static ActivityManager getInstance() {
        return ACTIVITY_MANAGER_INSTANCE.ACTIVITY_MANAGER;
    }

    private ArrayList<WeakReference<Activity>> sActivity = new ArrayList();
    private ArrayList<AppLifecycleCallbacks> sListener = new ArrayList();

    private int appCount = 0;
    private boolean isRunInBackground = false;
    private Activity currentActivity = null;

    private ActivityManager() {

    }

    /**
     * 获取当前显示Activity
     * @return
     */
    public Activity getCurrentActivity(){
        return currentActivity;
    }

    /**
     * 注册前后台其他换事件
     * @param listener
     */
    public void registerListener(AppLifecycleCallbacks listener){
        boolean isFind = false;
        for (int i = sListener.size()-1;i>=0;i--){
            AppLifecycleCallbacks listen = sListener.get(i);
            if (listen==null) {
                sListener.remove(i);
                Log.i("ActivityManager","空对象删除"+i);
                continue;
            }
            if (listen == listener){
                Log.i("ActivityManager","重复添加监听对象");
                isFind = true;
                break;
            }
        }
        if (!isFind){
            sListener.add(listener);
        }
    }

    /**
     * 关闭某类型Activity
     * @param clazz
     */
    public void CloseActivityByType (Class clazz){
        for (int i = sActivity.size() - 1; i > 0; i--) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity innerActivity = activityWeakReference.get();
            if (innerActivity!=null&&innerActivity.getClass().equals(clazz)){
                innerActivity.finish();
            }
        }
    }

    /**
     * 关闭掉所有Activity
     */
    public void exitApp() {
        for (int i = 0, size = sActivity.size(); i < size; i++) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
        if (sActivity != null && sActivity.size() > 0) {
            sActivity.clear();
        }
    }

    /************************内部逻辑处理****************************/
    private void add(Activity activity) {
        if (activity != null) {
            WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
            sActivity.add(activityWeakReference);
        }
    }

    private void remove(Activity activity) {
        for (int i = 0, size = sActivity.size(); i < size; i++) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity innerActivity = activityWeakReference.get();
            if (innerActivity == activity) {
                sActivity.remove(activityWeakReference);
                break;
            }
        }
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
        for (int i = sListener.size()-1;i>=0;i--){
            AppLifecycleCallbacks listen = sListener.get(i);
            if (listen==null) {
                sListener.remove(i);
                Log.i("ActivityManager","空对象删除"+i);
                continue;
            }
            if (listen!=null){
                listen.onBackToApp();
            }
        }
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        isRunInBackground = true;

        for (int i = sListener.size()-1;i>=0;i--){
            AppLifecycleCallbacks listen = sListener.get(i);
            if (listen==null) {
                sListener.remove(i);
                Log.i("ActivityManager","空对象删除");
                continue;
            }
            if (listen!=null){
                listen.onLeaveApp();
            }
        }
    }


    //region 生命周期--Application.ActivityLifecycleCallbacks
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        //activity  create时，添加activity
        add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //activity  start时，可以做一些操作，例如记录此activity回到前台的时间等
        appCount++;
        if (isRunInBackground) {
            //应用从后台回到前台 需要做的操作
            back2App(activity);
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        //activity  resume时，可以做一些操作，例如让一些后台任务重新开启，或者app切换到前台的时间等
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //activity  pause时，可以做一些操作，例如暂停一些后台任务
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //activity  stop时，可以做一些操作，例如记录app切换到后台的时间等
        appCount--;
        if (appCount == 0) {
            //应用进入后台 需要做的操作
            leaveApp(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //保存状态
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //activity  destroy时，移除activity
        remove(activity);
    }
    //endregion
}
