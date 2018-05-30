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

public class HttpUtil2 {
    public static final String BASE_URL = "http://192.168.2.21:8080/";

    private HttpService httpService;

    private static HttpUtil2 INSTANCE;

    private HttpUtil2(Context context, boolean isSaveCookie) {

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

        int DEFAULT_TIMEOUT = 5;

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

    public static HttpUtil2 getInstance(Context context, boolean isSaveCookie) {
        INSTANCE = new HttpUtil2(context, isSaveCookie);
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
     * 获取未结束会议
     *
     * @param subscriber
     * @param pk
     * @param pageNum
     * @param pageSize
     */
    public void getUnfinishMeeting(Subscriber subscriber, String pk, int pageNum, int pageSize) {
        Observable observable = httpService.getUnfinishMetting(pk, pageNum, pageSize);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取会议
     *
     * @param subscriber
     * @param pk
     * @param pageNum
     * @param pageSize
     */
    public void getAllMeeting(Subscriber subscriber, String pk, int pageNum, int pageSize) {
        Observable observable = httpService.getAllMetting(pk, pageNum, pageSize);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取我的会议
     *
     * @param subscriber
     * @param pk
     * @param pageNum
     * @param pageSize
     */
    public void getMyMeeting(Subscriber subscriber, String pk, int pageNum, int pageSize) {
        Observable observable = httpService.getMyMetting(pk, pageNum, pageSize);
        toSubscribe(observable, subscriber);
    }
}
