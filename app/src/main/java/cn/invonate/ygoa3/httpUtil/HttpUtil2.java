package cn.invonate.ygoa3.httpUtil;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.invonate.ygoa3.Entry.AddMeeting;
import cn.invonate.ygoa3.Entry.DeletePerson;
import cn.invonate.ygoa3.Entry.EditMeeting;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.Entry.Reason;
import okhttp3.MultipartBody;
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
    //    public static final String BASE_URL = "http://oaapi.yong-gang.cn:8080/innovate-api/";
//    public static final String BASE_URL = "http://192.168.1.27:8080/innovate-api/";
    public static final String BASE_URL = "http://192.168.2.21:8080/innovate-api/";
//    public static final String BASE_URL = "http://10.181.5.77:8080/innovate-api/";

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

        int DEFAULT_TIMEOUT = 15;

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间;

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

    /**
     * 删除会议
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void deleteMeet(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.deleteMeet(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 会议详情
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void getMeetingDetail(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.getMeetingDetail(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 会议动态
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void getDynamic(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.getDynamic(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 会议动态
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void getRepeat(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.getRepeat(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 确认参加
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void attend_sure(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.attend_sure(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 不参加
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void attend_not(Subscriber subscriber, String url, String pk, Reason reason) {
        Observable observable = httpService.attend_not(url, pk, reason);
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改参会人
     *
     * @param subscriber
     * @param url
     * @param pk
     * @param list
     */
    public void attend_join(Subscriber subscriber, String url, String pk, List<MeetingDetail.ResultBean.AttendListBean> list) {
        Observable observable = httpService.attend_Join(url, pk, list);
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除参会人
     *
     * @param subscriber
     * @param pk
     * @param person
     */
    public void delete_join(Subscriber subscriber, String pk, DeletePerson person) {
        Observable observable = httpService.delete_Join(pk, person);
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改会议
     *
     * @param subscriber
     * @param pk
     * @param meet
     */
    public void edit_meet(Subscriber subscriber, String pk, EditMeeting meet) {
        Observable observable = httpService.edit_meeting(pk, meet);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取会议地点
     *
     * @param subscriber
     * @param pk
     */
    public void getLocation(Subscriber subscriber, String pk) {
        Observable observable = httpService.getLocation(pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取会议室
     *
     * @param subscriber
     * @param pk
     * @param pageNum
     * @param pageSize
     * @param meetingDate
     * @param districtId
     */
    public void getRoom(Subscriber subscriber, String pk, int pageNum, int pageSize, String meetingDate, String districtId) {
        Observable observable = httpService.getRoom(pk, pageNum, pageSize, meetingDate, districtId);
        toSubscribe(observable, subscriber);
    }

    /**
     * 预约会议
     *
     * @param subscriber
     * @param pk
     * @param meet
     */
    public void add_meet(Subscriber subscriber, String pk, AddMeeting meet) {
        Observable observable = httpService.add_meeting(pk, meet);
        toSubscribe(observable, subscriber);
    }

    /**
     * 会议签到
     *
     * @param subscriber
     * @param url
     * @param pk
     */
    public void sign(Subscriber subscriber, String url, String pk) {
        Observable observable = httpService.sign(url, pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取待办会议数
     *
     * @param subscriber
     * @param pk
     */
    public void getMeetingCount(Subscriber subscriber, String pk) {
        Observable observable = httpService.getMeetingCount(pk);
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传附件
     *
     * @param subscriber
     * @param pk
     * @param parts
     */
    public void saveFile(Subscriber subscriber, String pk, List<MultipartBody.Part> parts) {
        Observable observable = httpService.saveFile(pk, parts);
        toSubscribe(observable, subscriber);
    }
}
