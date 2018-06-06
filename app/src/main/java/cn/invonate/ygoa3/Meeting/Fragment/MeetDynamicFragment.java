package cn.invonate.ygoa3.Meeting.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.DynamicAdapter;
import cn.invonate.ygoa3.Adapter.RepeatAdapter;
import cn.invonate.ygoa3.Entry.MeetRepeat;
import cn.invonate.ygoa3.Entry.MeetingDynamic;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import rx.Subscriber;

public class MeetDynamicFragment extends Fragment {

    @BindView(R.id.list_meet)
    SwipeMenuListView listMeet;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    Unbinder unbinder;
    private int index;
    private String id;

    YGApplication app;

    public static MeetDynamicFragment newInstance(int index, String id) {
        MeetDynamicFragment fragment = new MeetDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_meeting, container, false);
        app = (YGApplication) getActivity().getApplication();
        unbinder = ButterKnife.bind(this, view);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        refresh.autoRefresh();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        index = bundle.getInt("index", -1);
        id = bundle.getString("id", id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getData() {
        switch (index) {
            case 0:
                getDynamic();
                break;
            case 1:
                getRepeat();
                break;
        }
    }

    /**
     * 获取动态
     */
    private void getDynamic() {
        String url = "v1/oa/meetingDynamic/meetingDynamicRequest/" + id;
        Subscriber subscriber = new Subscriber<MeetingDynamic>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(MeetingDynamic data) {
                Log.i("getDynamic", data.toString());
                if ("0000".equals(data.getCode())) {
                    listMeet.setAdapter(new DynamicAdapter(data.getResult(), getActivity()));
                } else {
                    Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(getActivity(), false).getDynamic(subscriber, url, app.getUser().getRsbm_pk());

    }

    /**
     * 获取回复
     */
    private void getRepeat() {
        String url = "v1/oa/meeting/getReason/" + id;
        Subscriber subscriber = new Subscriber<MeetRepeat>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(MeetRepeat data) {
                Log.i("getDynamic", data.toString());
                if ("0000".equals(data.getCode())) {
                    listMeet.setAdapter(new RepeatAdapter(data.getResult(), getActivity()));
                } else {
                    Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(getActivity(), false).getRepeat(subscriber, url, app.getUser().getRsbm_pk());

    }
}
