package cn.invonate.ygoa3;

import android.app.Application;
import android.content.Context;

import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.Util.KLog;

/**
 * Created by liyangyang on 2017/10/20.
 */

public class YGApplication extends Application {
    private User user;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true);
        context=this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Context getContext() {
        return context;
    }
}
