package cn.invonate.ygoa3.Meeting.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.MeetAdapter;
import cn.invonate.ygoa3.Entry.Meeting;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import rx.Subscriber;

public class MeetingFragment extends Fragment {
    @BindView(R.id.list_meet)
    ListView listMeet;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    Unbinder unbinder;
    private int index;
    private YGApplication app;

    MeetAdapter adapter;

    List<Meeting.ResultBean.MeetBean> list_meet;

    public static MeetingFragment newInstance(int index) {
        MeetingFragment fragment = new MeetingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_meeting, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData(1);
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
    }

    private void getData(int page) {
        switch (index) {
            case 0:
                getUnfinishMetting(page);
                break;
            case 1:
                getAllMetting(page);
                break;
            case 2:
                getMyMetting(page);
                break;
        }
    }

    /**
     * 获取为结束的会议
     */
    private void getUnfinishMetting(final int page) {
        Subscriber subscriber = new Subscriber<Meeting>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(Meeting data) {
                Log.i("getUnfinishMetting", data.toString());
                if ("0000".equals(data.getCode())) {
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    listMeet.setAdapter(null);
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(getActivity(), false).getUnfinishMeeting(subscriber, app.getUser().getRsbm_pk(), page, 10);
    }

    /**
     * 获取所有的会议
     */
    private void getAllMetting(final int page) {
        Subscriber subscriber = new Subscriber<Meeting>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(Meeting data) {
                Log.i("getAllMetting", data.toString());
                if ("0000".equals(data.getCode())) {
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    listMeet.setAdapter(null);
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(getActivity(), false).getAllMeeting(subscriber, app.getUser().getRsbm_pk(), page, 10);
    }

    /**
     * 获取我的的会议
     */
    private void getMyMetting(final int page) {
        Subscriber subscriber = new Subscriber<Meeting>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(Meeting data) {
                Log.i("getMyMetting", data.toString());
                if ("0000".equals(data.getCode())) {
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    listMeet.setAdapter(null);
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(getActivity(), false).getMyMeeting(subscriber, app.getUser().getRsbm_pk(), page, 10);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
