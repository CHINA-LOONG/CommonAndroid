package com.loong.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 12:29
 * @desc : 已启用,请使用后SPUtils
 */
public class SharedPreferencesUtil {
    private static SharedPreferencesUtil mInstance;

    public static SharedPreferencesUtil getInstance() {
        if (mInstance == null) {
            Log.e("SharedPreferencesUtil", "未初始化");
        }
        return mInstance;
    }

    private static final String FILE_NAME = "save_file_name";
    private Context context;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferencesUtil(context);
                }
            }
        }
    }

    protected SharedPreferencesUtil(Context context) {
        this.context = context.getApplicationContext();
        mPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    /**
     * 保存数据到文件
     *
     * @param key
     * @param data
     */
    public void putData(String key, Object data) {
        String type = data.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            mEditor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            mEditor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            mEditor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            mEditor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            mEditor.putLong(key, (Long) data);
        } else {
            Gson gson = new Gson();
            String strJson = gson.toJson(data);
            mEditor.putString(key, strJson);
        }
        mEditor.commit();
    }

    /**
     * 从文件中读取数据
     *
     * @param Key
     * @param defValue
     * @return
     */
    public Object getData(String Key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        //defValue为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return mPreferences.getInt(Key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return mPreferences.getBoolean(Key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return mPreferences.getString(Key, (String) defValue);
        } else if ("Float".equals(type)) {
            return mPreferences.getFloat(Key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return mPreferences.getLong(Key, (Long) defValue);
        } else {
            String strJson = mPreferences.getString(Key, "");
            if (!TextUtils.isEmpty(strJson)) {
                Class clazz = defValue.getClass();
                Gson gson = new Gson();
                return gson.fromJson(strJson, clazz);
            } else {
                return defValue;
            }
        }
    }

    public <T> void saveData(String key, T data) {
        if (data instanceof String) {
            mEditor.putString(key, (String) data).commit();
        } else if (data instanceof Integer) {
            mEditor.putInt(key, (Integer) data).commit();
        } else if (data instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) data).commit();
        } else if (data instanceof Long) {
            mEditor.putLong(key, (Long) data).commit();
        } else if (data instanceof Float) {
            mEditor.putFloat(key, (Float) data).commit();
        } else {
            Gson gson = new Gson();
            String strJson = gson.toJson(data);
            mEditor.putString(key, strJson).commit();
        }
    }

    public <T> T readData(String key, Class<T> clazz) {

        String type = clazz.getSimpleName();
        if ("Integer".equals(type)) {
            return (T) Integer.valueOf(mPreferences.getInt(key, 0));
        } else if ("Boolean".equals(type)) {
            return (T) Boolean.valueOf(mPreferences.getBoolean(key, false));
        } else if ("String".equals(type)) {
            return (T) mPreferences.getString(key, (String) null);
        } else if ("Float".equals(type)) {
            return (T) Float.valueOf(mPreferences.getFloat(key, 0.0f));
        } else if ("Long".equals(type)) {
            return (T) Long.valueOf(mPreferences.getLong(key, 0L));
        } else {
            String strJson = mPreferences.getString(key, "");
            if (!TextUtils.isEmpty(strJson)) {
                Gson gson = new Gson();
                return gson.fromJson(strJson, clazz);
            } else {
                return null;
            }
        }
    }

    /**
     * Put the string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }

    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            mPreferences.edit().putString(key, value).commit();
        } else {
            mPreferences.edit().putString(key, value).apply();
        }
    }

    /**
     * Return the string value in sp.
     *
     * @param key The key of sp.
     * @return the string value if sp exists or {@code ""} otherwise
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    public String getString(@NonNull final String key, final String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }


    public void deleteAll() throws Exception {
        mEditor.clear();
        mEditor.commit();
    }

    public void deleteKey(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public boolean contains(String key) {

        return mPreferences.contains(key);

    }
}
