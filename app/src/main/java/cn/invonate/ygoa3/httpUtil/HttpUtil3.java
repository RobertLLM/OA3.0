package cn.invonate.ygoa3.httpUtil;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpUtil3 {
    public static final String BASE_URL = "http://oayj.yong-gang.cn:8080/innovate-api/";
//    public static final String BASE_URL = "http://192.168.1.27:8080/innovate-api/";
//    public static final String BASE_URL = "http://10.181.5.59:8080/innvote-api/";

    private HttpService httpService;

    private static HttpUtil3 INSTANCE;

    private HttpUtil3(Context context, boolean isSaveCookie) {

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

    public static HttpUtil3 getInstance(Context context, boolean isSaveCookie) {
        INSTANCE = new HttpUtil3(context, isSaveCookie);
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
     * 发送邮件
     *
     * @param subscriber
     * @param pk
     */
    public void sendMail(Subscriber subscriber, String pk,
                         RequestBody account,
                         RequestBody address,
                         RequestBody subject,
                         RequestBody context,
                         RequestBody cc,
                         RequestBody ref,
                         RequestBody[] index,
                         RequestBody folder,
                         RequestBody isReply,
                         List<MultipartBody.Part> parts) {
        Observable observable = httpService.sendMail(pk, account, address, subject, context, cc, ref, index, folder, isReply
                , parts
        );
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取邮件列表
     *
     * @param subscriber
     * @param pk
     * @param account
     * @param page
     * @param rows
     * @param folder
     * @param searchValue
     */
    public void getMailList(Subscriber subscriber, String pk, String account, int page, int rows, String folder, String searchValue) {
        Observable observable = httpService.getMailList(pk, account, page, rows, folder, searchValue);
        toSubscribe(observable, subscriber);
    }

    /**
     * @param subscriber
     * @param pk
     * @param account
     * @param msgID
     * @param folder
     */
    public void appendMailToFolder(Subscriber subscriber, String pk, String account, int[] msgID, String folder) {
        Observable observable = httpService.appendMailToFolder(pk, account, msgID, folder);
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置获取邮件正文
     *
     * @param subscriber
     * @param pk
     * @param account
     * @param msgID
     * @param folder
     */
    public void getMessage(Subscriber subscriber, String pk, String account, int msgID, String folder) {
        Observable observable = httpService.getMessage(pk, account, msgID, folder);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取附件
     *
     * @param subscriber
     * @param pk
     * @param account
     * @param msgID
     * @param folder
     * @param index
     */
    public void getAttachments(Subscriber subscriber, String pk, String account, int msgID, String folder, int index) {
        Observable observable = httpService.getAttachments(pk, account, msgID, folder, index);
        toSubscribe(observable, subscriber);
    }

    /**
     * 保存至草稿箱
     *
     * @param subscriber
     * @param pk
     * @param account
     * @param address
     * @param cc
     * @param subject
     * @param context
     * @param ref
     * @param index
     * @param parts
     */
    public void saveToDrafts(Subscriber subscriber, String pk,
                             RequestBody account,
                             RequestBody address,
                             RequestBody cc,
                             RequestBody subject,
                             RequestBody context,
                             RequestBody ref,
                             RequestBody folder,
                             RequestBody[] index,
                             List<MultipartBody.Part> parts) {
        Observable observable = httpService.saveToDrafts(pk, account, address, cc, subject, context, ref, folder, index, parts);
        toSubscribe(observable, subscriber);
    }

}
