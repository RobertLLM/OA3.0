package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.LocationAdapter;
import cn.invonate.ygoa3.Adapter.RoomAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.MeetingLocation;
import cn.invonate.ygoa3.Entry.Room;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import rx.Subscriber;

public class LocationActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;

    YGApplication app;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.list_location)
    ListView listLocation;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private CalendarDay select_date;

    private String districtId;

    private RoomAdapter adapter;

    private ArrayList<Room.ResultBean.ListBean> list_room;

    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        calendarView.state().edit()
                .setMinimumDate(new Date())
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                txtDate.setText(String.format("%d-%d-%d", date.getYear(), date.getMonth() + 1, date.getDay()));
                select_date = date;
                refresh.autoRefresh();
            }
        });
        calendarView.setTopbarVisible(false);
        calendarView.setCurrentDate(new Date());
        calendarView.setDateSelected(new Date(), true);
        select_date = calendarView.getSelectedDate();
        txtDate.setText(String.format("%d-%d-%d", calendarView.getSelectedDate().getYear(), calendarView.getSelectedDate().getMonth() + 1, calendarView.getSelectedDate().getDay()));
        getLocation();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getRoom(1);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (list_room.size() < total) {
                    getRoom(list_room.size() / 10 + 1);
                }
            }
        });
    }

    @OnClick({R.id.pic_back, R.id.txt_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.txt_change:
                if (adapter == null || adapter.getIndex() == -1) {
                    Toast.makeText(app, "请选择一个会议地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("room", list_room.get(adapter.getIndex()));
                bundle.putInt("date_y", select_date.getYear() - 1900);
                bundle.putInt("date_m", select_date.getMonth());
                bundle.putInt("date_d", select_date.getDay());
                bundle.putString("address_id", districtId);
                intent.putExtras(bundle);
                setResult(0x888, intent);
                finish();
                break;
        }
    }

    /**
     * 获取地点
     */
    private void getLocation() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetingLocation>() {
            @Override
            public void onNext(MeetingLocation data) {
                Log.i("getLocation", data.toString());
                if ("0000".equals(data.getCode())) {
                    if (!data.getResult().isEmpty()) {
                        initPop(data.getResult());
                    } else {
                        Toast.makeText(app, "暂无可用会议地点", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).getLocation(new ProgressSubscriber(onNextListener, this), app.getUser().getRsbm_pk());
    }

    /**
     * 初始化下拉pop
     *
     * @param list_location
     */
    private void initPop(final List<MeetingLocation.ResultBean> list_location) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_pop_meet, null, false);
        final PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ListView list = contentView.findViewById(R.id.list);
        list.setAdapter(new LocationAdapter(list_location, this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                districtId = list_location.get(position).getRowGuid();
                name.setText(list_location.get(position).getDistrictName());
                refresh.autoRefresh();
                window.dismiss();
            }
        });
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        name.setText(list_location.get(0).getDistrictName());
        districtId = list_location.get(0).getRowGuid();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.showAsDropDown(v, 0, 0, Gravity.CENTER);
            }
        });
        refresh.autoRefresh();
    }

    /**
     * 获取会议室
     *
     * @param page
     */
    private void getRoom(final int page) {
        Date date = new Date(select_date.getYear(), select_date.getMonth(), select_date.getDay());

        Log.i("select_date", date.getTime() + "");
        Subscriber subscriber = new Subscriber<Room>() {
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
            public void onNext(Room data) {
                Log.i("getRoom", data.toString());
                total = data.getResult().getTotal();
                if (page == 1) {
                    list_room = data.getResult().getList();
                    adapter = new RoomAdapter(list_room, LocationActivity.this);
                    listLocation.setAdapter(adapter);
                } else {
                    list_room.addAll(data.getResult().getList());
                    adapter.notifyDataSetChanged();
                }
                listLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.setIndex(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil2.getInstance(this, false).getRoom(subscriber, app.getUser().getRsbm_pk(), page, 10, date.getTime() + "", districtId);
    }
}
