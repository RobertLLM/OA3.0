package cn.invonate.ygoa3.Task;

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
import cn.invonate.ygoa3.Adapter.TaskAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class TaskListActivity extends BaseActivity {

    @BindView(R.id.list_task)
    LYYPullToRefreshListView listTask;

    YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        listTask.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getMyTask();
            }
        });
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyTask();
    }

    /**
     *
     */
    private void getMyTask() {
        Subscriber subscriber = new Subscriber<Mission>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listTask.onRefreshComplete();
            }

            @Override
            public void onNext(final Mission data) {
                Log.i("getMyTask", data.toString());
                if (data.getSuccess()==0){
                    listTask.setAdapter(new TaskAdapter(data.getData(), TaskListActivity.this));
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("businessId", data.getData().get(position - 1).getBusinessId());
                            bundle.putString("taskId", data.getData().get(position - 1).getId());
                            bundle.putString("workflowType", data.getData().get(position - 1).getWorkflowType());
                            bundle.putBoolean("need_layout", true);
                            bundle.putBoolean("isXt", data.getData().get(position - 1).isXt());
                            stepActivity(bundle, TaskDetailActivity.class);
                        }
                    });
                    listTask.onRefreshComplete();
                } else {
                    Toast.makeText(TaskListActivity.this, "获取失败，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this,false).getTask(subscriber, app.getUser().getSessionId());
    }
}
