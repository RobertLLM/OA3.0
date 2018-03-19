package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MD5;
import cn.invonate.ygoa3.Util.SpUtil;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber2;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener2;
import cn.invonate.ygoa3.main.MainActivity;

public class InitActivity extends BaseActivity {

    private YGApplication app;

    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        app = (YGApplication) getApplication();
        userName = SpUtil.getName(this);
        password = SpUtil.getPass(this);
        login();
    }

    private void login() {
        if ("".equals(userName) || "".equals(password)) {
            stepActivity(LoginActivity.class);
        } else {
            SubscriberOnNextListener2 onNextListener = new SubscriberOnNextListener2<User>() {
                @Override
                public void onNext(User data) {
                    Log.i("login", data.toString());
                    if (data.getSuccess() == 0) {
                        //登陆成功
                        app.setUser(data);
                        //记住密码
                        SpUtil.setName(InitActivity.this, userName);
                        SpUtil.setPass(InitActivity.this, password);
                        goActivity(MainActivity.class);
                    } else {
                        Toast.makeText(app, data.getMsg(), Toast.LENGTH_SHORT).show();
                        stepActivity(LoginActivity.class);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("login", e.toString());
                    Toast.makeText(app, "自动登录失败，请重新尝试", Toast.LENGTH_SHORT).show();
                    stepActivity(LoginActivity.class);
                }
            };

            HttpUtil.getInstance(this, true).login(new ProgressSubscriber2(onNextListener, this, "登录中"), userName,
                    MD5.GetMD5Code(password)
                    //password
            );
        }
    }
}
