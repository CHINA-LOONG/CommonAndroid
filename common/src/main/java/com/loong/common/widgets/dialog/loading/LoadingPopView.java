package com.loong.common.widgets.dialog.loading;

/**
 * @author Created by linyincongxingkeji on  2021/4/23 15:50
 * @description
 */

//public class LoadingPopView extends CenterPopupView {
//
//    private String mLaodingText = null;
//    private String mIndicatorName = null;
//    private boolean isCancelable = true;
//
//    public LoadingPopView(@NonNull Context context, String loadingText, String indicatorName, boolean isCancelable) {
//        super(context);
//        this.mLaodingText = loadingText;
//        this.mIndicatorName = indicatorName;
//        this.isCancelable = isCancelable;
//    }
//
//    private void initView() {
//        AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loadingView);
//        TextView loadingTextTv = (TextView) findViewById(R.id.loadingText);
//        loadingIndicatorView.setIndicator(mIndicatorName);
//        loadingIndicatorView.setIndicatorColor(Color.parseColor("#F22D354D"));
//        loadingIndicatorView.show();
//        loadingTextTv.setText(mLaodingText);
//    }
//
//    @Override
//    protected int getImplLayoutId() {
//        return R.layout.dialog_loading_vertical_layout;
//    }
//
//    @Override
//    protected void onCreate() {
//        super.onCreate();
//        initView();
//
//    }
//}
