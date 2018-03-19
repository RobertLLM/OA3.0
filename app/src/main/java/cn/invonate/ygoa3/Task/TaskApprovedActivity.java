package cn.invonate.ygoa3.Task;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.ApprovedAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Approved;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class TaskApprovedActivity extends BaseActivity {

    @BindView(R.id.list_task)
    LYYPullToRefreshListView listTask;

    private YGApplication app;

    private List<Approved.ApprovedBean> list_data;
    private ApprovedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_approved);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        getApproved(1);
        listTask.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getApproved(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getApproved(adapter.getCount() / 20 + 1);
            }
        });
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    private void getApproved(final int page) {
        Subscriber subscriber = new Subscriber<Approved>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(app,e.toString(),Toast.LENGTH_SHORT).show();
                listTask.onRefreshComplete();
            }

            @Override
            public void onNext(final Approved data) {
                Log.i("getApproved", data.toString());
                if (data.getSuccess() == 0) {
                    if (page == 1) {
                        list_data = data.getData();
                        adapter = new ApprovedAdapter(list_data, TaskApprovedActivity.this);
                        listTask.setAdapter(adapter);
                        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle bundle = new Bundle();
                                bundle.putString("businessId", data.getData().get(position - 1).getBusinessId());
                                bundle.putString("taskId", data.getData().get(position - 1).getId());
                                bundle.putString("workflowType", data.getData().get(position - 1).getWorkflowType());
                                bundle.putBoolean("need_layout", false);
                                bundle.putBoolean("isXt", data.getData().get(position - 1).isXt());
                                stepActivity(bundle, TaskDetailActivity.class);
                            }
                        });
                    } else {
                        list_data.addAll(data.getData());
                        adapter.notifyDataSetChanged();
                    }
                    if (data.getData().isEmpty()) {
                        listTask.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listTask.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    listTask.onRefreshComplete();
                }else{
                    Toast.makeText(TaskApprovedActivity.this, "获取失败，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this,false).getApproved(subscriber, app.getUser().getSessionId(), page);
    }
}
