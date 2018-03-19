package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.WelfareAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Welfare;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class WelfareActivity extends BaseActivity {

    @BindView(R.id.list_welfare)
    LYYPullToRefreshListView listWelfare;

    YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        getWelfare();
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 获取福利
     */
    private void getWelfare() {
        Subscriber subscriber = new Subscriber<Welfare>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Welfare data) {
                Log.i("getWelfare", data.toString());
                if (data.getSuccess() == 0) {
                    listWelfare.setAdapter(new WelfareAdapter(data.getData(), WelfareActivity.this));
                }
            }
        };
        HttpUtil.getInstance(this, false).getWelfare(subscriber, app.getUser().getSessionId());
    }
}
