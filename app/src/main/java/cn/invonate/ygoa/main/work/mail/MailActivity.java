package cn.invonate.ygoa.main.work.mail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.Adapter.MailAdapter;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.Mail;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.Domain;
import cn.invonate.ygoa.Util.MailHolder;
import cn.invonate.ygoa.Util.POP3ReceiveMailTest;
import cn.invonate.ygoa.View.LYYPullToRefreshListView;

public class MailActivity extends BaseActivity {
    private static final String USER_NAME = "033523";

    private static final String PASSWORD = "abc123/";

    private static final int MESSAGE_MAIL_ERROR = 0;

    private static final int MESSAGE_MAIL_SUCCESS = 1;

    private static final int MESSAGE_MAIL_NULL = 2;

    @BindView(R.id.mail_box_name)
    TextView mailBoxName;
    @BindView(R.id.list_mail)
    LYYPullToRefreshListView listMail;

    ArrayList<Mail> list_mails = new ArrayList<>(); //邮件规范类集合

    MailAdapter adapter;

    private String folderName = "INBOX";// 默认收件箱

    private int mode = -1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_MAIL_SUCCESS: //获取邮件成功
                    if (msg.getData().getBoolean("is_new")) {//如果是第一页，重新初始化数据
                        adapter = new MailAdapter(list_mails, MailActivity.this, folderName);
                        listMail.setAdapter(adapter);
                        // item点击事件
                        listMail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle bundle = new Bundle();
                                //bundle.putSerializable("mails", list_mails);
                                bundle.putInt("mode", mode);
                                bundle.putInt("position", position - 1);
                                stepActivity(bundle, MailDetailActivity.class);
                            }
                        });
                    } else {//不是第一页，更新数据
                        adapter.notifyDataSetChanged();
                    }
                    listMail.onRefreshComplete();
                    // 如果已加载完全部数据，将上拉加载取消
                    if (adapter.getCount() < total) {
                        listMail.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        listMail.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    break;
                case MESSAGE_MAIL_NULL:
                    adapter.notifyDataSetChanged();//若取到数据为空
                    Toast.makeText(MailActivity.this, "暂无邮件", Toast.LENGTH_SHORT).show();
                    listMail.onRefreshComplete();
                    break;
                case MESSAGE_MAIL_ERROR:
                    //Toast.makeText(MailActivity.this, "获取邮件失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    listMail.onRefreshComplete();
                    break;
            }
        }
    };

    private int total;//邮件的总数

    private PopupWindow pop;//切换邮件箱的弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        ButterKnife.bind(this);
        initPop();//初始化弹窗
        listMail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMails(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMails(adapter.getCount() / 20);
            }
        });
    }

    @Override
    protected void onDestroy() {
        MailHolder.mail_model = null;
        MailHolder.folder = null;
        MailHolder.mails = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        getMails(0);
        super.onResume();
    }

    @OnClick({R.id.img_back, R.id.mail_add, R.id.mail_search, R.id.mail_box_name})
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
        }
    }

    /**
     * 获取邮件
     *
     * @param page 页码
     */
    private void getMails(final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 准备连接服务器的会话信息
                    Properties props = new Properties();
                    props.setProperty("mail.store.protocol", "imap");
                    props.setProperty("mail.imap.host", Domain.MAIL_URL);
                    props.setProperty("mail.imap.port", Domain.MAIL_PORT);

                    // 创建Session实例对象
                    Session session = Session.getInstance(props);

                    // 创建IMAP协议的Store对象
                    Store store = session.getStore("imap");

                    // 连接邮件服务器
                    store.connect(Domain.user_mail, Domain.pass_mail);

                    // 获得收件箱
                    Folder folder = store.getFolder(folderName);


                    MailHolder.folder = folder;//缓存邮件箱

                    // 以读写模式打开收件箱
                    folder.open(Folder.READ_ONLY);

                    // 获得收件箱的邮件列表
                    total = folder.getMessageCount();
                    List<MimeMessage> data = new ArrayList<MimeMessage>();
                    Log.i("MimeMessage", data.toString());
                    if (total != 0) {
                        int start = total - 20 * page;
                        int end = start > 20 ? start - 19 : 1;
                        for (int i = start; i >= end; i--) {
                            Message message = folder.getMessage(i);
                            Message[] messages = new Message[]{message};
                            FetchProfile fp = new FetchProfile();
                            fp.add(FetchProfile.Item.ENVELOPE);
                            folder.fetch(messages, fp);
                            data.add((MimeMessage) message);
                        }
                        if (page == 0) {
                            list_mails = POP3ReceiveMailTest.parseMessage(data);
                            MailHolder.mails = data;
                            MailHolder.mail_model = list_mails;
                        } else {
                            list_mails.addAll(POP3ReceiveMailTest.parseMessage(data));
                            MailHolder.mails.addAll(data);
                            MailHolder.mail_model = list_mails;
                        }
                        System.out.println(list_mails.toString());
                        // 打印不同状态的邮件数量
                        System.out.println("收件箱中共" + total + "封邮件!");
                        android.os.Message msg = new android.os.Message();
                        msg.what = MESSAGE_MAIL_SUCCESS;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("is_new", page == 0);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } else {
                        list_mails.clear();
                        MailHolder.mails.clear();
                        handler.sendEmptyMessage(MESSAGE_MAIL_NULL);
                    }
                    // 关闭资源
                    //folder.close(false);
                    store.close();
                } catch (Exception e) {
                    handler.sendEmptyMessage(MESSAGE_MAIL_ERROR);
                    e.printStackTrace();
                }
            }
        }).start();
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
                    folderName = "Sent";
                    mailBoxName.setText("发件箱");
                    mode = 1;
                    break;
                case R.id.box_drafts:
                    folderName = "Drafts";
                    mailBoxName.setText("草稿箱");
                    mode = 2;
                    break;
                case R.id.box_trash:
                    folderName = "Trash";
                    mailBoxName.setText("已删除");
                    mode = 3;
                    break;
            }
            getMails(0);
            pop.dismiss();
        }
    };
}
