package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.AddMeeting;
import cn.invonate.ygoa3.Entry.MeetMessage;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.Entry.Room;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class AddMeetingActivity extends BaseActivity {

    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.person_in)
    TextView personIn;
    @BindView(R.id.person_note)
    TextView personNote;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.end_time)
    TextView endTime;

    Room.ResultBean.ListBean room = new Room.ResultBean.ListBean();

    private String address_id;

    private YGApplication app;

    private ArrayList<MeetingDetail.ResultBean.AttendListBean> list_attend = new ArrayList<>();
    private MeetingDetail.ResultBean.AttendListBean recorder;
    private Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        startDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        endDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        room.setStart_h(9);
        room.setStart_m(0);
        room.setEnd_s(0);
        room.setEnd_h(10);
        room.setEnd_m(0);
        room.setEnd_s(0);
        startTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getStart_h(), room.getStart_m(), room.getStart_s())));
        endTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getEnd_h(), room.getEnd_m(), room.getEnd_s())));
    }

    @OnClick({R.id.pic_back, R.id.layout_time, R.id.pic_search, R.id.layout_address, R.id.layout_in, R.id.layout_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.pic_search:
                addMeeting();
                break;
            case R.id.layout_time:
                stepToLocation();
                break;
            case R.id.layout_address:
                stepToLocation();
                break;
            case R.id.layout_in:
                stepToAttend();
                break;
            case R.id.layout_note:
                if (list_attend.isEmpty()) {
                    Toast.makeText(this, "请先选择参会人", Toast.LENGTH_SHORT).show();
                } else {
                    stepToRecord();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x999) {
            if (data != null) {
                list_attend = (ArrayList<MeetingDetail.ResultBean.AttendListBean>) data.getExtras().getSerializable("list");
                if (list_attend.isEmpty()) {
                    personIn.setText("请选择");
                } else {
                    personIn.setText(String.format("%s等%d人", list_attend.get(0).getUserName(), list_attend.size()));
                }
            }
        } else if (requestCode == 0x123 && resultCode == 0x321) {
            if (data != null) {
                recorder = new MeetingDetail.ResultBean.AttendListBean();
                recorder.setUserName(data.getExtras().getString("name"));
                recorder.setUserId(data.getExtras().getString("id"));
                recorder.setUserCode(data.getExtras().getString("code"));
                personNote.setText(recorder.getUserName());
            }
        } else if (requestCode == 0x123 && resultCode == 0x888) {
            if (data != null) {
                room = (Room.ResultBean.ListBean) data.getExtras().getSerializable("room");
                address_id = data.getExtras().getString("address_id");
                if (room.getRoomBuilding() != null && !room.getRoomBuilding().isEmpty()) {
                    if (room.getRoomFloor() != null && !room.getRoomFloor().isEmpty()) {
                        txtAddress.setText(String.format("%s楼%s层%s", room.getRoomBuilding(), room.getRoomFloor(), room.getRoomNo()));
                    } else {
                        txtAddress.setText(String.format("%s楼%s", room.getRoomBuilding(), room.getRoomNo()));
                    }
                } else {
                    if (room.getRoomFloor() != null && !room.getRoomFloor().isEmpty()) {
                        txtAddress.setText(String.format("%s层%s", room.getRoomFloor(), room.getRoomNo()));
                    } else {
                        txtAddress.setText(String.format("%s", room.getRoomNo()));
                    }
                }
                date = new Date(data.getExtras().getInt("date_y"), data.getExtras().getInt("date_m"), data.getExtras().getInt("date_d"));
                startDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                endDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                startTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getStart_h(), room.getStart_m(), room.getStart_s())));
                endTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getEnd_h(), room.getEnd_m(), room.getEnd_s())));
            }
        }
    }

    /**
     * 跳转到选择地点
     */
    private void stepToLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     * 跳转至选择参会人
     */
    private void stepToAttend() {
        if (list_attend.isEmpty()) {
            MeetingDetail.ResultBean.AttendListBean bean = new MeetingDetail.ResultBean.AttendListBean();
            bean.setUserCode(app.getUser().getUser_code());
            bean.setUserId(app.getUser().getRsbm_pk());
            bean.setUserName(app.getUser().getUser_name());
            list_attend.add(bean);
        }
        Intent intent = new Intent(this, AddAttendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list_attend);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     * 跳转至选择记录人
     */
    private void stepToRecord() {
        Intent intent = new Intent(this, ReportManActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list_attend);
        if (recorder == null) {
            bundle.putString("code", "");
        } else {
            bundle.putString("code", recorder.getUserCode());
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     *
     */
    private void addMeeting() {
        String t = title.getText().toString();
        if ("".equals(t)) {
            Toast.makeText(app, "请输入会议标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (room == null) {
            Toast.makeText(app, "请选择会议地点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recorder == null) {
            Toast.makeText(app, "请选择记录人", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list_attend == null || list_attend.isEmpty()) {
            Toast.makeText(app, "请选择参会人", Toast.LENGTH_SHORT).show();
            return;
        }
        AddMeeting meet = new AddMeeting();
        meet.setMeetingContent(t);
        Date start = new Date(date.getYear(), date.getMonth(), date.getDate(), room.getStart_h(), room.getStart_m(), room.getStart_s());
        Log.i("start_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start));
        meet.setStartTime(start.getTime() + "");
        Date end = new Date(date.getYear(), date.getMonth(), date.getDate(), room.getEnd_h(), room.getEnd_m(), room.getEnd_s());
        Log.i("end_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end));
        meet.setEndTime(end.getTime() + "");
        meet.setRoomId(room.getRoomId());
        meet.setRecordPersonId(recorder.getUserId());
        meet.setRecordPersonCode(recorder.getUserCode());
        meet.setRecordPersonName(recorder.getUserName());
        meet.setJoinList(list_attend);
        meet.setAddressId(address_id);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("addMeeting", data.toString());
                if ("0000".equals(data.getCode())) {
                    finish();
                    Toast.makeText(app, "预约成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).add_meet(new ProgressSubscriber(onNextListener, this, "预约中"), app.getUser().getRsbm_pk(), meet);
    }
}
