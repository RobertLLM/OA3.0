package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.PropertyAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Property;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class PropertyActivity extends BaseActivity {

    @BindView(R.id.list_property)
    LYYPullToRefreshListView listProperty;

    YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        listProperty.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getProperty();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProperty();
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 获取资产列表
     */
    private void getProperty() {
        Subscriber subscriber = new Subscriber<Property>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final Property data) {
                Log.i("getProperty", data.toString());
                if (data.getSuccess() == 0) {
                    listProperty.setAdapter(new PropertyAdapter(data.getData(), PropertyActivity.this));
                    listProperty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", position - 1);
                            bundle.putSerializable("property", data.getData().get(position - 1));
                            stepActivity(bundle,PropertyDetailActivity.class);
                        }
                    });
                }
            }
        };
        HttpUtil.getInstance(this, false).getProperty(subscriber, app.getUser().getSessionId());
    }
}
