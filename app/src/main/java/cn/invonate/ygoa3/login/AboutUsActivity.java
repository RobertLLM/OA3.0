package cn.invonate.ygoa3.login;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.info)
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        info.setText("当前版本：" + GetVersion(this));
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (Exception e) {
            return "";
        }
    }
}
