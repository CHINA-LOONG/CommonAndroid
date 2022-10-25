package com.loong.common.network;

import com.loong.common.network.token.TokenInterceptor;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2022-10-24 15:04:02
 * @desc : 多网络管理器
 */
public class MultiNetworkManager {

    private static MultiNetworkManager mInstance;
    public static MultiNetworkManager getInstance() {
        if (mInstance == null) {
            synchronized (MultiNetworkManager.class) {
                if (mInstance == null) {
                    mInstance = new MultiNetworkManager();
                }
            }
        }
        return mInstance;
    }

    private static Map<String, Retrofit> retrofitMap = new HashMap<>();

    /**
     * 初始化网络服务
     *
     * @param baseUrl 设置API的根地址[以/结尾]
     * @return
     */
    public Retrofit init(String baseUrl) {
        return init(baseUrl, (String) null);
    }

    /**
     * 初始化网络服务
     *
     * @param baseUrl    设置API的根地址[以/结尾]
     * @param refreshUrl 设置自动刷新token的API地址
     * @return
     */
    public Retrofit init(String baseUrl, String refreshUrl) {
        if (refreshUrl != null && refreshUrl.length() > 0) {
            TokenInterceptor tokenInterceptor = new TokenInterceptor(baseUrl, refreshUrl);
            return init(baseUrl, tokenInterceptor);
        } else {
            return init(baseUrl, (TokenInterceptor) null);
        }
    }

    /**
     * 初始化网络服务
     *
     * @param baseUrl          设置API的根地址[以/结尾]
     * @param tokenInterceptor 设置自动刷新token的拦截器
     * @return
     */
    public Retrofit init(String baseUrl, TokenInterceptor tokenInterceptor) {

        if (retrofitMap.get(baseUrl) == null) {
            synchronized (MultiNetworkManager.class) {
                if (retrofitMap.get(baseUrl) == null) {

                    OkHttpClient.Builder okHttpClientBuild = new OkHttpClient().newBuilder();

                    //信任所有服务器地址
                    okHttpClientBuild.hostnameVerifier((s, sslSession) -> true);

                    // 设置https的证书信任
                    try {
                        //信任所有服务器地址
                        okHttpClientBuild.hostnameVerifier((s, sslSession) -> true);

                        //创建管理器
                        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] x509Certificates,
                                    String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] x509Certificates,
                                    String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }};

                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        trustManagerFactory.init((KeyStore) null);
                        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                        }
                        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

                        //为OkHttpClient设置sslSocketFactory
                        okHttpClientBuild.sslSocketFactory(sslContext.getSocketFactory(), trustManager);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 设置自动刷新的拦截器
                    if (tokenInterceptor != null) {
                        okHttpClientBuild.addInterceptor(tokenInterceptor);
                    }
                    // 设置日志输出的拦截器
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.tag("HttpLog").d(message)).setLevel(HttpLoggingInterceptor.Level.BODY);
                    okHttpClientBuild.addInterceptor(loggingInterceptor);

                    OkHttpClient client = okHttpClientBuild
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(baseUrl)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    retrofitMap.put(baseUrl, retrofit);
                }
            }
        }
        return retrofitMap.get(baseUrl);
    }

    public <T> T getRequest(String baseUrl, Class<T> clazz) {
        return retrofitMap.get(baseUrl).create(clazz);
    }

    public Retrofit getRetrofit(String baseUrl) {
        return retrofitMap.get(baseUrl);
    }

}
