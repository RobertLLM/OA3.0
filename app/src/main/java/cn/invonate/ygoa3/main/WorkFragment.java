package cn.invonate.ygoa3.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Task.TaskApprovedActivity;
import cn.invonate.ygoa3.Task.TaskCopyActivity;
import cn.invonate.ygoa3.Task.TaskListActivity;
import cn.invonate.ygoa3.Task.TaskStartActivity;
import cn.invonate.ygoa3.Util.Domain;
import cn.invonate.ygoa3.WebView.WebViewActivity;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.main.work.mail.MailActivity;
import rx.Subscriber;

/**
 * Created by liyangyang on 2017/10/22.
 */

public class WorkFragment extends Fragment {
    Unbinder unbinder;

    YGApplication app;
    @BindView(R.id.task_sum)
    TextView taskSum;
    @BindView(R.id.mail_sum)
    TextView mailSum;
    @BindView(R.id.refresh)
    PullToRefreshScrollView refresh;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                taskSum.setText(msg.getData().getInt("task_size") + "");
                mailSum.setText(msg.getData().getInt("mail_size") + "");
                refresh.onRefreshComplete();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        taskSum.addTextChangedListener(new TextChangeListener(taskSum));
        mailSum.addTextChangedListener(new TextChangeListener(mailSum));
        getTaskSize();
        refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getTaskSize();
            }
        });
        return view;
    }

    class TextChangeListener implements TextWatcher {

        private TextView tv;

        public TextChangeListener(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (Integer.parseInt(tv.getText().toString()) == 0) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_process, R.id.layout_process_added, R.id.layout_process_down, R.id.layout_process_copy, R.id.layout_mail, R.id.layout_work, R.id.layout_allow, R.id.layout_finance, R.id.layout_union, R.id.layout_recruit, R.id.layout_news, R.id.layout_notice, R.id.layout_meting, R.id.layout_meal})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.layout_process:
                intent = new Intent(getActivity(), TaskListActivity.class);
                break;
            case R.id.layout_process_added:
                intent = new Intent(getActivity(), TaskStartActivity.class);
                break;
            case R.id.layout_process_down:
                intent = new Intent(getActivity(), TaskApprovedActivity.class);
                break;
            case R.id.layout_process_copy:
                intent = new Intent(getActivity(), TaskCopyActivity.class);
                break;
            case R.id.layout_mail:
                intent = new Intent(getActivity(), MailActivity.class);
                break;
            case R.id.layout_work:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "我的工作");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=4");
                break;
            case R.id.layout_allow:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "日常审批");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=0");
                break;
            case R.id.layout_finance:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "财务审批");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=3");
                break;
            case R.id.layout_union:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "公会审批");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=1");
                break;
            case R.id.layout_recruit:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "人事招聘");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=2");
                break;
            case R.id.layout_news:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "新闻动态");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=6");
                break;
            case R.id.layout_notice:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "通知公告");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=7");
                break;
            case R.id.layout_meting:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "会议管理");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=5");
                break;
            case R.id.layout_meal:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "在线点餐");
                intent.putExtra("url", "http://dc.yong-gang.com/members/oa_login?pk=" + app.getUser().getRsbm_pk());
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 获取待办的条数和未读邮件的条数
     */
    private void getTaskSize() {
        Subscriber subscriber = new Subscriber<Mission>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                refresh.onRefreshComplete();
            }

            @Override
            public void onNext(Mission data) {
                if (data.getSuccess() == 0) {
                    int task_sum = data.getData().size();
                    getMailSize(task_sum);
                }
            }
        };
        HttpUtil.getInstance(getActivity(), false).getTask(subscriber, app.getUser().getSessionId());
    }

    private void getMailSize(final int task_size) {
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
                    store.connect(app.getUser().getUser_code(), app.getUser().getMailPassword());

                    // 获得收件箱
                    Folder folder = store.getFolder("INBOX");

                    // 以读写模式打开收件箱
                    folder.open(Folder.READ_ONLY);

                    // 获得收件箱的邮件列表
                    int mail_size = folder.getUnreadMessageCount();

                    Bundle bundle = new Bundle();
                    bundle.putInt("task_size", task_size);
                    bundle.putInt("mail_size", mail_size);

                    Message message = handler.obtainMessage();
                    message.what = 0;
                    message.setData(bundle);
                    message.sendToTarget();

                    store.close();
                } catch (Exception e) {
                    if (refresh != null) {
                        refresh.onRefreshComplete();
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
