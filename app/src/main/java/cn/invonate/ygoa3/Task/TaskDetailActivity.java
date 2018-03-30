package cn.invonate.ygoa3.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.MemberAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartment2Activity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartment3Activity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.FormatTask;
import cn.invonate.ygoa3.Entry.Task;
import cn.invonate.ygoa3.Entry.TaskDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Task.fragment.TaskDetailFragment;
import cn.invonate.ygoa3.Task.fragment.TaskDetailFragment1;
import cn.invonate.ygoa3.Task.fragment.TaskDetailFragment2;
import cn.invonate.ygoa3.Task.fragment.TaskLineFragment;
import cn.invonate.ygoa3.View.PagerSlidingTabStrip;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import rx.Subscriber;

public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip taskTab;
    @BindView(R.id.pager_task)
    ViewPager pagerTask;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.copy)
    ImageView copy;
    @BindView(R.id.msg)
    EditText msg;
    @BindView(R.id.task_sum)
    TextView taskSum;
    @BindView(R.id.list_button)
    RecyclerView listButton;
    @BindView(R.id.layout_need)
    LinearLayout layoutNeed;
    @BindView(R.id.layout_more)
    TextView layoutMore;

    //private Mission.MissionBean task;
    private String businessId;
    private String taskId;
    private String workflowType;

    Fragment[] fragments;

    private String isXt;

    private boolean need_layout;//是否需要审批意见、抄送等

    YGApplication app;

    private ArrayList<Contacts> ccl = new ArrayList<>();
    private MemberAdapter adapter;

    public SlidingMenu menu;

    private ImageView pic_back;
    private TextView layout_add;
    private SwipeMenuListView list_contact;

    private tFinish finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detaail);
        ButterKnife.bind(this);
        slidingmune();
        app = (YGApplication) getApplication();
        businessId = getIntent().getExtras().getString("businessId");
        taskId = getIntent().getExtras().getString("taskId");
        workflowType = getIntent().getExtras().getString("workflowType");
        need_layout = getIntent().getExtras().getBoolean("need_layout");
        isXt = getIntent().getExtras().getString("isXt");

        finish = new tFinish();
        IntentFilter filter = new IntentFilter();
        filter.addAction("tFinish");
        registerReceiver(finish, filter);


        if (need_layout) {
            layoutNeed.setVisibility(View.VISIBLE);
            layoutMore.setVisibility(View.VISIBLE);
        } else {
            layoutNeed.setVisibility(View.GONE);
            layoutMore.setVisibility(View.GONE);
        }

        if (("1").equals(isXt)) {
            layoutMore.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
        }

        pagerTask.setOffscreenPageLimit(5);
        getTaskDetail();
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        adapter = new MemberAdapter(ccl, this);
        list_contact.setAdapter(adapter);

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
                        ccl.remove(position);
                        adapter.notifyDataSetChanged();
                        taskSum.setText(ccl.size() + "");
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

        pic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailActivity.this, SelectDepartment2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", ccl);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0x123);
            }
        });
        taskSum.setText("0");

        layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog dialog = new ActionSheetDialog(TaskDetailActivity.this).builder();
                dialog.setTitle("请选择操作")
                        .addSheetItem("协同", ActionSheetDialog.SheetItemColor.Black, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepToXt();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finish);
        super.onDestroy();

    }

    /**
     * 初始化menu
     */
    private void slidingmune() {
        menu = new SlidingMenu(this);
        menu.setSlidingEnabled(false);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        menu.setBehindWidth(width);// 设置SlidingMenu菜单的宽度
        menu.setFadeDegree(0.35f);
        LayoutInflater.from(this).inflate(R.layout.menu, null);
        menu.setMenu(R.layout.menu);
        menu.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        pic_back = findViewById(R.id.pic_back);
        layout_add = findViewById(R.id.layout_add);
        list_contact = findViewById(R.id.list_contact);
    }


    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123) {
            if (data != null) {
                ArrayList<Contacts> select = (ArrayList<Contacts>) data.getExtras().getSerializable("list");
                Log.i("select", JSON.toJSONString(select));
                ccl.clear();
                ccl.addAll(select);
                taskSum.setText(ccl.size() + "");
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 获取流程详情
     */
    private void getTaskDetail() {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(app, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String data) {
                Log.i("getTaskDetail ", data);
                String data1 = "{\n" +
                        "  \"title\": \"费用报销单\",\n" +
                        "  \"inputs\": [\n" +
                        "    {\n" +
                        "      \"value\": \"ces\",\n" +
                        "      \"label\": \"费用事由\",\n" +
                        "      \"type\": \"label\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": \"详细说明\",\n" +
                        "      \"label\": \"详细说明\",\n" +
                        "      \"type\": \"label\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"accordion\",\n" +
                        "      \"value\": [\n" +
                        "        {\n" +
                        "          \"content\": {\n" +
                        "            \"分摊部门\": \"供销事业部销售处扬州办事处\",\n" +
                        "            \"发生时间\": \"2017-02-03\",\n" +
                        "            \"费用名称\": \"广告费--产品试用费\",\n" +
                        "            \"税前金额\": 10,\n" +
                        "            \"税金\": 0,\n" +
                        "            \"汇款方式\": \"电汇/现汇\",\n" +
                        "            \"金额\": \"10.00\",\n" +
                        "            \"已付\": 0,\n" +
                        "            \"附外来凭证\": \"0\",\n" +
                        "            \"备注\": null\n" +
                        "          },\n" +
                        "          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"content\": {\n" +
                        "            \"已付\": 0,\n" +
                        "            \"附外来凭证\": \"0\",\n" +
                        "            \"备注\": null\n" +
                        "          },\n" +
                        "          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"label\": \"费用明细\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"text\",\n" +
                        "      \"label\": \"经办人\",\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": \"10.00\",\n" +
                        "      \"label\": \"本次支付\",\n" +
                        "      \"type\": \"label\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"字段名\",\n" +
                        "      \"label\": \"选人\",\n" +
                        "      \"type\": \"picker\",\n" +
                        "      \"limitCount\": \"1\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": \"1\",\n" +
                        "      \"label\": \"收款单位\",\n" +
                        "      \"type\": \"label\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": \"沈浩2017-02-10\",\n" +
                        "      \"label\": \"经办人\",\n" +
                        "      \"type\": \"label\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"workflowType\",\n" +
                        "      \"value\": \"13\",\n" +
                        "      \"type\": \"hidden\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"businessId\",\n" +
                        "      \"value\": \"9988D5B1-7A4F-4873-9A3C-03C4435534F6\",\n" +
                        "      \"type\": \"hidden\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": \"测试1234\",\n" +
                        "      \"type\": \"hidden\",\n" +
                        "      \"areaCode\": \"0_A_详情\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": [\n" +
                        "        \"未付金额：1000\",\n" +
                        "        \"\",\n" +
                        "        \"已付金额：11111\",\n" +
                        "        \"合计：1000000\"\n" +
                        "      ],\n" +
                        "      \"color\": [\n" +
                        "        \"5ABE98\",\n" +
                        "        \"\",\n" +
                        "        \"F4511E\",\n" +
                        "        \"#333333\"\n" +
                        "      ],\n" +
                        "      \"type\": \"fourSingle\",\n" +
                        "      \"areaCode\": \"1_A_付款信息\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"value\": [\n" +
                        "        \"第二次付款\",\n" +
                        "        \"已付\",\n" +
                        "        \"金额：11111\",\n" +
                        "        \"HK:8000000\"\n" +
                        "      ],\n" +
                        "      \"color\": [\n" +
                        "        \"#333333\",\n" +
                        "        \"5ABE98\",\n" +
                        "        \"#333333\",\n" +
                        "        \"#A9A9A9\"\n" +
                        "      ],\n" +
                        "      \"type\": \"fourSingle\",\n" +
                        "      \"areaCode\": \"1_A_付款信息\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"attachment\",\n" +
                        "      \"label\": \"安监局费用2017.excel\",\n" +
                        "      \"size\": \"1024kb\",\n" +
                        "      \"url\": \"/ygoa/xxxxxx\",\n" +
                        "      \"areaCode\": \"2_B_发票\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"attachment\",\n" +
                        "      \"label\": \"安监局费用2017.doc\",\n" +
                        "      \"size\": \"1024kb\",\n" +
                        "      \"url\": \"/ygoa/xxxxxx\",\n" +
                        "      \"areaCode\": \"3_B_合同\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"success\": \"0\"\n" +
                        "}\n";
                TaskDetail detail = JSON.parseObject(data, TaskDetail.class);
                title.setText(detail.getTitle());
                initFragment(detail, check(detail.getInputs()));

                if ("1".equals(isXt)) {
                    List<TaskDetail.Button> buttons = new ArrayList<>();
                    buttons.add(new TaskDetail.Button("", "确定", "/ygoa/ydpt/approveAllXt.action", "true"));
                    detail.setButtons(buttons);
                } else {
                    if (detail.getButtons() == null || detail.getButtons().isEmpty()) {
                        List<TaskDetail.Button> buttons = new ArrayList<>();
                        buttons.add(new TaskDetail.Button("approveResult", "同意", "/ygoa/ydpt/processTask.action", "true"));
                        buttons.add(new TaskDetail.Button("approveResult", "驳回", "/ygoa/ydpt/processTask.action", "false"));
                        detail.setButtons(buttons);
                    }
                }
                listButton.setLayoutManager(new GridLayoutManager(TaskDetailActivity.this, detail.getButtons().size()));
                listButton.setAdapter(new ButtonAdapter(detail.getButtons(), TaskDetailActivity.this));
            }
        };
        HttpUtil.getInstance(this, false).getTaskDetail(subscriber, app.getUser().getSessionId(), businessId, taskId, workflowType);
    }

    /**
     * 判断新数据还是老数据
     *
     * @param list_input
     * @return
     */
    private boolean check(List<TaskDetail.Input> list_input) {
        for (TaskDetail.Input input : list_input) {
            if (input.getAreaCode() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param detail
     * @param is_new 是否需要转格式
     */
    private void initFragment(TaskDetail detail, boolean is_new) {
        if (is_new) {
            FormatTask format = new FormatTask();
            format.setTitle(detail.getTitle());
            format.setButtons(detail.getButtons());
            List<FormatTask.TaskGroup> tasks = new ArrayList<>();
            int max = 0;
            for (int i = 0; i < detail.getInputs().size(); i++) {
                String[] area = detail.getInputs().get(i).getAreaCode().split("_");
                int index = Integer.parseInt(area[0]);
                detail.getInputs().get(i).setF_type(area[1]);
                detail.getInputs().get(i).setIndex(index);
                detail.getInputs().get(i).setF_name(area[2]);
                if (index > max) {
                    max = index;
                }
            }

            for (int i = 0; i <= max; i++) {
                FormatTask.TaskGroup t = new FormatTask.TaskGroup();
                ArrayList<TaskDetail.Input> inputs = new ArrayList<>();
                t.setIndex(i);
                for (TaskDetail.Input input : detail.getInputs()) {
                    if (input.getIndex() == i) {
                        t.setType(input.getF_type());
                        t.setName(input.getF_name());
                        inputs.add(input);
                    }
                }
                t.setInputs(inputs);
                tasks.add(t);
            }
            format.setTasks(tasks);
            Log.i("FormatTask", JSON.toJSONString(format));

            // 初始化Fragment
            fragments = new Fragment[format.getTasks().size() + 1];
            for (int i = 0; i < format.getTasks().size(); i++) {
                switch (format.getTasks().get(i).getType()) {
                    case "A":
                        TaskDetailFragment1 f1 = new TaskDetailFragment1();
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("inputs", format.getTasks().get(i).getInputs());
                        f1.setArguments(bundle1);
                        fragments[i] = f1;
                        break;
                    case "B":
                        TaskDetailFragment2 f2 = new TaskDetailFragment2();
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("inputs", format.getTasks().get(i).getInputs());
                        f2.setArguments(bundle2);
                        fragments[i] = f2;
                        break;
                }
            }
            TaskLineFragment f = new TaskLineFragment();
            Bundle bundle = new Bundle();
            bundle.putString("businessId", businessId);
            f.setArguments(bundle);
            fragments[format.getTasks().size()] = f;

            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), format.getTasks());
            pagerTask.setAdapter(adapter);
            taskTab.setViewPager(pagerTask);

        } else {
            fragments = new Fragment[2];
            TaskDetailFragment f1 = new TaskDetailFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("inputs", detail.getInputs());
            f1.setArguments(bundle1);
            fragments[0] = f1;
            TaskLineFragment f2 = new TaskLineFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("businessId", businessId);
            f2.setArguments(bundle2);
            fragments[1] = f2;

            MyPagerAdapterOld adapter = new MyPagerAdapterOld(getSupportFragmentManager());
            pagerTask.setAdapter(adapter);
            taskTab.setViewPager(pagerTask);


        }
    }

    public class MyPagerAdapterOld extends LazyFragmentPagerAdapter {

        public MyPagerAdapterOld(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String name = "";
            if (position == 0) {
                name = "详情";
            } else {
                name = "审批轨迹";
            }
            return name;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        protected Fragment getItem(ViewGroup container, int position) {
            return fragments[position];
        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<FormatTask.TaskGroup> groups;

        public MyPagerAdapter(FragmentManager fm, List<FormatTask.TaskGroup> groups) {
            super(fm);
            this.groups = groups;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String name = "";
            if (position < groups.size()) {
                name = groups.get(position).getName();
            } else {
                name = "审批轨迹";
            }
            return name;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }

    /**
     * 按钮适配器
     */
    class ButtonAdapter extends Adapter<ButtonAdapter.ViewHolder> {

        private List<TaskDetail.Button> data;
        private Context context;

        public ButtonAdapter(List<TaskDetail.Button> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public ButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_detail_button, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ButtonAdapter.ViewHolder holder, final int position) {
            holder.btn.setText(data.get(position).getLabel());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processTask(data.get(position).getUrl(), data.get(position).getName(), data.get(position).getValue());
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.btn)
            Button btn;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    private void stepToXt() {
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", app.getUser().getSessionId());
        for (Fragment f : fragments) {
            if (f instanceof TaskDetailFragment) {
                Map<String, String> map = ((TaskDetailFragment) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            } else if (f instanceof TaskDetailFragment1) {
                Map<String, String> map = ((TaskDetailFragment1) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            } else if (f instanceof TaskDetailFragment2) {
                Map<String, String> map = ((TaskDetailFragment2) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("map", JSON.toJSONString(params));
        stepActivity(bundle, SelectDepartment3Activity.class);
    }

    /**
     * 同意和拒绝
     */
    private void processTask(String url, String name, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(name, value);
        params.put("sessionId", app.getUser().getSessionId());
        for (Fragment f : fragments) {
            if (f instanceof TaskDetailFragment) {
                Map<String, String> map = ((TaskDetailFragment) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            } else if (f instanceof TaskDetailFragment1) {
                Map<String, String> map = ((TaskDetailFragment1) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            } else if (f instanceof TaskDetailFragment2) {
                Map<String, String> map = ((TaskDetailFragment2) f).getMessage();
                if (map == null) {
                    return;
                }
                params.putAll(map);
            }
        }
        String cmnt = msg.getText().toString().trim();
        if (("1").equals(isXt) && cmnt.equals("")) {
            Toast.makeText(app, "请输入审批意见", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!"".equals(cmnt)) {
            params.put("cmnt", cmnt);
        }

        StringBuffer sb = new StringBuffer();
        for (Contacts c : ccl) {
            sb.append(c.getUser_code() + ",");
        }

        params.put("ccr", sb.toString());

        Log.i("params", JSON.toJSONString(params));

        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Task>() {

            @Override
            public void onNext(Task data) {
                Log.i("processTask", data.toString());
                if (data.getSuccess() == 0) {
                    finish();
                } else {
                    Toast.makeText(app, data.getMsg() + "", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this, false).processTask(new ProgressSubscriber(onNextListener, this, ""), url, params);
    }

    class tFinish extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
