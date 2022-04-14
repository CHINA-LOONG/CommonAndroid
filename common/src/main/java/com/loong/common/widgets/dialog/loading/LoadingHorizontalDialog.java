package com.loong.common.widgets.dialog.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.loong.common.R;
import com.loong.common.widgets.view.AVLoadingIndicatorView;


/*
    水平布局的加载框
 */
public class LoadingHorizontalDialog extends Dialog {

    private String mLaodingText;
    private String mIndicatorName;
    private boolean isCancelable;

    private LoadingHorizontalDialog(@NonNull Context context, String loadingText, String indicatorName, boolean isCancelable) {
        super(context, R.style.CommonDialogStyle);
        this.mLaodingText = loadingText;
        this.mIndicatorName = indicatorName;
        this.isCancelable = isCancelable;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_horizontal_layout);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(isCancelable);
        setCancelable(isCancelable);
        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    private void initView() {
        AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loadingView);
        TextView loadingMessageTv = (TextView) findViewById(R.id.loadingMessageTv);
        loadingIndicatorView.setIndicator(mIndicatorName);
        loadingIndicatorView.setIndicatorColor(Color.parseColor("#F22D354D"));

        loadingIndicatorView.show();
        loadingMessageTv.setText(mLaodingText);
    }

    public LoadingHorizontalDialog shown() {
        show();
        return this;
    }

    public static class Builder {

        private Context mContext;
        private String mLoadigText = "加载中...";
        private String mIndicatorName = "LineSpinFadeLoaderIndicator";
        private boolean isCancelable = false;

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

        public LoadingHorizontalDialog build() {
            return new LoadingHorizontalDialog(mContext, mLoadigText, mIndicatorName, isCancelable);
        }
    }
}


