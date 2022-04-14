package com.loong.common.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jakewharton.rxbinding2.view.RxView;
import com.loong.common.R;
import com.loong.common.widgets.ProgressButton;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


//版本更新弹窗
public class VersionUpdateDialog extends Dialog {

    private Context mContext;
    private UpdateBean mUpdateBean;
    private OnUpdateClickListener mOnUpdateClickListener;
    private OnUpdateErrorListener mOnUpdateErrorListener;

    private ProgressButton progressBt;


    private VersionUpdateDialog(@NonNull Context context, UpdateBean updateBean, OnUpdateClickListener onUpdateClickListener, OnUpdateErrorListener onUpdateErrorListener) {
        super(context, R.style.MyUsualDialog);
        mContext = context;
        mUpdateBean = updateBean;
        mOnUpdateClickListener = onUpdateClickListener;
        mOnUpdateErrorListener = onUpdateErrorListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_update_version_layout);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }


    private void initView() {

        TextView versionContentTv = (TextView) findViewById(R.id.versionContentTv);
        progressBt = (ProgressButton) findViewById(R.id.progressBt);
        versionContentTv.setText(mUpdateBean.getResult().getAppInfo().getUpdDesc());

        String updateType = mUpdateBean.getResult().getAppInfo().getUpdateType();

        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setVisibility(updateType.equals("forceUpdate")?View.GONE:View.VISIBLE);
        ivClose.setOnClickListener(v -> {dismiss();});

        TextView versionProblemsTv = (TextView) findViewById(R.id.versionProblemsTv);
        versionProblemsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnUpdateErrorListener != null) {
                    mOnUpdateErrorListener.onUpdateError();
                }

            }
        });

        RxView.clicks(progressBt)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (progressBt.getText().toString().trim().equals("立即升级")) {
                            mOnUpdateClickListener.onUpdate("立即升级");
                        } else if (progressBt.getText().toString().trim().equals("下载失败")) {

                        } else if (progressBt.getText().toString().trim().equals("立即安装")) {
                            mOnUpdateClickListener.onUpdate("立即安装");
                        }
                    }
                });


    }

    public void setProgress(int progress) {
        progressBt.setProgress(progress);
        progressBt.setText(progress + " %");
    }

    public void setButtonText(String message) {
        progressBt.setText(message);
    }

    public VersionUpdateDialog shown() {
        show();
        return this;
    }

    public static class Builder {


        private Context mContext;
        private UpdateBean updateBean;
        private OnUpdateClickListener onUpdateClickListener;
        private OnUpdateErrorListener onUpdateErrorListener;


        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setUpdateBean(UpdateBean updateBean) {
            this.updateBean = updateBean;
            return this;
        }


        public Builder setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
            this.onUpdateClickListener = onUpdateClickListener;
            return this;
        }

        public Builder setOnUpdateErrorListener(OnUpdateErrorListener onUpdateErrorListener) {
            this.onUpdateErrorListener = onUpdateErrorListener;
            return this;
        }


        public VersionUpdateDialog build() {
            return new VersionUpdateDialog(mContext, updateBean, onUpdateClickListener, onUpdateErrorListener);
        }
    }


    public interface OnUpdateClickListener {

        void onUpdate(String updateStr);
    }

    public interface OnUpdateErrorListener {
        void onUpdateError();
    }
}
