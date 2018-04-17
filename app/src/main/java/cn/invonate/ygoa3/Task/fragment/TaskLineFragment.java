package cn.invonate.ygoa3.Task.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.LineAdapter;
import cn.invonate.ygoa3.Entry.TaskLine;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskLineFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {

    Unbinder unbinder;
    @BindView(R.id.list_line)
    LYYPullToRefreshListView listLine;
    private String businessId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessId = getArguments().getString("businessId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_task_line, container, false);
        unbinder = ButterKnife.bind(this, view);
        getTaskLine();
        listLine.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getTaskLine();
            }
        });
        return view;
    }

    /**
     * 获取审批流程
     */
    private void getTaskLine() {
        Subscriber subscriber = new Subscriber<TaskLine>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                if (listLine != null) {
                    listLine.onRefreshComplete();
                }
            }

            @Override
            public void onNext(TaskLine data) {
                Log.i("getTaskLine", data.toString());
                LineAdapter adapter = new LineAdapter(data.getData(), getActivity());
                listLine.setAdapter(adapter);
                listLine.onRefreshComplete();
            }
        };
        HttpUtil.getInstance(getActivity(), false).getTaskLine(subscriber, businessId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
