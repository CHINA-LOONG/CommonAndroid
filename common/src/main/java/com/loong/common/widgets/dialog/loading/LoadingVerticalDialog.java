package com.loong.common.widgets.dialog.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.loong.common.R;
import com.loong.common.widgets.view.AVLoadingIndicatorView;


public class LoadingVerticalDialog extends Dialog {

    private String mLaodingText = null;
    private String mIndicatorName = null;
    private boolean isCancelable = true;

    private LoadingVerticalDialog(@NonNull Context context, String loadingText, String indicatorName, boolean isCancelable) {
        super(context, R.style.CommonDialogStyle);
        this.mLaodingText = loadingText;
        this.mIndicatorName = indicatorName;
        this.isCancelable = isCancelable;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_vertical_layout);
        setCanceledOnTouchOutside(isCancelable);
        setCancelable(isCancelable);
        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    private void initView() {
        AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loadingView);
        TextView loadingTextTv = (TextView) findViewById(R.id.loadingText);
        loadingIndicatorView.setIndicator(mIndicatorName);
        loadingIndicatorView.setIndicatorColor(Color.parseColor("#F22D354D"));
        loadingIndicatorView.show();
        loadingTextTv.setText(mLaodingText);
    }

    public LoadingVerticalDialog shown() {
        show();
        return this;
    }

    public static class Builder {


        private Context mContext;
        private String mLoadigText = "加载中...";
        private String mIndicatorName = "BallSpinFadeLoaderIndicator";
        private boolean isCancelable = true;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setLoadingText(String loadigText) {
            this.mLoadigText = loadigText;
            return this;
        }

        public Builder setIndicatorName(String indicatorName) {
            this.mIndicatorName = indicatorName;
            return this;
        }

        public Builder setIsCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public LoadingVerticalDialog build() {
            return new LoadingVerticalDialog(mContext, mLoadigText, mIndicatorName,isCancelable);
        }
    }
}


