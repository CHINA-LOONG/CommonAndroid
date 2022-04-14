package com.loong.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 17:55
 * @desc :
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @NonNull
    @Override
    public Type getRawType() {
        return raw;
    }

    @Nullable
    @Override
    public Type getOwnerType() {
        return null;
    }
}
