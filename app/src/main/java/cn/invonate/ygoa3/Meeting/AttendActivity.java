package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.AttendAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.DeletePerson;
import cn.invonate.ygoa3.Entry.MeetMessage;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class AttendActivity extends BaseActivity {

    @BindView(R.id.list_person)
    SwipeMenuListView listPerson;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private MeetingDetail.ResultBean data;

    YGApplication app;

    AttendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        data = (MeetingDetail.ResultBean) getIntent().getExtras().getSerializable("bean");
        if (data != null) {
            adapter = new AttendAdapter(data.getAttendList(), this);
            listPerson.setAdapter(adapter);
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem delete = new SwipeMenuItem(AttendActivity.this);
                    delete.setWidth(dp2px(90));
                    delete.setTitleSize(18);
                    delete.setTitleColor(Color.WHITE);
                    delete.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    delete.setTitle("删除");
                    menu.addMenuItem(delete);
                }
            };
            listPerson.setMenuCreator(creator);
            listPerson.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    delete_join(position, data.getAttendList().get(position).getUserId());
                    return false;
                }
            });
        }

    }

    @OnClick({R.id.pic_back, R.id.fab, R.id.pic_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.fab:
                Intent intent = new Intent(AttendActivity.this, SelectDepartmentActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.pic_sure:
                attend_join();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x321) {
            if (data != null) {
                ArrayList<Contacts> select = (ArrayList<Contacts>) data.getExtras().getSerializable("list");
                Log.i("select", JSON.toJSONString(select));
                for (Contacts c : select) {
                    if (!check(c)) {
                        MeetingDetail.ResultBean.AttendListBean bean = new MeetingDetail.ResultBean.AttendListBean();
                        bean.setUserCode(c.getUser_code());
                        bean.setUserId(c.getRsbm_pk());
                        bean.setUserName(c.getUser_name());
                        this.data.getAttendList().add(bean);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 查重
     *
     * @return
     */
    private boolean check(Contacts contacts) {
        for (MeetingDetail.ResultBean.AttendListBean bean : data.getAttendList()) {
            if (contacts.getUser_code().equals(bean.getUserCode()))
                return true;
        }
        return false;
    }

    /**
     * 修改参会人
     */
    private void attend_join() {
        String url = "v1/oa/meetingJoin/addJoins/" + data.getId();
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("attend_join", data.toString());
                if ("0000".equals(data.getCode())) {
                    setResult(0x999);
                    finish();
                } else {
                    Toast.makeText(AttendActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).attend_join(new ProgressSubscriber(onNextListener, this, "修改中"), url, app.getUser().getRsbm_pk(), data.getAttendList());
    }

    /**
     * 删除参会人
     */
    private void delete_join(final int position, String id) {
        DeletePerson person = new DeletePerson();
        person.setMeetingId(data.getId());
        person.setUserId(id);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("delete_join", data.toString());
                if ("0000".equals(data.getCode())) {
                    AttendActivity.this.data.getAttendList().remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AttendActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).delete_join(new ProgressSubscriber(onNextListener, this, "删除中"), app.getUser().getRsbm_pk(), person);
    }

    // 将dp转换为px
    private int dp2px(int value) {
        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）
        // 第二个参数为我们待转的数据的值的大小
        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }
}
