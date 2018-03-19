package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;

public class WageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pic_back, R.id.layout_wage, R.id.layout_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_wage:
                stepActivity(SalaryActivity.class);
                break;
            case R.id.layout_gift:
                stepActivity(WelfareActivity.class);
                break;
        }
    }
}
