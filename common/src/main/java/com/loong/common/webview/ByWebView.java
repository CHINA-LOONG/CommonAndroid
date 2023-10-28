package com.loong.common.webview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.loong.common.R;

import java.lang.ref.WeakReference;

public class ByWebView {

    private WebView mWebView;
    private WebProgress mProgressBar;
    private View mErrorView;
    private int mErrorLayoutId;
    private String mErrorTitle;


    private Activity mActivity;

    private ByWebChromeClient mWebChromeClient;

    private ByLoadJsHolder byLoadJsHolder;

    private ByWebView(Builder builder){

        this.mActivity = builder.mActivity;
        this.mErrorTitle = builder.mErrorTitle;
        this.mErrorLayoutId = builder.mErrorLayoutId;

        FrameLayout parentLayout = new FrameLayout(mActivity);
        // 设置WebView
        setWebView(builder.mCustomWebView);
        parentLayout.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 进度条布局
        handleWebProgress(builder, parentLayout);
        if (builder.mIndex != -1) {
            builder.mWebContainer.addView(parentLayout, builder.mIndex, builder.mLayoutParams);
        } else {
            builder.mWebContainer.addView(parentLayout, builder.mLayoutParams);
        }
        // 配置
        handleSetting();
        // 视频、照片、进度条
        mWebChromeClient = new ByWebChromeClient(mActivity, this);
        mWebChromeClient.setOnByWebChromeCallback(builder.mOnTitleProgressCallback);
        mWebView.setWebChromeClient(mWebChromeClient);

        // 错误页面、页面结束、处理DeepLink
        ByWebViewClient mByWebViewClient = new ByWebViewClient(mActivity, this);
        mByWebViewClient.setOnByWebClientCallback(builder.mOnByWebClientCallback);
        mWebView.setWebViewClient(mByWebViewClient);

        handleJsInterface(builder);
    }


    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void handleJsInterface(Builder builder) {
        if (!TextUtils.isEmpty(builder.mInterfaceName) && builder.mInterfaceObj != null) {
            mWebView.addJavascriptInterface(builder.mInterfaceObj, builder.mInterfaceName);
        }
    }

