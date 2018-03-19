package cn.invonate.ygoa3;

import android.app.Application;

import cn.invonate.ygoa3.Entry.User;

/**
 * Created by liyangyang on 2017/10/20.
 */

public class YGApplication extends Application {
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
