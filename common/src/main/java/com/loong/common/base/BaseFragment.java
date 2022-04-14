package com.loong.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.loong.common.event.EventBusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/16 22:10
 * @desc :
 */
public abstract class BaseFragment<T extends ViewBinding> extends SupportFragment {
    protected T binding;

    public Activity _mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getParams();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>)((ParameterizedType)superclass).getActualTypeArguments()[0];
        try {
            Method method =aClass.getDeclaredMethod("inflate",LayoutInflater.class,ViewGroup.class,boolean.class);
            binding = (T) method.invoke(null, getLayoutInflater(),container,false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        View view = binding.getRoot();


        initView();
        initData();
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    protected abstract void initView();
    protected void initData(){}

    protected void setImmersionBar(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._mActivity = activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(EventBusMessage event) {

    }

    /**
     * 获取页面传递的参数
     */
    public void getParams() { }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setImmersionBar();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBar.destroy(this);
    }
}
