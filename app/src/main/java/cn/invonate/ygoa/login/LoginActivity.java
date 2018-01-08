package cn.invonate.ygoa.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.User;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.SpUtil;
import cn.invonate.ygoa.YGApplication;
import cn.invonate.ygoa.httpUtil.HttpUtil;
import cn.invonate.ygoa.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa.httpUtil.SubscriberOnNextListener;
import cn.invonate.ygoa.main.MainActivity;

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
        app= (YGApplication) getApplication();
        editName.setText(SpUtil.getName(this));
        editPass.setText(SpUtil.getPass(this));
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        //goActivity(MainActivity.class);
//        View view = LayoutInflater.from(this).inflate(R.layout.item_loading, null);
//        //LoadingDialog item=view.findViewById(R.id.load);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setView(view)
//                //.setTitle("111111")
//                .create();
//        dialog.show();
        login(editName.getText().toString().trim(), editPass.getText().toString().trim());
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
                        SpUtil.setName(LoginActivity.this,userName);
                        SpUtil.setPass(LoginActivity.this,password);
                    }
                    goActivity(MainActivity.class);
                }else{
                    Toast.makeText(app, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance().login(new ProgressSubscriber(onNextListener, this, "登录中"), userName, password);
    }
}
