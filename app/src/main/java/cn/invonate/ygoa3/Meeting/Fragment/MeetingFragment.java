package cn.invonate.ygoa3.Meeting.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.MeetAdapter;
import cn.invonate.ygoa3.Entry.MeetMessage;
import cn.invonate.ygoa3.Entry.MeetResponse;
import cn.invonate.ygoa3.Entry.Meeting;
import cn.invonate.ygoa3.Entry.Reason;
import cn.invonate.ygoa3.Meeting.MeetDetailActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import rx.Subscriber;

public class MeetingFragment extends Fragment {
    @BindView(R.id.list_meet)
    SwipeMenuListView listMeet;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    Unbinder unbinder;
    private int index;
    private YGApplication app;

    MeetAdapter adapter;

    ArrayList<Meeting.ResultBean.MeetBean> list_meet;

    private boolean isLastPage;

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
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!isLastPage) {
                    getData(list_meet.size() / 10 + 1);
                } else {
                    refresh.finishLoadMore();
                }
            }
        });
        refresh.autoRefresh();
        listMeet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MeetDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("meet", list_meet.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
     * 获取未结束的会议
     */
    private void getUnfinishMetting(final int page) {
        Subscriber subscriber = new Subscriber<Meeting>() {
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
            public void onNext(final Meeting data) {
                Log.i("getUnfinishMetting", data.toString());
                if ("0000".equals(data.getCode())) {
                    isLastPage = data.getResult().isIsLastPage();
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
                adapter.setOnAttendClickListener(new MeetAdapter.OnAttendClickListener() {
                    @Override
                    public void onAttend(View view, int position) {
                        attend_sure(position, list_meet.get(position).getId());
                    }

                    @Override
                    public void onNotAttend(View view, final int position) {
                        final EditText edit = new EditText(getActivity());
                        AlertDialog dialog = new AlertDialog(getActivity()).builder();
                        dialog.setTitle("请输入理由");
                        dialog.setView(edit);
                        dialog.setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                attend_not(position, list_meet.get(position).getId(), edit.getText().toString().trim());
                            }
                        }).show();
                    }
                });
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
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(Meeting data) {
                Log.i("getAllMetting", data.toString());
                if ("0000".equals(data.getCode())) {
                    isLastPage = data.getResult().isIsLastPage();
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
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
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onNext(Meeting data) {
                Log.i("getMyMeeting", data.toString());
                if ("0000".equals(data.getCode())) {
                    isLastPage = data.getResult().isIsLastPage();
                    if (page == 1) {
                        list_meet = data.getResult().getList();
                        adapter = new MeetAdapter(list_meet, getActivity());
                        listMeet.setAdapter(adapter);
                    } else {
                        list_meet.addAll(data.getResult().getList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        SwipeMenuItem delete = new SwipeMenuItem(getActivity());
                        delete.setWidth(dp2px(90));
                        delete.setTitleSize(18);
                        delete.setTitleColor(Color.WHITE);
                        delete.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        delete.setTitle("取消会议");
                        menu.addMenuItem(delete);
                    }
                };
                listMeet.setMenuCreator(creator);
                listMeet.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        delete_meet(position);
                        return false;
                    }
                });
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

    /**
     * 删除会议
     *
     * @param position
     */
    private void delete_meet(final int position) {
        String url = String.format("v1/oa/meeting/deleteMeetingById/%s", list_meet.get(position).getId());
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetResponse>() {
            @Override
            public void onNext(MeetResponse data) {
                Log.i("delete_meet", data.toString());
                if (data.getCode().equals("0000")) {
                    list_meet.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(app, "取消成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(getActivity(), false).deleteMeet(new ProgressSubscriber(onNextListener, getActivity(), "删除中"), url, app.getUser().getRsbm_pk());
    }

    // 将dp转换为px
    private int dp2px(int value) {
        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）
        // 第二个参数为我们待转的数据的值的大小
        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    /**
     * 确认参加
     *
     * @param position
     * @param id
     */
    private void attend_sure(final int position, String id) {
        String url = "v1/oa/meetingJoin/attendMeeting/" + id;
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("attend_sure", data.toString());
                if ("0000".equals(data.getCode())) {
                    list_meet.get(position).setJoinStatus("1");
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(getActivity(), false).attend_sure(new ProgressSubscriber(onNextListener, getActivity()), url, app.getUser().getRsbm_pk());
    }

    /**
     * 不参加
     *
     * @param position
     * @param id
     */
    private void attend_not(final int position, String id, String reason) {
        Reason r = new Reason();
        r.setReason(reason);
        Log.i("reason", JSON.toJSONString(r));
        String url = "v1/oa/meetingJoin/noAttendMeeting/" + id;
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("attend_not", data.toString());
                if ("0000".equals(data.getCode())) {
                    list_meet.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(getActivity(), false).attend_not(new ProgressSubscriber(onNextListener, getActivity()), url, app.getUser().getRsbm_pk(), r);
    }
}
