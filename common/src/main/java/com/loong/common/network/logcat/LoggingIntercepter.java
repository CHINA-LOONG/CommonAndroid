package com.loong.common.network.logcat;

import com.loong.common.utils.LoggerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

/**
 *
 * 自定义okhttp拦截器，用来打印 网络请求信息
 */
public class LoggingIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String method = request.method();
        JSONObject jsonObject = new JSONObject();

        if ("POST".equals(method) || "PUT".equals(method)) {
            if (request.body() instanceof FormBody){
                FormBody body = (FormBody) request.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        try {
                            jsonObject.put(body.name(i), body.encodedValue(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Timber.tag("request").e(String.format("发送请求 %s on %s  %nRequestParams:%s%nMethod:%s",
                        request.url(), chain.connection(), jsonObject.toString(), request.method()));
            }else {
                Buffer buffer = new Buffer();
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    request.body().writeTo(buffer);
                    String body = buffer.readUtf8();
                    Timber.tag("request").e(String.format("发送请求 %s on %s  %nRequestParams:%s%nMethod:%s",
                            request.url(), chain.connection(), body, request.method()));
                }
            }
        }else {
            Timber.tag("request").e(String.format("发送请求 %s on %s%nMethod:%s",
                    request.url(), chain.connection(), request.method()));
        }

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        Timber.tag("response").e(String.format("Retrofit接收响应: %s %n返回json:【%s】 %n耗时：%.1fms",
                response.request().url(),
                responseBody.string(),
                (t2 - t1) / 1e6d
        ));
        return response;
    }
}
