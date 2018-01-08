package cn.invonate.ygoa.httpUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class HttpUtil {
    //public static final String BASE_URL = "http://esale.yong-gang.com/";
    //private static final String BASE_URL = "http://192.168.202.180:8000/";
    //public static final String BASE_URL = "http://172.20.1.17:8000";
    //private static final String BASE_URL = "http://192.168.2.1/";
    public static final String BASE_URL = "http://192.168.3.97:8080";

    private HttpService httpService;

    private static HttpUtil INSTANCE = new HttpUtil();

    private HttpUtil() {

        int DEFAULT_TIMEOUT = 8;

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 用来统一处理Http的flag,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
//    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
//
//        @Override
//        public T call(HttpResult<T> httpResult) {
//            Log.i("result", httpResult.toString());
//            if (httpResult.getResultCode() == 1) {
//                String msg = httpResult.getResultMsg();
//                if (msg == null) {
//                    msg = "msg为空";
//                }
//                throw new RuntimeException(msg);
//            }
//            return httpResult.getData();
//        }
//    }

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
     * @param subscriber 观察者监听
     * @param userName   用户名
     * @param password   密码
     */
    public void login(Subscriber subscriber, String userName, String password) {
        Observable observable = httpService.login(userName, password);
        toSubscribe(observable, subscriber);
    }

    /**
     * 通讯录查询部门
     *
     * @param subscriber
     * @param id
     */
    public void getDepartment(Subscriber subscriber, String id) {
        Observable observable = httpService.getDepartment(id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 通讯录部门成员
     *
     * @param subscriber
     * @param id
     */
    public void getContacts(Subscriber subscriber, String id) {
        Observable observable = httpService.getContacts(id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 模糊搜索员工信息
     *
     * @param subscriber
     * @param keyword
     * @param page
     * @param row
     */
    public void getMembers(Subscriber subscriber, String keyword, int page, int row) {
        Observable observable = httpService.getMembers(keyword, page, row);
        toSubscribe(observable, subscriber);
    }

}
