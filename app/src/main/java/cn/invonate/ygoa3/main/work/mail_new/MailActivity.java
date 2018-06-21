package cn.invonate.ygoa3.main.work.mail_new;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.MailNewAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.MailMessage;
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.KLog;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil3;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import rx.Subscriber;

public class MailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mail_box_name)
    TextView mailBoxName;
    @BindView(R.id.list_mail)
    ListView listMail;

    MailNewAdapter adapter;
    @BindView(R.id.layout_edit)
    LinearLayout layoutEdit;
    @BindView(R.id.et_mail)
    TextView etMail;
    @BindView(R.id.layout_all)
    RelativeLayout layoutAll;
    @BindView(R.id.layout_none)
    RelativeLayout layoutNone;
    @BindView(R.id.rl_edit)
    RelativeLayout rlEdit;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private String folderName = "INBOX";// 默认收件箱

    private int mode = 0;

    private YGApplication app;

    private PopupWindow pop;//切换邮件箱的弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        ButterKnife.bind(this);
        initPop();//初始化弹窗
        app = (YGApplication) getApplication();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                listMail.setOnItemClickListener(null);
                getNewMails(1, true);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Log.i("size", adapter.getCount() + "");
                if (adapter.getCount() % 10 == 0)
                    getNewMails(adapter.getCount() / 10 + 1, false);
                else
                    refresh.finishLoadMore();
            }
        });
        refresh.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        MailHolder.mailsBeans = null;
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.img_back, R.id.mail_add, R.id.mail_search, R.id.mail_box_name, R.id.layout_all, R.id.layout_none})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.mail_add:
                stepActivity(SendMailActivity.class);
                break;
            case R.id.mail_search:
                break;
            case R.id.mail_box_name:
                pop.showAsDropDown(view, AutoUtils.getPercentWidthSize(-90), 0, Gravity.CENTER);
                break;
            case R.id.layout_all:
                for (MailNew.ResultBean.MailsBean mail : MailHolder.mailsBeans) {
                    mail.setSelect(true);
                }
                etMail.setText("删除");
                etMail.setTextColor(Color.parseColor("#FF0000"));
                adapter.notifyDataSetChanged();
                break;
            case R.id.layout_none:
                for (MailNew.ResultBean.MailsBean mail : MailHolder.mailsBeans) {
                    mail.setSelect(false);
                }
                etMail.setText("取消编辑");
                etMail.setTextColor(Color.parseColor("#000000"));
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 获取邮件
     *
     * @param page
     */
    private void getNewMails(final int page, final boolean update) {
        Subscriber subscriber = new Subscriber<MailNew>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(MailActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                refresh.finishRefresh(false);
                refresh.finishLoadMore(false);
            }

            @Override
            public void onNext(MailNew data) {
                KLog.i("getNewMails", data);
                if ("0000".equals(data.getCode())) {
                    if (page == 1 && update) {
                        MailHolder.mailsBeans = data.getResult().getMails();
                        adapter = new MailNewAdapter(MailHolder.mailsBeans, MailActivity.this, folderName);
                        listMail.setAdapter(adapter);
                        etMail.setOnClickListener(MailActivity.this);
                        listMail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (adapter.isSelect_mode()) {
                                    MailHolder.mailsBeans.get(position).setSelect(!MailHolder.mailsBeans.get(position).isSelect());
                                    adapter.notifyDataSetChanged();
                                    if (checkSelect()) {
                                        etMail.setText("删除");
                                        etMail.setTextColor(Color.parseColor("#FF0000"));
                                    } else {
                                        etMail.setText("取消编辑");
                                        etMail.setTextColor(Color.parseColor("#000000"));
                                    }
                                } else {
                                    Intent intent = new Intent(MailActivity.this, MailDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("position", position);
                                    bundle.putInt("mode", mode);
//                                    bundle.putSerializable("list", list_mails);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        });
                        if (MailHolder.mailsBeans.isEmpty())
                            Toast.makeText(MailActivity.this, "暂无邮件", Toast.LENGTH_SHORT).show();
                    } else {
                        MailHolder.mailsBeans.addAll(data.getResult().getMails());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MailActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        };
        HttpUtil3.getInstance(this, false).getMailList(subscriber, app.getUser().getRsbm_pk(), "", page, 10, folderName, null);
    }

    /**
     * 初始化弹窗
     */
    private void initPop() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_mail, null);
        popView.findViewById(R.id.box_in).setOnClickListener(popListener);
        popView.findViewById(R.id.box_sent).setOnClickListener(popListener);
        popView.findViewById(R.id.box_drafts).setOnClickListener(popListener);
        popView.findViewById(R.id.box_trash).setOnClickListener(popListener);
        pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
        AutoUtils.autoSize(popView);
    }

    private View.OnClickListener popListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.box_in:
                    folderName = "INBOX";
                    mailBoxName.setText("收件箱");
                    mode = 0;
                    break;
                case R.id.box_sent:
                    folderName = "SENT";
                    mailBoxName.setText("发件箱");
                    mode = 1;
                    break;
                case R.id.box_drafts:
                    folderName = "DRAFTS";
                    mailBoxName.setText("草稿箱");
                    mode = 2;
                    break;
                case R.id.box_trash:
                    folderName = "TRASH";
                    mailBoxName.setText("已删除");
                    mode = 3;
                    break;
            }
            refresh.autoRefresh();
            pop.dismiss();
        }
    };


    /**
     * 监测是否有选中
     *
     * @return
     */
    private boolean checkSelect() {
        for (MailNew.ResultBean.MailsBean mail : MailHolder.mailsBeans) {
            if (mail.isSelect()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (adapter.isSelect_mode()) {
            if (!checkSelect()) {// 取消编辑
                adapter.setSelect_mode(false);
                layoutEdit.setVisibility(View.GONE);
                for (MailNew.ResultBean.MailsBean mail : MailHolder.mailsBeans) {
                    mail.setSelect(false);
                }
                etMail.setText("编辑");
                etMail.setTextColor(Color.parseColor("#000000"));
            } else { // 删除邮件
                List<MailNew.ResultBean.MailsBean> list_mail = new ArrayList<>();
                for (int i = 0; i < MailHolder.mailsBeans.size(); i++) {
                    if (MailHolder.mailsBeans.get(i).isSelect()) {
                        list_mail.add(MailHolder.mailsBeans.get(i));
                    }
                }
                int[] ids = new int[list_mail.size()];
                for (int i = 0; i < list_mail.size(); i++) {
                    ids[i] = list_mail.get(i).getId();
                }
                deleteMail(ids);
            }
        } else { // 进行编辑
            adapter.setSelect_mode(true);
            layoutEdit.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            etMail.setText("取消编辑");
            etMail.setTextColor(Color.parseColor("#000000"));
        }
    }

    /**
     * 将邮件添加至垃圾箱
     *
     * @param msgId
     */
    private void deleteMail(int[] msgId) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MailMessage>() {
            @Override
            public void onNext(MailMessage data) {
                Log.i("appendMailToFolder", data.toString());
                if (data.getCode().equals("0000")) {
                    Toast.makeText(app, "删除成功", Toast.LENGTH_SHORT).show();
                    etMail.setText("编辑");
                    etMail.setTextColor(Color.parseColor("#000000"));
                    layoutEdit.setVisibility(View.GONE);
                    refresh.autoRefresh();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil3.getInstance(this, false).appendMailToFolder(new ProgressSubscriber(onNextListener, this, "删除中"), app.getUser().getRsbm_pk(), app.getUser().getRsbm_pk(), msgId, adapter.getType());
    }
}
