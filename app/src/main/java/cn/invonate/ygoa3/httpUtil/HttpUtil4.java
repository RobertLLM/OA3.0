package cn.invonate.ygoa3.httpUtil;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpUtil4 {
    public static final String BASE_URL = "http://192.168.1.34/";

    private HttpService httpService;

    private static HttpUtil4 INSTANCE;

    private HttpUtil4(Context context, boolean isSaveCookie) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient(context, isSaveCookie))
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    private OkHttpClient getOkHttpClient(Context context, boolean isSaveCookie) {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BASIC;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("lyy", "OkHttp====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp

        int DEFAULT_TIMEOUT = 50;

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间;
        if (isSaveCookie) {
            httpClientBuilder.interceptors().add(new ReceivedCookiesInterceptor(context));
        }

        if (!isSaveCookie) {
            httpClientBuilder.interceptors().add(new AddCookiesInterceptor(context));
        }

        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    public static HttpUtil4 getInstance(Context context, boolean isSaveCookie) {
        INSTANCE = new HttpUtil4(context, isSaveCookie);
        return INSTANCE;
    }

    /**
     * 统一配置观察者
     *
     * @param o
     * @param <T>
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 获取集团通讯录部门
     *
     * @param subscriber
     * @param pk
     */
    public void getDepart(Subscriber subscriber, String pk) {
        Observable observable = httpService.getDepart(pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取部门的统计信息
     *
     * @param subscriber
     * @param pk
     */
    public void getDepartInfo(Subscriber subscriber, String pk) {
        Observable observable = httpService.getDepartInfo(pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取部门成员列表
     *
     * @param subscriber
     * @param pk
     */
    public void getEmpList(Subscriber subscriber, String pk) {
        Observable observable = httpService.getEmpList(pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人信息
     *
     * @param subscriber
     * @param code
     */
    public void getEmpInfo(Subscriber subscriber, String code) {
        Observable observable = httpService.getEmpInfo(code);
        toSubscribe(observable, subscriber);
    }

}
