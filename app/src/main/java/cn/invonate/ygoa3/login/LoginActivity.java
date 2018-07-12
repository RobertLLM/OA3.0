package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MD5;
import cn.invonate.ygoa3.Util.SpUtil;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import cn.invonate.ygoa3.main.MainActivity;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pass)
    EditText editPass;
    @BindView(R.id.check_pass)
    CheckBox checkPass;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        editName.setText(SpUtil.INSTANCE.getName(this));
        editPass.setText(SpUtil.INSTANCE.getPass(this));
    }

    @OnClick({R.id.btn_login, R.id.txt_forget_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login(editName.getText().toString().trim(), editPass.getText().toString().trim());
                break;
            case R.id.txt_forget_pass:
                stepActivity(ForgetPassActivity.class);
                break;
        }

    }

    /**
     * 登录
     *
     * @param userName 工号
     * @param password 密码
     */
    private void login(final String userName, final String password) {
        if ("".equals(userName)) {
            Toast.makeText(app, "工号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(password)) {
            Toast.makeText(app, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User data) {
                Log.i("login", data.toString());
                if (data.getSuccess() == 0) {
                    //登陆成功
                    app.setUser(data);
                    if (checkPass.isChecked()) {
                        //记住密码
                        SpUtil.INSTANCE.setName(LoginActivity.this, userName);
                        SpUtil.INSTANCE.setPass(LoginActivity.this, password);
                    }
                    goActivity(MainActivity.class);
                } else {
                    Toast.makeText(app, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this,true).login(new ProgressSubscriber(onNextListener, this, "登录中"), userName,
                MD5.Companion.GetMD5Code(password)
                //password
        );
    }
}
