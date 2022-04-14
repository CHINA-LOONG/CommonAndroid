package com.loong.common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loong.common.bean.BaseBeanT;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ikidou.reflect.TypeBuilder;
import ikidou.reflect.TypeToken;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 17:37
 * @desc :
 */
public class GsonUtil {

    private static Gson gson =null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
/*    // data 为 object 的情况
    {"code":"0","message":"success","data":{"name":"loong","age":18}}
    // data 为 array 的情况
    {"code":"0","message":"success","data":[{"name":"loong","age":18},{"name":"cui","age":16}]}*/

    public static <T> BaseBeanT<T> fromJsonObject(Reader reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(BaseBeanT.class, new Class[]{clazz});
        return gson.fromJson(reader, type);
    }

    public static <T> BaseBeanT<List<T>> fromJsonArray(Reader reader,Class<T> clazz){
        Type listType = new ParameterizedTypeImpl(List.class,new Class[]{clazz});
        Type type = new ParameterizedTypeImpl(BaseBeanT.class,new Type[]{listType});
        return gson.fromJson(reader,type);
    }

    public <T> BaseBeanT<T> fromJsonToObject(Reader reader,Class<T> clazz){
        Type type = TypeBuilder
                .newInstance(BaseBeanT.class)
                .addTypeParam(clazz)
                .build();
        return gson.fromJson(reader,type);
    }

    public <T> BaseBeanT<List<T>> fromJsonToArray(Reader reader,Class<T> clazz){
        Type type = TypeBuilder
                .newInstance(BaseBeanT.class)
                .beginSubType(List.class)
                .addTypeParam(clazz)
                .endSubType()
                .build();
        return gson.fromJson(reader,type);
    }


        /**
         * 把一个map变成json字符串
         * @param map
         * @return
         */
        public static String parseMapToJson(Map<?, ?> map) {
            try {
                return gson.toJson(map);
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * 把一个json字符串变成对象
         * @param json
         * @param cls
         * @return
         */
        public static <T> T parseJsonToBean(String json, Class<T> cls) {
            T t = null;
            try {
                t = gson.fromJson(json, cls);
            } catch (Exception e) {

            }
            return t;
        }

        /**
         * 把json字符串变成map
         * @param json
         * @return
         */
        public static HashMap<String, Object> parseJsonToMap(String json) {
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            HashMap<String, Object> map = null;
            try {
                map = gson.fromJson(json, type);
            } catch (Exception e) {
            }
            return map;
        }

        /**
         * 把json字符串变成集合
         * params: new TypeToken<List<yourbean>>(){}.getType(),
         *
         * @param json
         * @param type  new TypeToken<List<yourbean>>(){}.getType()
         * @return
         */
        public static List<?> parseJsonToList(String json, Type type) {
            List<?> list = gson.fromJson(json, type);
            return list;
        }

        /**
         *
         * 获取json串中某个字段的值，注意，只能获取同一层级的value
         *
         * @param json
         * @param key
         * @return
         */
        public static String getFieldValue(String json, String key) {
            if (TextUtils.isEmpty(json))
                return null;
            if (!json.contains(key))
                return "";
            JSONObject jsonObject = null;
            String value = null;
            try {
                jsonObject = new JSONObject(json);
                value = jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return value;
        }

        /**
         * 格式化json
         * @param uglyJSONString
         * @return
         */
        public static String jsonFormatter(String uglyJSONString){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(uglyJSONString);
            String prettyJsonString = gson.toJson(je);
            return prettyJsonString;
        }

}
