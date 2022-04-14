package com.loong.common.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.loong.common.R;
import com.loong.common.event.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.jessyan.autosize.AutoSizeConfig;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity<T extends ViewBinding> extends SupportActivity {
    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取binding对象
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>)((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T)method.invoke(null,getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setImmersionBar();

        initView();
        initData();
    }
//
//    /**
//     * 重写 getResource 方法，防止系统字体影响
//     */
//    @Override
//    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
//        Resources resources = super.getResources();
//        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
//            android.content.res.Configuration configuration = resources.getConfiguration();
//            configuration.fontScale = 1.0f;
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        }
//        return resources;
//    }

    protected abstract void initView();
    protected void initData(){}

    protected void setImmersionBar(){
        View mStatusBarView = findViewById(R.id.view_status_bar_placeholder);
        if (mStatusBarView != null) {
            ImmersionBar.with(this)
                    .statusBarView(mStatusBarView)
                    .statusBarColor(R.color.colorPrimary)
                    .statusBarDarkFont(false)
                    .navigationBarColor(R.color.color_white)
                    .navigationBarDarkIcon(true)
                    .init();
        }
    }


    public Context getContext(){
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(EventBusMessage event) {

    }




    //region 设置标题和返回按钮
    /**
     * 设置APPBar的标题
     * @param title 标题文案
     */
    protected void setTitle(String title){
        TextView tvTitle = findViewById(R.id.tv_title_bar_content);
        if (tvTitle!=null){
            tvTitle.setText(title);
        }
    }
    /**
     * 设置APPBar的标题
     * @param title 标题文案
     */
    protected void setTitleRes(@StringRes int title){
        TextView tvTitle = findViewById(R.id.tv_title_bar_content);
        if (tvTitle!=null){
            tvTitle.setText(title);
        }
    }

    /**
     * 设置APPBar的标题和返回按钮
     * @param title 标题文案
     */
    protected void setTitleWithBack(String title){
        setTitle(title);
        setActivityBack();
    }

    /**
     * 设置AppBar的返回
     */
    protected void setActivityBack(){
        View leftback = findViewById(R.id.ll_title_bar_leftback);
        if (leftback!=null){
            leftback.setVisibility(View.VISIBLE);
            leftback.setOnClickListener(v -> onClickLeftBack());
        }
    }
    /**
     * 设置APPBar的搜索提示和返回按钮
     * @param hint 搜索提示
     */
    protected void setSearchHintWithBack(String hint){
        EditText etSearch = findViewById(R.id.etSearch);
        if (etSearch!=null){
            etSearch.setHint(hint);
        }
        setActivityBack();
    }

    //endregion

    //region 右侧--独立图标
    /**
     * 获取APPBar右侧[图标事件]的图片
     * @return 返回图标ImageView
     */
    protected ImageView getImageRightAction(){
        ImageView ivRight = findViewById(R.id.iv_title_bar_right);
        ivRight.setVisibility(View.VISIBLE);
        return ivRight;
    }
    /**
     * 设置APPBar右侧[图标事件]的事件
     * @param action
     */
    protected void setRightImageActionClickListener(@Nullable View.OnClickListener action){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_right);
        llRight.setOnClickListener(action);
    }
    /**
     * 设置APPBar右侧[图标事件]的图片
     * @param resId
     */
    protected void setImageRightAction(@DrawableRes int resId){
        ImageView ivRight = findViewById(R.id.iv_title_bar_right);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
    }
    //endregion


    //region 右侧--图标和标题
    protected void setRightTitleVisibility(int visibility){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_rigthAction);
        if (llRight!=null) {
            llRight.setVisibility(visibility);
        }
    }

    /**
     * 设置APPBar右侧[标题事件]的事件
     * @param action
     */
    protected void setRightTitleActionClickListener(@Nullable View.OnClickListener action){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_rigthAction);
        llRight.setOnClickListener(action);
    }
    /**
     * 获取APPBar右侧[标题事件]的左侧图标
     * @return 返回图标ImageView
     */
    protected ImageView getRightTitleActionLeftImageView(){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_rigthAction);
        if (llRight!=null) {
            llRight.setVisibility(View.VISIBLE);
        }
        ImageView ivLeft = findViewById(R.id.iv_title_bar_rigthAction);
        ivLeft.setVisibility(View.VISIBLE);
        return ivLeft;
    }
    /**
     * 获取APPBar右侧[标题事件]的标题
     * @return 返回标题TextView
     */
    protected TextView getRightTitleActionTextView(){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_rigthAction);
        if (llRight!=null) {
            llRight.setVisibility(View.VISIBLE);
        }
        TextView tvRight = findViewById(R.id.tv_title_bar_rigthAction);
        tvRight.setVisibility(View.VISIBLE);
        return tvRight;
    }

    protected void setRightTitleActionAndLeftImage(@DrawableRes int resId,@Nullable View.OnClickListener action){
        LinearLayout llRight = findViewById(R.id.ll_title_bar_rigthAction);
        if (llRight!=null) {
            llRight.setVisibility(View.VISIBLE);
        }
        ImageView ivLeft = getRightTitleActionLeftImageView();
        ivLeft.setVisibility(View.VISIBLE);
        setRightTitleActionClickListener(action);
    }

    /**
     * 设置APPBar的右侧功能标题
     * @param title 标题文案
     */
    protected void setRightTitleActionTitle(String title){
        TextView tvRight = getRightTitleActionTextView();
        tvRight.setText(title);
    }
    /**
     * 设置APPBar的右侧功能标题
     * @param title 标题文案
     * @param action 点击功能
     */
    protected void setRightTitleActionAndTitle(String title, @Nullable View.OnClickListener action){
        TextView tvRight = getRightTitleActionTextView();
        tvRight.setText(title);
        setRightTitleActionClickListener(action);
    }

    //endregion

    /**
     * 点击APPBAR的返回按钮
     */
    protected void onClickLeftBack(){
        finish();
    }

}



/**
        e.g.
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(layoutInflater);
*/
