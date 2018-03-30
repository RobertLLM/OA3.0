package cn.invonate.ygoa3.Contacts.Select;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.DepartmentAdapter;
import cn.invonate.ygoa3.Adapter.MemberAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.Department;
import cn.invonate.ygoa3.Entry.Member;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * 用于抄送选人
 */
public class SelectDepartment2Activity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.list_connect)
    LYYPullToRefreshListView listConnect;
    @BindView(R.id.ccl)
    AutoCompleteTextView act_ccl;
    @BindView(R.id.copy)
    ImageView copy;
    @BindView(R.id.task_sum)
    TextView taskSum;

    private DepartmentAdapter adapter;

    private ArrayList<Contacts> list_contacts = new ArrayList<>();

    public SlidingMenu menu;

    private SwipeMenuListView list_contact;
    private TextView layout_complete;

    private MemberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_department2);
        ButterKnife.bind(this);
        list_contacts = (ArrayList<Contacts>) getIntent().getExtras().getSerializable("list");
        slidingmune();
        taskSum.setText(list_contacts + "");
        name.setText("通讯录");
        getDepartment("0", "");

        act_ccl.addTextChangedListener(new TextWatch(act_ccl));
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        mAdapter = new MemberAdapter(list_contacts, this);
        list_contact.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建“删除”项
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(160);
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.parseColor("#FFFFFF"));
                deleteItem.setTitleSize(18);
                // 将创建的菜单项添加进菜单中
                menu.addMenuItem(deleteItem);
            }
        };


        list_contact.setMenuCreator(creator);

        list_contact.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        list_contacts.remove(position);
                        mAdapter.notifyDataSetChanged();
                        taskSum.setText(list_contacts.size() + "");
                        break;
                }
                return true;
            }
        });

        taskSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int sum = Integer.parseInt(taskSum.getText().toString().trim());
                if (sum == 0) {
                    taskSum.setVisibility(View.GONE);
                } else {
                    taskSum.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layout_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list_contacts);
                intent.putExtras(bundle);
                setResult(0x321, intent);
                finish();
            }
        });

    }

    /**
     * 初始化menu
     */
    private void slidingmune() {
        menu = new SlidingMenu(this);
        menu.setSlidingEnabled(false);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        menu.setBehindWidth(width);// 设置SlidingMenu菜单的宽度
        menu.setFadeDegree(0.35f);
        LayoutInflater.from(this).inflate(R.layout.menu_depart, null);
        menu.setMenu(R.layout.menu_depart);
        menu.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        layout_complete = findViewById(R.id.layout_complete);
        list_contact = findViewById(R.id.list_contact);
    }


    /**
     * 通讯录查询部门
     */
    private void getDepartment(final String id, final String name) {
        Subscriber subscriber = new Subscriber<ArrayList<Department>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listConnect.onRefreshComplete();
            }

            @Override
            public void onNext(final ArrayList<Department> data) {
                Log.i("getDepartment", data.toString());
                if ("0".equals(id)) {
                    adapter = new DepartmentAdapter(data, SelectDepartment2Activity.this);
                    listConnect.setAdapter(adapter);
                    listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            getDepartment(data.get(position - 1).getId_(), data.get(position - 1).getDepartment_name());
                        }
                    });
                    listConnect.onRefreshComplete();
                } else {
                    if (data.isEmpty()) {// 为空  跳转至成员列表界面
                        Intent intent = new Intent(SelectDepartment2Activity.this, SelectContactsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("name", name);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 0x123);
                    } else {// 还有子部门  跳转至部门界面
                        Intent intent = new Intent(SelectDepartment2Activity.this, SelectDepartmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putSerializable("list", data);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 0x123);
                    }
                }
            }
        };
        HttpUtil.getInstance(this, false).getDepartment(subscriber, id);
    }

    private boolean check(Contacts contacts) {
        for (Contacts c : list_contacts) {
            if (c.getUser_code().equals(contacts.getUser_code())) {
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("SelectDepartment", requestCode + "------" + resultCode);
        if (requestCode == 0x123) {
            if (data != null) {
                ArrayList<Contacts> select = (ArrayList<Contacts>) data.getExtras().getSerializable("list");
                Log.i("select", JSON.toJSONString(select));
                for (Contacts c : select) {
                    if (!check(c)) {
                        list_contacts.add(c);
                    }
                }
                taskSum.setText(list_contacts.size() + "");
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    class TextWatch implements TextWatcher {
        public AutoCompleteTextView textView;

        public TextWatch(AutoCompleteTextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = textView.getText().toString().trim();
            if (!text.endsWith(",")) {
                if (text.contains(",")) {
                    String[] codes = text.split(",");
                    text = codes[codes.length - 1];
                }
                getPerson(text, textView);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void getPerson(String keyword, final AutoCompleteTextView textView) {
        Subscriber subscriber = new Subscriber<Member>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final Member data) {
                Log.i("getPerson", data.toString());
                final MemberAdapter adapter = new MemberAdapter(data.getRows(), SelectDepartment2Activity.this);
                textView.setAdapter(adapter);
                adapter.setOnItemClickListener(new MemberAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Contacts contacts = data.getRows().get(position);
                        if (check(contacts)) {
                            Toast.makeText(SelectDepartment2Activity.this, "该人员已添加", Toast.LENGTH_SHORT).show();
                        } else {
                            list_contacts.add(contacts);
                            mAdapter.notifyDataSetChanged();
                            taskSum.setText(list_contacts.size() + "");
                            textView.setText("");
                            Toast.makeText(SelectDepartment2Activity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        HttpUtil.getInstance(this, false).getMembers(subscriber, keyword, 1, 10000);
    }

}
