package cn.invonate.ygoa3.main.work.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.MyApplicationList;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.KLog;
import cn.invonate.ygoa3.Util.SpUtil;
import cn.invonate.ygoa3.WebView.WebViewActivity;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * Created by yang on 2018/3/19.
 */

public class MyApplicationActivity extends BaseActivity {
    YGApplication app;
    @BindView(R.id.pic_back)
    ImageView picBack;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.rv_common_app)
    RecyclerView rvCommonApp;
    @BindView(R.id.rv_all_app)
    RecyclerView rvAllApp;
    AllApplicationAdapter allAdapter;
    CommonAdapter commonAdapter;
    private List<MyApplicationList> commonApplication = new ArrayList<>();
    private List<MyApplicationList> allApplication = new ArrayList<>();
    private List<MyApplicationList.Head2Location> head = new ArrayList<>();
    List<Integer> positionOfHead = new ArrayList<>();
    //是否显示加减号
    boolean isShow = false;
    //编辑按钮是否按下
    private boolean editHasFocus = false;
    //加号减号状态信息
    private List<HashMap<Integer, Boolean>> commonApp = new ArrayList<>();
    private List<HashMap<Integer, Boolean>> allApp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_application);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        initUrlPic();
    }

    @OnClick({R.id.pic_back, R.id.edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                if (editHasFocus) {
                    AlertDialog alertDialog1 = new AlertDialog(MyApplicationActivity.this).builder().setMsg("是否保存").setCancelable(false)
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SpUtil.writeString("commonApp", JSON.toJSONString(commonApplication));
                                    finish();
                                }
                            });
                    alertDialog1.show();
                } else {
                    finish();
                }
                break;
            case R.id.edit:
                if (!editHasFocus) {
                    isShow = true;
                    edit.setText("完成");
                    editHasFocus = true;
                    allAdapter.notifyDataSetChanged();
                    commonAdapter.notifyDataSetChanged();
                } else {
                    isShow = false;
                    editHasFocus = false;
                    SpUtil.writeString("commonApp", JSON.toJSONString(commonApplication));
                    edit.setText("编辑");
                    allAdapter.notifyDataSetChanged();
                    commonAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    //初始化图片数据
    private void initUrlPic() {
        if (TextUtils.isEmpty(SpUtil.readString("function_name_id"))) {
            List<MyApplicationList.PicToName> picToNames = new ArrayList<>();
            //人事招聘
            picToNames.add(new MyApplicationList.PicToName("年度计划", R.mipmap.app_ndjh));
            picToNames.add(new MyApplicationList.PicToName("临时计划", R.mipmap.app_lsjh));
            picToNames.add(new MyApplicationList.PicToName("人员信息台账", R.mipmap.app_ryxxtz));
            picToNames.add(new MyApplicationList.PicToName("招工", R.mipmap.app_zgsq));
            picToNames.add(new MyApplicationList.PicToName("驳回明细", R.mipmap.app_bhmx));
            picToNames.add(new MyApplicationList.PicToName("完成进度", R.mipmap.app_wcjd));
            //日常审批
            picToNames.add(new MyApplicationList.PicToName("轧辊", R.mipmap.app_zh));
            picToNames.add(new MyApplicationList.PicToName("新供方审批", R.mipmap.app_xgf));
            picToNames.add(new MyApplicationList.PicToName("快餐配送", R.mipmap.app_dcps));
            picToNames.add(new MyApplicationList.PicToName("采购", R.mipmap.app_cgd));
            picToNames.add(new MyApplicationList.PicToName("工程", R.mipmap.app_gclxd));
            picToNames.add(new MyApplicationList.PicToName("用车", R.mipmap.app_ycsq));
            picToNames.add(new MyApplicationList.PicToName("信息", R.mipmap.app_xxlld));
            picToNames.add(new MyApplicationList.PicToName("月度能源考核", R.mipmap.app_ydnykh));
            picToNames.add(new MyApplicationList.PicToName("用能", R.mipmap.app_ynsq));
            picToNames.add(new MyApplicationList.PicToName("委托验审", R.mipmap.app_wtys));
            picToNames.add(new MyApplicationList.PicToName("使用情况证明", R.mipmap.app_syqk));
            picToNames.add(new MyApplicationList.PicToName("设备新增", R.mipmap.app_sbxz));
            picToNames.add(new MyApplicationList.PicToName("办公家具", R.mipmap.app_bgjj));
            picToNames.add(new MyApplicationList.PicToName("文件评审", R.mipmap.app_wjps));
            //工会审批
            picToNames.add(new MyApplicationList.PicToName("新婚礼品", R.mipmap.app_xhlp));
            picToNames.add(new MyApplicationList.PicToName("活动经费", R.mipmap.app_hdjf));
            picToNames.add(new MyApplicationList.PicToName("慰问", R.mipmap.app_ww));
            //财务
            picToNames.add(new MyApplicationList.PicToName("汇款汇总单", R.mipmap.app_hksphz));
            picToNames.add(new MyApplicationList.PicToName("费用报销", R.mipmap.app_fybxd));
            picToNames.add(new MyApplicationList.PicToName("汇款", R.mipmap.app_hksp));
            picToNames.add(new MyApplicationList.PicToName("借款", R.mipmap.app_jk));
            picToNames.add(new MyApplicationList.PicToName("退款", R.mipmap.app_tk));
            SpUtil.writeString("function_name_id", JSON.toJSONString(picToNames));
        }
        if (!TextUtils.isEmpty(SpUtil.readString("commonApp"))) {
            commonApplication = JSONArray.parseObject(SpUtil.readString("commonApp"), new TypeReference<List<MyApplicationList>>() {
            });
            for (MyApplicationList myApplicationList : commonApplication) {
                myApplicationList.setPic_id(searchPicture(myApplicationList.getFunction_name()));
            }
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvCommonApp.setLayoutManager(layoutManager);
        commonApp.clear();
        for (int i = 0; i < commonApplication.size(); i++) {
            HashMap<Integer, Boolean> hashMap = new HashMap<>();
            hashMap.put(i, false);
            commonApp.add(hashMap);
        }
        commonAdapter = new CommonAdapter(commonApplication, MyApplicationActivity.this, commonApp);
        rvCommonApp.setAdapter(commonAdapter);
        //stringToJson(result);
        getApplicationList();
    }

    private void stringToJson(List<List<MyApplicationList>> allApplications) {
        for (int i = 0; i < allApplications.size(); i++) {
            for (int j = 0; j < allApplications.get(i).size(); j++) {
                if (TextUtils.isEmpty(allApplications.get(i).get(j).getPhone_url())) {
                    Collections.swap(allApplications.get(i), j, 0);
                    break;
                }
            }
            for (int j = 0; j < allApplications.get(i).size(); j++) {
                MyApplicationList dateBean = allApplications.get(i).get(j);
                dateBean.setPic_id(searchPicture(dateBean.getFunction_name()));
                allApplication.add(dateBean);
            }
        }
        KLog.json(JSON.toJSONString(allApplication));

        //添加头部及偏移位置

        for (int i = 0; i < allApplication.size(); i++) {
            if (TextUtils.isEmpty(allApplication.get(i).getPhone_url())) {
                if (i == 0) {
                    MyApplicationList.Head2Location head2Location = new MyApplicationList.Head2Location(allApplication.get(i).getFunction_name(), i);
                    head.add(head2Location);
                } else {
                    positionOfHead.add(i);
                }
            }
        }
        for (int i = 0; i < positionOfHead.size(); i++) {
            if ((i + 1) <= positionOfHead.size() - 1) {
                MyApplicationList.Head2Location head2Location = new MyApplicationList.Head2Location(allApplication.get(positionOfHead.get(i)).getFunction_name(), positionOfHead.get(i + 1));
                head.add(head2Location);
            } else {
                MyApplicationList.Head2Location head2Location = new MyApplicationList.Head2Location(allApplication.get(positionOfHead.get(i)).getFunction_name(), allApplication.size() - 1);
                head.add(head2Location);
            }
        }
        for (int i = 0; i < head.size(); i++) {
            tab.addTab(tab.newTab().setText(head.get(i).getFunction_name()));
            tab.getTabAt(i).setTag(head.get(i));
            KLog.json(JSON.toJSONString(head.get(i)));
        }

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                rvAllApp.smoothScrollToPosition(((MyApplicationList.Head2Location) tab.getTag()).getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                rvAllApp.smoothScrollToPosition(((MyApplicationList.Head2Location) tab.getTag()).getPosition());
            }
        });
        for (int i = 0; i < allApplication.size(); i++) {
            boolean isAD = false;
            for (int j = 0; j < commonAdapter.getItemCount(); j++) {
                if (allApplication.get(i).getFunction_name().equals(commonAdapter.getExistItem().get(j).getFunction_name())) {
                    isAD = true;
                    break;
                }
            }
            HashMap<Integer, Boolean> hashMap = new HashMap<>();
            hashMap.put(i, isAD);
            allApp.add(hashMap);
        }
        allAdapter = new AllApplicationAdapter(allApplication, MyApplicationActivity.this, allApp);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (allAdapter.getItemViewType(position) == 0) {
                    return 4;
                } else if (allAdapter.getItemViewType(position) == 1) {
                    return 1;
                } else {
                    return 4;
                }
            }
        });
        rvAllApp.setLayoutManager(gridLayoutManager);
        rvAllApp.setAdapter(allAdapter);
        rvAllApp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                gridLayoutManager.findFirstCompletelyVisibleItemPosition();

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                List<Integer> position = positionOfHead;
                position.add(0, 0);
                for (int i = 0; i < position.size(); i++) {
                    if (8 == positionOfHead.get(i)) {
                        for (int j = 0; i < tab.getTabCount(); j++) {
                            if (positionOfHead.get(i) == ((MyApplicationList.Head2Location) tab.getTabAt(i).getTag()).getPosition()) {
                                tab.getTabAt(i).getCustomView().setSelected(true);
                            }
                        }
                    }
                }
            }
        });
    }

    private int searchPicture(String function_name) {
        if (!TextUtils.isEmpty(SpUtil.readString("function_name_id"))) {
            List<MyApplicationList.PicToName> picToNames = new ArrayList<>();
            picToNames = JSONArray.parseObject(SpUtil.readString("function_name_id"), new TypeReference<List<MyApplicationList.PicToName>>() {
            });
            for (MyApplicationList.PicToName picToName : picToNames) {
                if (picToName.getFunction_name().equals(function_name)) {
                    return picToName.getPic_id();
                }
            }
        }
        return R.mipmap.app_bgjj;
    }


    private void getApplicationList() {
        Subscriber subscriber = new Subscriber<List<List<MyApplicationList>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof UnknownHostException) {
                    Toast.makeText(getApplicationContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(List<List<MyApplicationList>> allApplications) {
                stringToJson(allApplications);
            }


        };
        HttpUtil.getInstance(MyApplicationActivity.this, false).get_application(subscriber);
    }


    //适配器
    public class AllApplicationAdapter extends RecyclerView.Adapter<AllApplicationAdapter.ViewHolder> {
        List<MyApplicationList> myApplicationLists = new ArrayList<>();
        Context mContext;
        public List<HashMap<Integer, Boolean>> ADMode = new ArrayList<>();//约定false为加号，true为减号

        public AllApplicationAdapter(List<MyApplicationList> myApplicationLists, Context context, List<HashMap<Integer, Boolean>> ADMode) {
            this.myApplicationLists = myApplicationLists;
            mContext = context;
            this.ADMode = ADMode;

        }

        public void setViewStatus(List<HashMap<Integer, Boolean>> ADMode) {
            this.ADMode = ADMode;
        }

        public HashMap<Integer, Boolean> getViewStatus(int position) {
            return ADMode.get(position);
        }

        //获取commonAdapter中存在条目
        public List<MyApplicationList> getExistItem() {
            return myApplicationLists;
        }

        @Override
        public int getItemViewType(int position) {
            return myApplicationLists.get(position).getPhone_url().isEmpty() ? 0 : 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            ViewHolder viewHolder;
            if (viewType == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.rv_app_head, parent, false);
                viewHolder = new ViewHolder(view, viewType);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.rv_app_list, parent, false);
                viewHolder = new ViewHolder(view, viewType);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    holder.head.setText(myApplicationLists.get(position).getFunction_name());
                    break;
                case 1:
                    holder.tv_app_name.setText(myApplicationLists.get(position).getFunction_name());
                    int res = myApplicationLists.get(position).getPic_id();
                    holder.pic_app_name.setImageResource(res);
                    if (isShow) {
                        holder.add_delete.setVisibility(View.VISIBLE);
                        holder.bg_color.setBackgroundColor(getResources().getColor(R.color.title_color));
                    } else {
                        holder.add_delete.setVisibility(View.GONE);
                        holder.bg_color.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    if (getViewStatus(position).get(position)) {
                        holder.add_delete.setImageResource(R.mipmap.app_delete);
                    } else {
                        holder.add_delete.setImageResource(R.mipmap.app_add);
                    }
                    holder.add_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getViewStatus(position).get(position)) {
                                //减少
                                allApp.get(position).put(position, false);
                                for (int j = 0; j < commonAdapter.getItemCount(); j++) {
                                    if (commonApplication.get(j).getFunction_name().equals(myApplicationLists.get(position).getFunction_name())) {
                                        commonApplication.remove(j);
                                        break;
                                    }
                                }
                                commonAdapter.notifyDataSetChanged();
                                notifyDataSetChanged();
                            } else {
                                //添加
                                if (commonApplication.size() == 8) {
                                    Toast.makeText(mContext, "最多添加8个应用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                allApp.get(position).put(position, true);
                                commonApplication.add(myApplicationLists.get(position));
                                commonAdapter.notifyDataSetChanged();
                                notifyDataSetChanged();
                            }
                        }
                    });
                    holder.pic_app_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editHasFocus) {
                                return;
                            }

                            Intent intent = new Intent(MyApplicationActivity.this, WebViewActivity.class);
                            intent.putExtra("name", myApplicationLists.get(position).getFunction_name());
                            intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code=" + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&url=" + myApplicationLists.get(position).getPhone_url());
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return myApplicationLists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView head;
            ImageView add_delete;
            ImageView pic_app_name;
            TextView tv_app_name;
            View bg_color;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                if (viewType == 0) {
                    head = itemView.findViewById(R.id.head);
                } else {
                    bg_color = itemView.findViewById(R.id.bg_color);
                    add_delete = itemView.findViewById(R.id.pic_add_delete);
                    pic_app_name = itemView.findViewById(R.id.pic_app_name);
                    tv_app_name = itemView.findViewById(R.id.tv_app_name);
                }

            }
        }
    }

    class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
        private List<MyApplicationList> myApplicationLists;
        private Context mContext;
        public List<HashMap<Integer, Boolean>> ADMode = new ArrayList<>();//约定false为加号，true为减号

        public CommonAdapter(List<MyApplicationList> myApplicationLists, Context context, List<HashMap<Integer, Boolean>> ADMode) {
            this.myApplicationLists = myApplicationLists;
            mContext = context;
            this.ADMode = ADMode;
        }

        public void setViewStatus(List<HashMap<Integer, Boolean>> ADMode) {
            this.ADMode = ADMode;
        }

        public HashMap<Integer, Boolean> getViewStatus(int position) {
            return ADMode.get(position);
        }

        //获取commonAdapter中存在条目
        public List<MyApplicationList> getExistItem() {
            return myApplicationLists;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_app_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_app_name.setText(myApplicationLists.get(position).getFunction_name());
            holder.pic_app_name.setImageResource(myApplicationLists.get(position).getPic_id());
            if (isShow) {
                holder.add_delete.setVisibility(View.VISIBLE);
                holder.bg_color.setBackgroundColor(getResources().getColor(R.color.title_color));
            } else {
                holder.add_delete.setVisibility(View.GONE);
                holder.bg_color.setBackgroundColor(getResources().getColor(R.color.white));
            }
            holder.add_delete.setImageResource(R.mipmap.app_delete);
            holder.add_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < allAdapter.getItemCount(); j++) {
                        if (myApplicationLists.get(position).getFunction_name().equals(allApplication.get(j).getFunction_name())) {
                            allApp.get(j).put(j, false);
                            break;
                        }
                    }
                    commonApplication.remove(position);
                    commonAdapter.notifyDataSetChanged();
                    allAdapter.notifyDataSetChanged();

                }
            });
            holder.pic_app_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editHasFocus) {
                        return;
                    }
                    KLog.i(myApplicationLists.get(position).getFunction_name() + "  我被点击了");
                    Intent intent = new Intent(MyApplicationActivity.this, WebViewActivity.class);
                    intent.putExtra("name", myApplicationLists.get(position).getFunction_name());
                    intent.putExtra("url", HttpUtil.BASE_URL + myApplicationLists.get(position).getPhone_url());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return myApplicationLists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView add_delete;
            ImageView pic_app_name;
            TextView tv_app_name;
            View bg_color;

            public ViewHolder(View itemView) {
                super(itemView);
                add_delete = itemView.findViewById(R.id.pic_add_delete);
                pic_app_name = itemView.findViewById(R.id.pic_app_name);
                tv_app_name = itemView.findViewById(R.id.tv_app_name);
                bg_color = itemView.findViewById(R.id.bg_color);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在编辑状态下返回提示是否保存
        if (keyCode == KeyEvent.KEYCODE_BACK && editHasFocus) {
            AlertDialog alertDialog = new AlertDialog(MyApplicationActivity.this).builder().setMsg("是否保存").setCancelable(false)
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpUtil.writeString("commonApp", JSON.toJSONString(commonApplication));
                            finish();
                        }
                    });
            alertDialog.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
