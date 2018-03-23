package cn.invonate.ygoa3.login.group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.GroupAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.PersonGroup;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class GroupActivity extends BaseActivity {

    @BindView(R.id.list_group)
    LYYPullToRefreshListView listGroup;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        listGroup.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getGroup();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroup();
    }

    @OnClick({R.id.pic_back, R.id.edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.edit:
                stepActivity(AddGroupActivity.class);
                break;
        }
    }

    /**
     * 获取群组
     */
    private void getGroup() {
        Subscriber subscriber = new Subscriber<PersonGroup>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listGroup.onRefreshComplete();
            }

            @Override
            public void onNext(final PersonGroup data) {
                Log.i("getGroup", data.toString());
                if (data.getSuccess() == 0) {
                    listGroup.setAdapter(new GroupAdapter(data.getData(), app));
                    listGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(GroupActivity.this, GroupDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("group", data.getData().get(position - 1));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(app, "获取失败，请重新登录", Toast.LENGTH_SHORT).show();
                }
                listGroup.onRefreshComplete();
            }
        };
        HttpUtil.getInstance(this, false).getGroup(subscriber, app.getUser().getSessionId());
    }
}