    public ByLoadJsHolder getLoadJsHolder() {
        if (byLoadJsHolder == null) {
            byLoadJsHolder = new ByLoadJsHolder(mWebView);
        }
        return byLoadJsHolder;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleSetting() {
        WebSettings ws = mWebView.getSettings();
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 网页内容的宽度自适应屏幕
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        } else {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // WebView从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)
     *
     * @param textZoom 默认100
     */
    public void setTextZoom(int textZoom) {
        mWebView.getSettings().setTextZoom(textZoom);
    }


    /**
     * 配置自定义的WebView
     */
    private void setWebView(WebView mCustomWebView){
        if (mCustomWebView!=null){
            mWebView = mCustomWebView;
        }else {
            mWebView = new WebView(mActivity);
        }
    }


    private void handleWebProgress(Builder builder, FrameLayout parentLayout) {
        if (builder.mUseWebProgress) {
            mProgressBar = new WebProgress(mActivity);
            if (builder.mProgressStartColor != 0 && builder.mProgressEndColor != 0) {
                mProgressBar.setColor(builder.mProgressStartColor, builder.mProgressEndColor);
            } else if (builder.mProgressStartColor != 0) {
                mProgressBar.setColor(builder.mProgressStartColor, builder.mProgressStartColor);
            } else if (!TextUtils.isEmpty(builder.mProgressStartColorString)
                    && !TextUtils.isEmpty(builder.mProgressEndColorString)) {
                mProgressBar.setColor(builder.mProgressStartColorString, builder.mProgressEndColorString);
            } else if (!TextUtils.isEmpty(builder.mProgressStartColorString)
                    && TextUtils.isEmpty(builder.mProgressEndColorString)) {
                mProgressBar.setColor(builder.mProgressStartColorString, builder.mProgressStartColorString);
            }
            int progressHeight = ByWebTools.dip2px(parentLayout.getContext(), WebProgress.WEB_PROGRESS_DEFAULT_HEIGHT);
            if (builder.mProgressHeightDp != 0) {
                mProgressBar.setHeight(builder.mProgressHeightDp);
                progressHeight = ByWebTools.dip2px(parentLayout.getContext(), builder.mProgressHeightDp);
            }
            mProgressBar.setVisibility(View.GONE);
            parentLayout.addView(mProgressBar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, progressHeight));
        }
    }

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url) && url.endsWith("mp4") && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mWebView.loadData(ByWebTools.getVideoHtmlBody(url), "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(url);
        }
        if (mProgressBar != null) {
            mProgressBar.show();
        }
        hideErrorView();
    }

    public void reload() {
        hideErrorView();
        mWebView.reload();
    }

    public void onResume() {
        mWebView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        mWebView.resumeTimers();
    }

    public void onPause() {
        mWebView.onPause();
        mWebView.resumeTimers();
    }

    public void onDestroy() {
        if (mWebChromeClient != null && mWebChromeClient.getVideoFullView() != null) {
            mWebChromeClient.getVideoFullView().removeAllViews();
        }
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }
    /**
     * 选择图片之后的回调，在Activity里onActivityResult调用
     */
    public void handleFileChooser(int requestCode, int resultCode, Intent intent) {
        if (mWebChromeClient != null) {
            mWebChromeClient.handleFileChooser(requestCode, resultCode, intent);
        }
    }

    public boolean handleKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isBack();
        }
        return false;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public boolean isBack() {
        // 全屏播放退出全屏
        if (mWebChromeClient.inCustomView()) {
            mWebChromeClient.onHideCustomView();
            return true;

            // 返回网页上一页
        } else if (mWebView.canGoBack()) {
            hideErrorView();
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public WebView getWebView() {
        return mWebView;
    }

    public WebProgress getProgressBar() {
        return mProgressBar;
    }

    /**
     * 显示错误布局
     */
    public void showErrorView() {
        try {
            if (mErrorView == null) {
                FrameLayout parent = (FrameLayout) mWebView.getParent();
                mErrorView = LayoutInflater.from(parent.getContext()).inflate((mErrorLayoutId == 0) ? R.layout.by_load_url_error : mErrorLayoutId, null);
                mErrorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reload();
                    }
                });
                parent.addView(mErrorView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                mErrorView.setVisibility(View.VISIBLE);
            }
            mWebView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏错误布局
     */
    public void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public View getErrorView() {
        return mErrorView;
    }

    String getErrorTitle() {
        return mErrorTitle;
    }

    public static Builder with(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity can not be null .");
        }
        return new Builder(activity);
    }
    /**
     * 修复可能部分h5无故竖屏问题，如果h5里有视频全屏播放请禁用
     */
    public void setFixScreenPortrait(boolean fixScreenPortrait) {
        if (mWebChromeClient != null) {
            mWebChromeClient.setFixScreenPortrait(fixScreenPortrait);
        }
    }

    /**
     * 修复可能部分h5无故横屏问题，如果h5里有视频全屏播放请禁用
     */
    public void setFixScreenLandscape(boolean fixScreenLandscape) {
        if (mWebChromeClient != null) {
            mWebChromeClient.setFixScreenLandscape(fixScreenLandscape);
        }
    }

    public static class Builder{
        // 绑定的界面
        private Activity mActivity;

        // 默认使用进度条
        private boolean mUseWebProgress = true;
        // 进度条 高度
        private int mProgressHeightDp;
        // 进度条 开始颜色
        private int mProgressStartColor;
        private String mProgressStartColorString;
        // 进度条 结束颜色
        private int mProgressEndColor;
        private String mProgressEndColorString;

        // webview的父对象
        private ViewGroup mWebContainer;
        // webview布局参数
        private ViewGroup.LayoutParams mLayoutParams;
        // 添加子项的位置(在子列表的索引)
        private int mIndex = -1;

        // 使用自定义的webview
        private WebView mCustomWebView;
        // 页面加载事件监听
        private OnTitleProgressCallback mOnTitleProgressCallback;
        private OnByWebClientCallback mOnByWebClientCallback;

        //Js监听
        private String mInterfaceName;
        private Object mInterfaceObj;

        // 错误页面布局和标题
        private int mErrorLayoutId;
        private String mErrorTitle;

        public Builder(Activity activity){
            this.mActivity = activity;
        }

        /**
         * WebView容器
         */
        public Builder setWebParent(@NonNull ViewGroup webContainer, ViewGroup.LayoutParams layoutParams) {
            this.mWebContainer = webContainer;
            this.mLayoutParams = layoutParams;
            return this;
        }
        /**
         * WebView容器
         *
         * @param webContainer 外部WebView容器
         * @param index        加入的位置
         * @param layoutParams 对应的LayoutParams
         */
        public Builder setWebParent(@NonNull ViewGroup webContainer, int index, ViewGroup.LayoutParams layoutParams) {
            this.mWebContainer = webContainer;
            this.mIndex = index;
            this.mLayoutParams = layoutParams;
            return this;
        }
        /**
         * @param isUse 是否使用进度条，默认true
         */
        public Builder useWebProgress(boolean isUse) {
            this.mUseWebProgress = isUse;
            return this;
        }
        /**
         * 设置进度条颜色
         *
         * @param color 示例：ContextCompat.getColor(this, R.color.red)
         */
        public Builder useWebProgress(int color) {
            return useWebProgress(color, color, 3);
        }
        /**
         * 设置进度条颜色
         *
         * @param color 示例："#FF0000"
         */
        public Builder useWebProgress(String color) {
            return useWebProgress(color, color, 3);
        }

        /**
         * 设置进度条渐变色颜色
         *
         * @param startColor 开始颜色
         * @param endColor   结束颜色
         * @param heightDp   进度条高度，单位dp
         */
        public Builder useWebProgress(int startColor, int endColor, int heightDp) {
            mProgressStartColor = startColor;
            mProgressEndColor = endColor;
            mProgressHeightDp = heightDp;
            return this;
        }
        public Builder useWebProgress(String startColor, String endColor, int heightDp) {
            mProgressStartColorString = startColor;
            mProgressEndColorString = endColor;
            mProgressHeightDp = heightDp;
            return this;
        }

        /**
         * @param customWebView 自定义的WebView
         */
        public Builder setCustomWebView(WebView customWebView) {
            mCustomWebView = customWebView;
            return this;
        }
        /**
         * @param errorLayoutId 错误页面布局，标题默认“网页打开失败”
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutId) {
            mErrorLayoutId = errorLayoutId;
            return this;
        }

        /**
         * @param errorLayoutId 错误页面布局
         * @param errorTitle    错误页面标题
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutId, String errorTitle) {
            mErrorLayoutId = errorLayoutId;
            mErrorTitle = errorTitle;
            return this;
        }

        /**
         * 添加Js监听
         */
        public Builder addJavascriptInterface(String interfaceName, Object interfaceObj) {
            this.mInterfaceName = interfaceName;
            this.mInterfaceObj = interfaceObj;
            return this;
        }

        /**
         * @param onTitleProgressCallback 返回Title 和 Progress
         */
        public Builder setOnTitleProgressCallback(OnTitleProgressCallback onTitleProgressCallback) {
            this.mOnTitleProgressCallback = onTitleProgressCallback;
            return this;
        }
        /**
         * 页面加载结束监听 和 处理三方跳转链接
         */
        public Builder setOnByWebClientCallback(OnByWebClientCallback onByWebClientCallback) {
            this.mOnByWebClientCallback = onByWebClientCallback;
            return this;
        }

        /**
         * 直接获取ByWebView，避免一定要调用loadUrl()才能获取ByWebView的情况
         */
        public ByWebView get() {
            return new ByWebView(this);
        }

        /**
         * loadUrl()并获取ByWebView
         */
        public ByWebView loadUrl(String url){
            ByWebView byWebView = get();
            byWebView.loadUrl(url);
            return byWebView;
        }
    }


    public static class ByWebChromeClient extends WebChromeClient{

        private WeakReference<Activity> mActivityWeakReference = null;
        private ByWebView mByWebView;
        private ValueCallback<Uri> mUploadMessage;
        private ValueCallback<Uri[]> mUploadMessageForAndroid5;
        private static int RESULT_CODE_FILE_CHOOSER = 1;
        private static int RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5 = 2;

        private View mProgressVideo;
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        private ByFullscreenHolder videoFullView;
        private OnTitleProgressCallback onByWebChromeCallback;
        // 修复可能部分h5无故横屏问题
        private boolean isFixScreenLandscape = false;
        // 修复可能部分h5无故竖屏问题
        private boolean isFixScreenPortrait = false;

        ByWebChromeClient(Activity activity, ByWebView byWebView) {
            mActivityWeakReference = new WeakReference<Activity>(activity);
            this.mByWebView = byWebView;
        }

        void setOnByWebChromeCallback(OnTitleProgressCallback onByWebChromeCallback) {
            this.onByWebChromeCallback = onByWebChromeCallback;
        }

        public void setFixScreenLandscape(boolean fixScreenLandscape) {
            isFixScreenLandscape = fixScreenLandscape;
        }

        public void setFixScreenPortrait(boolean fixScreenPortrait) {
            isFixScreenPortrait = fixScreenPortrait;
        }

        /**
         * 播放网络视频时全屏会被调用的方法
         */
        @SuppressLint("SourceLockedOrientationActivity")
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                if (!isFixScreenLandscape) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                mByWebView.getWebView().setVisibility(View.INVISIBLE);

                // 如果一个视图已经存在，那么立刻终止并新建一个
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
                videoFullView = new ByFullscreenHolder(mActivity);
                videoFullView.addView(view);
                decor.addView(videoFullView);

                mCustomView = view;
                mCustomViewCallback = callback;
                videoFullView.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 视频播放退出全屏会被调用的
         */
        @SuppressLint("SourceLockedOrientationActivity")
        @Override
        public void onHideCustomView() {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                // 不是全屏播放状态
                if (mCustomView == null) {
                    return;
                }
                // 还原到之前的屏幕状态
                if (!isFixScreenPortrait) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

                mCustomView.setVisibility(View.GONE);
                if (videoFullView != null) {
                    videoFullView.removeView(mCustomView);
                    videoFullView.setVisibility(View.GONE);
                }
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();
                mByWebView.getWebView().setVisibility(View.VISIBLE);
            }
        }

        /**
         * 视频加载时loading
         */
        @Override
        public View getVideoLoadingProgressView() {
            if (mProgressVideo == null) {
                mProgressVideo = LayoutInflater.from(mByWebView.getWebView().getContext()).inflate(R.layout.by_video_loading_progress, null);
            }
            return mProgressVideo;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 进度条
            if (mByWebView.getProgressBar() != null) {
                mByWebView.getProgressBar().setWebProgress(newProgress);
            }
            // 当显示错误页面时，进度达到100才显示网页
            if (mByWebView.getWebView() != null
                    && mByWebView.getWebView().getVisibility() == View.INVISIBLE
                    && (mByWebView.getErrorView() == null || mByWebView.getErrorView().getVisibility() == View.GONE)
                    && newProgress == 100) {
                mByWebView.getWebView().setVisibility(View.VISIBLE);
            }
            if (onByWebChromeCallback != null) {
                onByWebChromeCallback.onProgressChanged(newProgress);
            }
        }

        /**
         * 判断是否是全屏
         */
        boolean inCustomView() {
            return (mCustomView != null);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置title
            if (onByWebChromeCallback != null) {
                if (mByWebView.getErrorView() != null && mByWebView.getErrorView().getVisibility() == View.VISIBLE) {
                    onByWebChromeCallback.onReceivedTitle(TextUtils.isEmpty(mByWebView.getErrorTitle()) ? "网页无法打开" : mByWebView.getErrorTitle());
                } else {
                    onByWebChromeCallback.onReceivedTitle(title);
                }
            }
        }

        //扩展浏览器上传文件
        //3.0++版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg);
        }

        //3.0--版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg);
        }

        // For Android > 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            openFileChooserImplForAndroid5(uploadMsg);
            return true;
        }

        private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                mActivity.startActivityForResult(Intent.createChooser(intent, "文件选择"), RESULT_CODE_FILE_CHOOSER);
            }
        }

        private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                mUploadMessageForAndroid5 = uploadMsg;
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");

                mActivity.startActivityForResult(chooserIntent, RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5);
            }
        }

        /**
         * 5.0以下 上传图片成功后的回调
         */
        private void uploadMessage(Intent intent, int resultCode) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

        /**
         * 5.0以上 上传图片成功后的回调
         */
        private void uploadMessageForAndroid5(Intent intent, int resultCode) {
            if (null == mUploadMessageForAndroid5) {
                return;
            }
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }

        /**
         * 用于Activity的回调
         */
        public void handleFileChooser(int requestCode, int resultCode, Intent intent) {
            if (requestCode == RESULT_CODE_FILE_CHOOSER) {
                uploadMessage(intent, resultCode);
            } else if (requestCode == RESULT_CODE_FILE_CHOOSER_FOR_ANDROID_5) {
                uploadMessageForAndroid5(intent, resultCode);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
            // 部分页面可能崩溃
//        request.grant(request.getResources());
        }

        ByFullscreenHolder getVideoFullView() {
            return videoFullView;
        }

        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {
            if (super.getDefaultVideoPoster() == null) {
                return BitmapFactory.decodeResource(mByWebView.getWebView().getResources(), R.drawable.by_icon_video);
            } else {
                return super.getDefaultVideoPoster();
            }
        }
    }
}
