package cn.invonate.ygoa3.main.work.mail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.protocol.FLAGS;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.FileAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.FileEntry;
import cn.invonate.ygoa3.Entry.Mail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.Domain;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.Util.POP3ReceiveMailTest;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.main.BytePicActivity;
import cn.invonate.ygoa3.main.LocalViewActivity;

public class MailDetailActivity extends BaseActivity {

    private static final int MODE_INBOEX = 0;
    private static final int MODE_SENT = 1;
    private static final int MODE_DRAFT = 2;
    private static final int MODE_TRASH = 3;
    @BindView(R.id.list_files)
    RecyclerView listFiles;
    @BindView(R.id.sc_content)
    ScrollView scContent;

    private int mode = -1;

    @BindView(R.id.txt_subject)
    TextView txtSubject;
    @BindView(R.id.txt_from)
    TextView txtFrom;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.web_content)
    WebView webContent;
    @BindView(R.id.mail_next)
    ImageView mailNext;
    @BindView(R.id.mail_pre)
    ImageView mailPre;

    //private ArrayList<Mail> list_mail;

    private int position;//当前邮件位置

    private static final int DELETE_FAIL = 0;
    private static final int DELETE_SUCCESS = 1;

    private YGApplication app;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELETE_SUCCESS:
                    finish();
                    break;
                case DELETE_FAIL:
                    Toast.makeText(MailDetailActivity.this, "删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static final int SHOW_DIALOG = 0;
    public static final int DISMISS_DIALOG = 1;

    private ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler contentHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG:
                    dialog.show();
                    break;
                case DISMISS_DIALOG:
                    dialog.dismiss();
                    change_mail(position);
                    break;
            }
        }
    };

    private void initWebView(WebView view) {
        //scContent=findViewById(R.id.sc_content);
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // User settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(false); // 不支持缩放
        webSettings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("lyy", "densityDpi = " + mDensity);
//        if (mDensity == 240) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        } else if (mDensity == 160) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        } else if (mDensity == 120) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
//            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        } else {
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
//        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        position = getIntent().getExtras().getInt("position");
        mode = getIntent().getExtras().getInt("mode");
        listFiles.setLayoutManager(new LinearLayoutManager(this));
        setContent(position);
        check_index();
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //加载失败
        }
    }

    @OnClick({R.id.img_back, R.id.mail_next, R.id.mail_pre, R.id.img_delete, R.id.img_resend, R.id.txt_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.mail_next:
                setContent(++position);
                check_index();
                break;
            case R.id.mail_pre:
                setContent(--position);
                check_index();
                break;
            case R.id.img_delete:
                AlertDialog dialog = new AlertDialog(this).builder();
                dialog.setTitle("提示")
                        .setMsg("你确定要删除这封邮件吗？")
                        .setPositiveButton("确定", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                delete_single_mail(MailHolder.mails.get(position));
                            }
                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.img_resend:
                ActionSheetDialog dialog2 = new ActionSheetDialog(this).builder();
                dialog2.setTitle("请选择操作");
                switch (mode) {
                    case MODE_INBOEX:
                    case MODE_TRASH:
                        dialog2.addSheetItem("回复", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepSend(2);
                            }
                        });
                        dialog2.addSheetItem("转发", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepSend(1);
                            }
                        }).show();
                        break;
                    case MODE_SENT:
                        dialog2.addSheetItem("编辑", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepSend(0);
                            }
                        }).show();
                        dialog2.addSheetItem("转发", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepSend(1);
                            }
                        }).show();
                        break;
                    case MODE_DRAFT:
                        dialog2.addSheetItem("编辑", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stepSend(0);
                            }
                        }).show();
                        break;
                }
                break;
            case R.id.txt_info:
                Bundle bundle = new Bundle();
                ArrayList<Mail.Address> receiver = MailHolder.mail_model.get(position).getReceiver();
                ArrayList<Mail.Address> copy = MailHolder.mail_model.get(position).getCopy();
                bundle.putSerializable("receiver", receiver);
                bundle.putSerializable("copy", copy);
                stepActivity(bundle, ContactsListActivity.class);
                break;
        }
    }

    /**
     * @param mode 0:编辑 1:转发 2:回复
     */
    private void stepSend(int mode) {
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        bundle.putInt("mail", position);
        stepActivity(bundle, SendMailActivity.class);
    }

    private void setContent(final int index) {
        if (MailHolder.mail_model.get(index).getContent() == null) {
            contentHandler.sendEmptyMessage(SHOW_DIALOG);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Folder folder = MailHolder.folder;
                        // 以读写模式打开收件箱
                        if (!folder.getStore().isConnected()) {
                            folder.getStore().connect();
                        }
                        if (folder.isOpen()) {
                            folder.close(true);
                        }
                        folder.open(Folder.READ_WRITE);
                        POP3ReceiveMailTest.setContent(MailHolder.mails.get(index), MailHolder.mail_model.get(index));
                        contentHandler.sendEmptyMessage(DISMISS_DIALOG);
                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            contentHandler.sendEmptyMessage(DISMISS_DIALOG);
        }
    }

    /**
     * 切换邮件
     *
     * @param index
     */
    private void change_mail(final int index) {
        initWebView(webContent);
        webContent.setWebViewClient(new YgWebViewClient());
        Mail mail = MailHolder.mail_model.get(index);
        final ArrayList<FileEntry> files = new ArrayList<>();
        scContent.scrollTo(0, 0);
        txtSubject.setText("主题：" + mail.getSubject());
        txtFrom.setText("发件人：" + mail.getPersonal());
        txtDate.setText("日期：" + mail.getSend_date());
        webContent.setVisibility(View.VISIBLE);
        webContent.loadDataWithBaseURL(null, mail.getContent(), "text/html", "utf-8", null);
        webContent.setWebChromeClient(new WebChromeClient());
        get_single_mail(MailHolder.mails.get(position));
        for (int i = 0; i < mail.getAttachments().size(); i++) {
            FileEntry entry = IOToFile(mail.getAttachments().get(i), mail.getAttachmentsInputStreams().get(i), mail.getFile_size().get(i));
            Log.i("附件内容", entry.toString());
            files.add(entry);
        }
        MailHolder.mail_model.get(index).setFiles(files);
        final FileAdapter adapter = new FileAdapter(files, this);
        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                if (adapter.getItemViewType(position) == FileAdapter.TYPE_IMG) {
                    bundle.putInt("index", index);
                    bundle.putInt("position", position);
                    stepActivity(bundle, BytePicActivity.class);
                } else {
                    String name = files.get(position).getName();
//                    File file = MyUtils.getCacheFile(name, MailDetailActivity.this);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                    if (file.exists()) {
                        bundle.putString("path", name);
                        stepActivity(bundle, LocalViewActivity.class);
                    } else {
                        OutputStream output = null;
                        try {
                            output = new FileOutputStream(file);
                            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                            bufferedOutput.write(files.get(position).getIs());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bundle.putString("path", name);
                        stepActivity(bundle, LocalViewActivity.class);
                    }
                }

            }
        });
        listFiles.setAdapter(adapter);
    }

    private FileEntry IOToFile(String name, byte[] is, int size) {
        FileEntry entry = new FileEntry();
        String fileType = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        entry.setName(name);
        entry.setType(fileType);
        entry.setIs(is);
        entry.setSize(size);
        return entry;
    }

    /**
     * 将邮件设置已读
     *
     * @param message 要查看的邮件
     */
    private void get_single_mail(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                try {
                    Folder folder = MailHolder.folder;
                    // 以读写模式打开收件箱
                    if (folder.isOpen()) {
                        folder.close(true);
                    }
                    folder.open(Folder.READ_WRITE);
                    if (!message.isSet(Flags.Flag.SEEN)) {
                        message.setFlag(Flags.Flag.SEEN, true);
                    }
                    result = true;
                    folder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                Log.i("seen", result + "");
            }
        }).start();
    }

    /**
     * 删除邮件
     *
     * @param message 要删除的邮件
     */
    private void delete_single_mail(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                try {
                    Folder folder = MailHolder.folder;
                    // 以读写模式打开收件箱
                    if (folder.isOpen()) {
                        folder.close(true);
                    }
                    folder.open(Folder.READ_WRITE);
                    if (!message.isSet(Flags.Flag.DELETED)) {
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                    if (mode != MODE_TRASH) {
                        save_to_trash(message);//若模式不是已删除，则复制一份存入垃圾箱
                    }
                    folder.close(true);
                    result = true;
                } catch (MessagingException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendEmptyMessage(result ? DELETE_SUCCESS : DELETE_FAIL);
                }
                Log.i("delete", result + "");
            }
        }).start();
    }

    /**
     * 邮件添加到垃圾箱
     *
     * @param msg
     * @throws MessagingException
     */
    private void save_to_trash(Message msg) throws MessagingException {
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", Domain.MAIL_URL);
        props.setProperty("mail.imap.port", Domain.MAIL_PORT);

        // 创建Session实例对象
        Session session = Session.getInstance(props);

        // 创建IMAP协议的Store对象
        Store store = session.getStore("imap");
        store.connect(app.getUser().getUser_code(), app.getUser().getMailPassword());
        Folder folder = store.getFolder("Trash");

        folder.open(Folder.READ_WRITE); //打开垃圾箱

        Message[] msgs = {msg};

        folder.appendMessages(msgs);

        msg.setFlag(FLAGS.Flag.DELETED, true);

        folder.close(true);
    }

    /**
     * 监测position位置
     */
    private void check_index() {
        if (position == 0) {
            mailPre.setClickable(false);
            mailPre.setAlpha(0.5f);
        } else {
            mailPre.setClickable(true);
            mailPre.setAlpha(1f);
        }
        if (position == MailHolder.mail_model.size() - 1) {
            mailNext.setClickable(false);
            mailNext.setAlpha(0.5f);
        } else {
            mailNext.setClickable(true);
            mailNext.setAlpha(1f);
        }
    }


}
