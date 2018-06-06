package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.EditMeeting;
import cn.invonate.ygoa3.Entry.MeetMessage;
import cn.invonate.ygoa3.Entry.Meeting;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.Entry.Reason;
import cn.invonate.ygoa3.Meeting.Fragment.MeetDynamicFragment;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.TimeUtil;
import cn.invonate.ygoa3.View.CustomViewPager;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class MeetDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.locate)
    TextView locate;
    @BindView(R.id.person)
    TextView person;
    @BindView(R.id.dynamic)
    TextView dynamic;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.person_in)
    TextView personIn;
    @BindView(R.id.in_sum)
    TextView inSum;
    @BindView(R.id.layout_in)
    LinearLayout layoutIn;
    @BindView(R.id.person_note)
    TextView personNote;
    @BindView(R.id.layout_note)
    LinearLayout layoutNote;
    @BindView(R.id.layout_sign)
    LinearLayout layoutSign;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    CustomViewPager vpContent;

    Meeting.ResultBean.MeetBean bean;

    YGApplication app;

    @BindView(R.id.txt_change)
    TextView txtChange;
    @BindView(R.id.layout_attend)
    LinearLayout layoutAttend;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;

    private MeetingDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_detail);
        app = (YGApplication) getApplication();
        ButterKnife.bind(this);
        bean = (Meeting.ResultBean.MeetBean) getIntent().getExtras().getSerializable("meet");
        // 初始化标题数组
        tabIndicators = new ArrayList<>();
        tabIndicators.add("全部");
        tabIndicators.add("回复");

        tabFragments = new ArrayList<>();
        for (int i = 0; i < tabIndicators.size(); i++) {
            tabFragments.add(MeetDynamicFragment.newInstance(i, bean.getId()));
        }
        vpContent.setOffscreenPageLimit(2);
        vpContent.setScanScroll(false);
        tlTab.setTabMode(TabLayout.MODE_FIXED);
        tlTab.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.colorPrimary));
        tlTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setElevation(tlTab, 10);
        tlTab.setupWithViewPager(vpContent);

        ContentPagerAdapter contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(contentAdapter);

        getMeetingDetail();//获取详情
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 根据会议状态  设置详细的显示
     */
    private void setStatus(MeetingDetail.ResultBean bean) {
        if ("0".equals(bean.getMeetingStatus()) && "0".equals(bean.getMeetingGoingStatus())) {// 判断会议未开始
            if (bean.getCreatorId().equals(app.getUser().getRsbm_pk())) {//判断是否自己创建
                // 自己是创建人，允许修改会议
                txtChange.setVisibility(View.VISIBLE);
                title.setEnabled(true);
            } else {
                // 不允许修改会议
                txtChange.setVisibility(View.INVISIBLE);
                title.setEnabled(false);
            }
            if (bean.getJoinStatus().equals("0")) {// 未选择参加不参加
                layoutAttend.setVisibility(View.VISIBLE);
            } else {
                layoutAttend.setVisibility(View.GONE);
            }
        } else {
            txtChange.setVisibility(View.INVISIBLE);
            layoutAttend.setVisibility(View.GONE);
        }
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    @OnClick({R.id.pic_back, R.id.txt_change, R.id.layout_in, R.id.layout_note, R.id.layout_sign, R.id.attend_sure, R.id.attend_not})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.txt_change:
                editMeet();
                break;
            case R.id.layout_in:
                if (detail != null)
                    stepToAttend();
                break;
            case R.id.layout_note:
                if (detail != null)
                    stepToReport();

                break;
            case R.id.layout_sign:

                break;
            case R.id.attend_sure:
                attend_sure(bean.getId());
                break;
            case R.id.attend_not:
                final EditText edit = new EditText(this);
                AlertDialog dialog = new AlertDialog(this).builder();
                dialog.setTitle("请输入理由");
                dialog.setView(edit);
                dialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attend_not(bean.getId(), edit.getText().toString().trim());
                    }
                }).show();
                break;
        }
    }

    /**
     * 跳转至参会人界面
     */
    private void stepToAttend() {
        Intent intent = new Intent(this, AttendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", detail.getResult());
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     * 跳转至记录人
     */
    private void stepToReport() {
        Intent intent = new Intent(this, ReportManActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", detail.getResult().getAttendList());
        bundle.putString("code", detail.getResult().getRecordPersonCode());
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     * 获取详情
     */
    private void getMeetingDetail() {
        String url = "v1/oa/meeting/selectMeetingById/" + bean.getId();
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetingDetail>() {
            @Override
            public void onNext(MeetingDetail data) {
                Log.i("getMeetingDetail", data.toString());
                detail = data;
                if ("0000".equals(data.getCode())) {
                    title.setText(data.getResult().getTitle());
                    time.setText(TimeUtil.timeFormatNoYear(data.getResult().getStartTime()) + " - " + TimeUtil.timeFormatJustMMHH(data.getResult().getEndTime()));
                    person.setText(data.getResult().getCreatorName());
                    locate.setText(data.getResult().getAddressName());
                    dynamic.setText(data.getResult().getDynamic() + "条动态");
                    sum.setText(data.getResult().getAttendNum() + "/" + data.getResult().getTotalNum() + "人确认参加");
                    if ("1".equals(data.getResult().getMeetingStatus())) {//取消
                        status.setText("取消");
                        status.setBackgroundResource(R.drawable.back_meet_grey);
                        status.setTextColor(Color.parseColor("#A4A4A4"));
                    } else if ("0".equals(data.getResult().getMeetingStatus())) {
                        String s = data.getResult().getMeetingGoingStatus();
                        if (s != null) {
                            switch (s) {
                                case "0":
                                    status.setText("未进行");
                                    status.setBackgroundResource(R.drawable.back_meet_grey);
                                    status.setTextColor(Color.parseColor("#A4A4A4"));
                                    break;
                                case "1":
                                    status.setText("已结束");
                                    status.setBackgroundResource(R.drawable.back_meet_red);
                                    status.setTextColor(Color.parseColor("#FF0000"));
                                    break;
                                case "2":
                                    status.setText("进行中");
                                    status.setBackgroundResource(R.drawable.back_meet_blue);
                                    status.setTextColor(Color.parseColor("#0099FF"));
                                    break;
                                default:
                                    status.setVisibility(View.GONE);
                                    break;
                            }
                        } else {
                            status.setVisibility(View.GONE);
                        }
                    } else {
                        status.setVisibility(View.GONE);
                    }
                    // 设置参会人
                    inSum.setText(data.getResult().getAttendNum() + "/" + data.getResult().getTotalNum() + "人参加");
                    personIn.setText(String.format("%s等%d人", data.getResult().getAttendList().get(0).getUserName(), data.getResult().getAttendList().size()));
                    personNote.setText(data.getResult().getRecordPersonName());
                    setStatus(data.getResult());
                } else {
                    Toast.makeText(app, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).getMeetingDetail(new ProgressSubscriber(onNextListener, this), url, app.getUser().getRsbm_pk());
    }


    /**
     * 确认参加
     *
     * @param id
     */
    private void attend_sure(String id) {
        String url = "v1/oa/meetingJoin/attendMeeting/" + id;
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("attend_sure", data.toString());
                if ("0000".equals(data.getCode())) {
                    layoutAttend.setVisibility(View.GONE);
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).attend_sure(new ProgressSubscriber(onNextListener, this), url, app.getUser().getRsbm_pk());
    }

    /**
     * 不参加
     *
     * @param id
     */
    private void attend_not(String id, String reason) {
        Reason r = new Reason();
        r.setReason(reason);
        Log.i("reason", JSON.toJSONString(r));
        String url = "v1/oa/meetingJoin/noAttendMeeting/" + id;
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("attend_not", data.toString());
                if ("0000".equals(data.getCode())) {
                    layoutAttend.setVisibility(View.GONE);
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).attend_not(new ProgressSubscriber(onNextListener, this), url, app.getUser().getRsbm_pk(), r);
    }


    /**
     * 编辑会议
     */
    private void editMeet() {
        EditMeeting meet = new EditMeeting();
        meet.setId(detail.getResult().getId());
        meet.setMeetingContent(title.getText().toString().trim());
        meet.setRecordPersonCode(detail.getResult().getRecordPersonCode());
        meet.setRecordPersonId(detail.getResult().getRecordPersonId());
        meet.setRecordPersonName(detail.getResult().getRecordPersonName());
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("editMeet", data);
            }
        };
        HttpUtil2.getInstance(this, false).edit_meet(new ProgressSubscriber(onNextListener, this, "修改中"), app.getUser().getRsbm_pk(), meet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x321) {
            if (data != null) {
                detail.getResult().setRecordPersonCode(data.getExtras().getString("code"));
                detail.getResult().setRecordPersonId(data.getExtras().getString("id"));
                detail.getResult().setRecordPersonName(data.getExtras().getString("name"));
                personNote.setText(detail.getResult().getRecordPersonName());
            }
        } else if (resultCode == 0x999) {
            getMeetingDetail();
        }
    }
}
