package cn.invonate.ygoa3.Task;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.TaskAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.Entry.TaskEntry;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class TaskListActivity extends BaseActivity {

    @BindView(R.id.list_task)
    ExpandableListView listTask;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    YGApplication app;

    List<TaskEntry> list_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
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
        refresh.autoRefresh();
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
                Toast.makeText(TaskListActivity.this, "请求超时，请检查网络后重新尝试", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh();
            }

            @Override
            public void onNext(final Mission data) {
                Log.i("getMyTask", data.toString());
                if (data.getSuccess() == 0) {
                    list_task = exchange(data);
                    TaskAdapter taskAdapter = new TaskAdapter(list_task, TaskListActivity.this);
                    listTask.setAdapter(taskAdapter);
                    for (int i = 0; i < taskAdapter.getGroupCount(); i++) {
                        listTask.expandGroup(i);
                    }
                    listTask.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("businessId", list_task.get(groupPosition).getTasks().get(childPosition).getBusinessId());
                            bundle.putString("taskId", list_task.get(groupPosition).getTasks().get(childPosition).getId());
                            bundle.putString("workflowType", list_task.get(groupPosition).getTasks().get(childPosition).getWorkflowType());
                            bundle.putBoolean("need_layout", true);
                            bundle.putString("isXt", list_task.get(groupPosition).getTasks().get(childPosition).getIsXt());
                            stepActivity(bundle, TaskDetailActivity.class);
                            return true;
                        }
                    });
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                } else {
                    Toast.makeText(TaskListActivity.this, "获取失败，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this, false).getTask(subscriber, app.getUser().getSessionId());
    }

    private List<TaskEntry> exchange(Mission data) {
        List<TaskEntry> list_task = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Mission.MissionBean bean : data.getData()) {
            String lb = bean.getLb();
            if (!names.contains(lb)) {
                names.add(lb);
            }
        }
        for (String name : names) {
            TaskEntry task = new TaskEntry();
            task.setLb(name);
            List<Mission.MissionBean> tasks = new ArrayList<>();
            for (Mission.MissionBean bean : data.getData()) {
                if (bean.getLb().equals(name)) {
                    tasks.add(bean);
                }
            }
            task.setTasks(tasks);
            list_task.add(task);
        }
        Log.i("tasks", JSON.toJSONString(list_task));
        return list_task;
    }

}
