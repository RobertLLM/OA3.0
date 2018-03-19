package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Entry.InitPassMessage;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class ForgetPassActivity extends AppCompatActivity {

    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.edit_identify)
    EditText editIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pic_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.btn_submit:
                init_pass(editCode.getText().toString().trim(), editIdentify.getText().toString().trim());
                break;
        }
    }

    /**
     * 重置密码
     *
     * @param code
     * @param identify
     */
    private void init_pass(String code, String identify) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<InitPassMessage>() {
            @Override
            public void onNext(InitPassMessage data) {
                Log.i("init_pass", data.toString());
                if (data.getSuccess()==0){
                    AlertDialog dialog=new AlertDialog(ForgetPassActivity.this).builder();
                    dialog.setTitle("修改成功");
                    dialog.setMsg("密码初始化为A+身份证后6位");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }else{
                    AlertDialog dialog=new AlertDialog(ForgetPassActivity.this).builder();
                    dialog.setTitle("修改失败");
                    dialog.setMsg(data.getCause());
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            }
        };
        HttpUtil.getInstance(this,false).initPass(new ProgressSubscriber(onNextListener, this, "提交中"), code, identify);
    }

}
