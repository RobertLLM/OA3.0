package cn.invonate.ygoa3.main.work.mail_new;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.FileNewAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.MailMessage;
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.Entry.MessageContent;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil3;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import cn.invonate.ygoa3.main.LocalPicActivity;
import cn.invonate.ygoa3.main.LocalViewActivity;
import rx.Subscriber;

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

    private String folder;

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

    private int position;//当前邮件位置

    private static final int DELETE_FAIL = 0;
    private static final int DELETE_SUCCESS = 1;

    private YGApplication app;

    private ProgressDialog dialog;

    private void initWebView(WebView view) {
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // User settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 不支持缩放
        webSettings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("lyy", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
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
        switch (mode) {
            case 0:
                folder = "INBOX";
                break;
            case 1:
                folder = "SENT";
                break;
            case 2:
                folder = "DRAFTS";
                break;
            case 3:
                folder = "TRASH";
                break;
        }
        listFiles.setLayoutManager(new LinearLayoutManager(this));
        change_mail(position);
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
                change_mail(++position);
                check_index();
                break;
            case R.id.mail_pre:
                change_mail(--position);
                check_index();
                break;
            case R.id.img_delete:
                AlertDialog dialog = new AlertDialog(this).builder();
                dialog.setTitle("提示")
                        .setMsg("你确定要删除这封邮件吗？")
                        .setPositiveButton("确定", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                int[] ids = new int[]{MailHolder.mailsBeans.get(position).getId()};
                                deleteMail(ids);
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
                ArrayList<MailNew.ResultBean.MailsBean.ReceiveBean> receiver = MailHolder.mailsBeans.get(position).getReceive();
                ArrayList<MailNew.ResultBean.MailsBean.CcBean> copy = MailHolder.mailsBeans.get(position).getCc();
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
        bundle.putSerializable("files", MailHolder.mailsBeans.get(position).getAttachments());
        bundle.putString("folder", folder);
        stepActivity(bundle, SendMailActivity.class);
    }

    /**
     * 切换邮件
     *
     * @param index
     */
    private void change_mail(final int index) {
        initWebView(webContent);
        webContent.setWebViewClient(new YgWebViewClient());
        final MailNew.ResultBean.MailsBean mail = MailHolder.mailsBeans.get(index);
        scContent.scrollTo(0, 0);
        txtSubject.setText("主题：" + (mail.getSubject().equals("") ? "<无主题>" : mail.getSubject()));
        txtFrom.setText("发件人：" + mail.getSender().getUserName());
        txtDate.setText("日期：" + mail.getSentDate());
        webContent.setVisibility(View.VISIBLE);
        webContent.loadDataWithBaseURL(null, mail.getTextContent(), "text/html", "utf-8", null);
        webContent.setWebChromeClient(new WebChromeClient());
        FileNewAdapter adapter = new FileNewAdapter(MailHolder.mailsBeans.get(position).getAttachments(), MailDetailActivity.this);
        adapter.setOnItemClickListener(new FileNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("onItemClick", position + "");
                getAttachments(MailDetailActivity.this.position, position);
            }
        });

        listFiles.setAdapter(adapter);
        // 将邮件置为已读
        getMessage(mail.getId());
    }

    /**
     * 置为已读
     */
    private void getMessage(int id) {
        Subscriber subscriber = new Subscriber<MessageContent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(MessageContent data) {
                Log.i("getMessage", data.toString());
                if (data.getResult() != null) {
                    MailHolder.mailsBeans.get(position).setTextContent(data.getResult().getMail().getTextContent());
                    webContent.loadDataWithBaseURL(null, data.getResult().getMail().getTextContent(), "text/html", "utf-8", null);
                }
            }
        };
        HttpUtil3.getInstance(this, false).getMessage(subscriber, app.getUser().getRsbm_pk(), "", id, folder);
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
        if (position == MailHolder.mailsBeans.size() - 1) {
            mailNext.setClickable(false);
            mailNext.setAlpha(0.5f);
        } else {
            mailNext.setClickable(true);
            mailNext.setAlpha(1f);
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
                    MailHolder.mailsBeans.remove(position);
                    Toast.makeText(app, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil3.getInstance(this, false).appendMailToFolder(new ProgressSubscriber(onNextListener, this, "删除中"), app.getUser().getRsbm_pk(), app.getUser().getRsbm_pk(), msgId, folder);
    }

    /**
     * 获取附件
     *
     * @param mail_position
     * @param file_index
     */
    private void getAttachments(final int mail_position, final int file_index) {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("getAttachments", e.toString());
                Toast.makeText(MailDetailActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String data) {
                Log.i("getAttachments", data);
                String name = MailHolder.mailsBeans.get(mail_position).getAttachments().get(file_index).getFileName();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                if (file.exists()) {
                    file.delete();
                }
                byte[] bytes = Base64.decode(data, Base64.DEFAULT);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    out.write(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MailDetailActivity.this, "转码失败", Toast.LENGTH_SHORT).show();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (isImage(name)) {
                    Intent intent = new Intent(MailDetailActivity.this, LocalPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MailDetailActivity.this, LocalViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        };
        HttpUtil3.getInstance(this, false).getAttachments(subscriber, app.getUser().getRsbm_pk(), app.getUser().getRsbm_pk(), MailHolder.mailsBeans.get(mail_position).getId(), folder, MailHolder.mailsBeans.get(mail_position).getAttachments().get(file_index).getIndex());
    }

    /**
     * 监测是否是图片
     *
     * @param name
     * @return
     */
    private boolean isImage(String name) {
        for (String img : img) {
            String type = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
            if (type.equals(img)) {
                return true;
            }
        }
        return false;
    }

    private static String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};

}
