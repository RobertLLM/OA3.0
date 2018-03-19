package cn.invonate.ygoa3.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.ChangePass;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MD5;
import cn.invonate.ygoa3.Util.SpUtil;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class ChangePassActivity extends BaseActivity {

    @BindView(R.id.pass_old)
    EditText passOld;
    @BindView(R.id.pass_new)
    EditText passNew;
    @BindView(R.id.pass_sure)
    EditText passSure;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
    }

    @OnClick({R.id.pic_back, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.btn_sure:
                changePass(passOld.getText().toString().trim(),
                        passNew.getText().toString().trim(),
                        passSure.getText().toString().trim());
                break;
        }
    }

    /**
     * 修改密码
     *
     * @param pass_old
     * @param pass_new
     * @param pass_sure
     */
    private void changePass(String pass_old, String pass_new, String pass_sure) {
        if (!pass_new.equals(pass_sure)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<ChangePass>() {
            @Override
            public void onNext(ChangePass data) {
                Log.i("changePass", data.toString());
                Toast.makeText(app, data.getCause(), Toast.LENGTH_SHORT).show();
                if (data.getSuccess()==0){
                    SpUtil.setName(ChangePassActivity.this, "");
                    SpUtil.setPass(ChangePassActivity.this, "");
                    Intent intent = new Intent();
                    intent.setAction("finish");
                    sendBroadcast(intent);
                    Intent login = new Intent(ChangePassActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        };
        HttpUtil.getInstance(this, false).changePass(new ProgressSubscriber(onNextListener, this, "修改中"), MD5.GetMD5Code(pass_old), MD5.GetMD5Code(pass_new), app.getUser().getUser_code());
    }

}
