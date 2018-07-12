package cn.invonate.ygoa3.Meeting;

import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.ZXingUtils;

public class ShowCodeActivity extends BaseActivity {

    @BindView(R.id.code)
    ImageView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_code);
        ButterKnife.bind(this);
        String id = getIntent().getExtras().getString("id");
        code.setImageBitmap(ZXingUtils.createQRImage("v1/oa/meetingDynamic/sign/" + id, 500, 500));
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }
}
