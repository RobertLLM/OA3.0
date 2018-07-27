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
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;

public class AddAttendActivity extends BaseActivity {

    @BindView(R.id.list_person)
    SwipeMenuListView listPerson;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private ArrayList<MeetingDetail.ResultBean.AttendListBean> list;

    YGApplication app;

    AttendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        list = (ArrayList<MeetingDetail.ResultBean.AttendListBean>) getIntent().getExtras().getSerializable("list");
        if (list != null) {
            adapter = new AttendAdapter(list, this);
            listPerson.setAdapter(adapter);
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem delete = new SwipeMenuItem(AddAttendActivity.this);
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
                    if (list.get(position).getUserCode().equals(app.getUser().getUser_code())) {
                        Toast.makeText(app, "无法删除会议创建人", Toast.LENGTH_SHORT).show();
                    } else {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
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
                Intent intent = new Intent(AddAttendActivity.this, SelectDepartmentActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.pic_sure:
                Intent intent2 = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent2.putExtras(bundle);
                setResult(0x999, intent2);
                finish();
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
                        list.add(bean);
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
        for (MeetingDetail.ResultBean.AttendListBean bean : list) {
            if (contacts.getUser_code().equals(bean.getUserCode()))
                return true;
        }
        return false;
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
