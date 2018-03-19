package cn.invonate.ygoa3.Task.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.ApprovedAdapter;
import cn.invonate.ygoa3.Entry.Approved;
import cn.invonate.ygoa3.Entry.TaskCopy;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Task.TaskDetailActivity;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/1/30.
 */

public class TaskUnCopyFragment extends Fragment {
    @BindView(R.id.list_task)
    LYYPullToRefreshListView listTask;
    Unbinder unbinder;

    YGApplication app;

    private List<Approved.ApprovedBean> list_data;
    private ApprovedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_task_uncopy, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        getCopyTask();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCopyTask() {
        Subscriber subscriber = new Subscriber<TaskCopy>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final TaskCopy data) {
                Log.i("getCopyTask", data.toString());
                if (data.getSuccess() == 0) {
                    list_data = data.getDataUn();
                    adapter = new ApprovedAdapter(list_data, getActivity());
                    listTask.setAdapter(adapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("businessId", data.getData().get(position - 1).getBusinessId());
                            bundle.putString("taskId", data.getData().get(position - 1).getId());
                            bundle.putString("workflowType", data.getData().get(position - 1).getWorkflowType());
                            bundle.putBoolean("need_layout", false);
                            bundle.putBoolean("isXt", data.getData().get(position - 1).isXt());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    listTask.onRefreshComplete();
                } else {
                    Toast.makeText(getActivity(), "获取失败，请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(getActivity(),false).getCopyTask(subscriber, app.getUser().getSessionId());
    }
}
