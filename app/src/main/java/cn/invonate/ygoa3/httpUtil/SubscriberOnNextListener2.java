package cn.invonate.ygoa3.httpUtil;

/**
 * Created by liyangyang on 17/4/10.
 */
public interface SubscriberOnNextListener2<T> {
    void onNext(T t);

    void onError(Throwable e);
}
