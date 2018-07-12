package cn.invonate.ygoa3.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.GlideUtil;
import cn.invonate.ygoa3.Util.SpUtil;

public class SettingActivity extends BaseActivity {

    private FinishBroadCast finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        finish = new FinishBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        registerReceiver(finish, intentFilter);
    }

    @OnClick({R.id.pic_back, R.id.layout_clean, R.id.layout_change_pass, R.id.layout_us, R.id.layout_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_clean:
                String size = GlideUtil.Companion.getInstance().getCacheSize(getApplicationContext());
                AlertDialog clear = new AlertDialog(this).builder();
                clear.setTitle("清除" + size + "缓存?")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlideUtil.Companion.getInstance().clearImageAllCache(getApplicationContext());
                        SpUtil.INSTANCE.removeAll(SettingActivity.this);
                    }
                }).show();
                break;
            case R.id.layout_change_pass:
                stepActivity(ChangePassActivity.class);
                break;
            case R.id.layout_us:
                stepActivity(AboutUsActivity.class);
                break;
            case R.id.layout_exit:
                AlertDialog dialog = new AlertDialog(this).builder();
                dialog.setTitle("确定退出当前账号？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpUtil.INSTANCE.setName(SettingActivity.this, "");
                        SpUtil.INSTANCE.setPass(SettingActivity.this, "");
                        Intent intent = new Intent();
                        intent.setAction("finish");
                        sendBroadcast(intent);
                        Intent login = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finish);
        super.onDestroy();
    }

    class FinishBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
