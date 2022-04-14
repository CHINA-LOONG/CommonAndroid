package com.loong.common.widgets.dialog.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loong.common.R;


/**
 * 网页加载页面顶部加载中弹窗
 */
public class WebLoadingDialog extends Dialog {

    private boolean mCancelable = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;//使用SwipeRefreshLayout做刷新Loading


    public WebLoadingDialog(@NonNull Context context) {
        this(context, R.style.WebLoadingDialogStyle);
    }

    public WebLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
//        // 设置窗口大小
//        WindowManager windowManager = getWindow().getWindowManager();
//        int screenWidth = windowManager.getDefaultDisplay().getWidth();
//        int screenHeight = windowManager.getDefaultDisplay().getHeight();
//        WindowManager.LayoutParams attributes = getWindow().getAttributes();
//        attributes.alpha =1f;
//        attributes.width = screenWidth;
//        attributes.height = screenHeight;
//        getWindow().setAttributes(attributes);

        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCancelable);
        getWindow().setGravity(Gravity.TOP);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        //设置样式刷新显示的位置
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 120);
        mSwipeRefreshLayout.setSize(140);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_black);

        mSwipeRefreshLayout.setEnabled(false);//禁用下拉刷新，只作为加载动画显示
        //开始刷新
        mSwipeRefreshLayout.setRefreshing(true);

    }


    @Override
    public void show() {
        super.show();
        //开始刷新
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        //停止刷新
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 屏蔽返回键
            return mCancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}
