package cn.invonate.ygoa3.main.work.mail_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ess.filepicker.FilePicker;
import com.ess.filepicker.model.EssFile;
import com.ess.filepicker.util.Const;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.MemberAdapter;
import cn.invonate.ygoa3.Adapter.PhotoNewAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.MailAddress;
import cn.invonate.ygoa3.Entry.MailMessage;
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.Entry.Member;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.View.SwipeItemLayout;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.HttpUtil3;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

public class SendMailActivity extends BaseActivity {

    @BindView(R.id.list_to)
    RecyclerView list_to;
    @BindView(R.id.list_copy)
    RecyclerView list_copy;
    @BindView(R.id.subject)
    EditText edit_subject;
    @BindView(R.id.content)
    WebView web_content;
    @BindView(R.id.list_file)
    RecyclerView listFile;

    private ArrayList<MailNew.ResultBean.MailsBean.AttachmentsBean> photoPaths = new ArrayList<>();

    private PhotoNewAdapter adapter;

    ProgressDialog dialog;

    private MailNew.ResultBean.MailsBean mail;

    private int index = -1;

    private YGApplication app;

    List<Contacts> list_contacts = new ArrayList<>();
    List<Contacts> list_copys = new ArrayList<>();

    private SendMailAdapter input_adapter;
    private SendMailAdapter2 input_adapter2;

    private String content = "";

    private int isReplay = 0;

    private List<Integer> indexes = new ArrayList<>();

    private String folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();

        list_copy.setLayoutManager(new GridLayoutManager(this, 4));
        input_adapter2 = new SendMailAdapter2(list_copys, this);
        list_copy.setAdapter(input_adapter2);

        list_to.setLayoutManager(new GridLayoutManager(this, 4));
        input_adapter = new SendMailAdapter(list_contacts, this);
        list_to.setAdapter(input_adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int mode = getIntent().getExtras().getInt("mode");
            int index = bundle.getInt("mail");
            folder = bundle.getString("folder");
            mail = MailHolder.mailsBeans.get(index);
            switch (mode) {
                case 0:
                    edit();
                    photoPaths = (ArrayList<MailNew.ResultBean.MailsBean.AttachmentsBean>) bundle.getSerializable("files");
                    break;
                case 1:
                    re_send();
                    photoPaths = (ArrayList<MailNew.ResultBean.MailsBean.AttachmentsBean>) bundle.getSerializable("files");
                    break;
                case 2:
                    isReplay = 1;
                    replay();
                    break;
            }
        }

        adapter = new PhotoNewAdapter(photoPaths, this);

        adapter.setOnDeleteItemClickListener(new PhotoNewAdapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(View view, int position) {
                if (photoPaths.get(position).getType() == 0) {
                    indexes.add(photoPaths.get(position).getIndex());
                }
                photoPaths.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        listFile.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        listFile.setLayoutManager(new LinearLayoutManager(this));
        listFile.setAdapter(adapter);

        initWebView(web_content);
        web_content.loadDataWithBaseURL(null, "<html> <body contenteditable=\"true\">" + content + "</body></html>", "text/html", "utf-8", null);
    }

    private void initWebView(WebView view) {
        view.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // User settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
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

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void send(String html) {
            String start = "<body contenteditable=\"true\">";
            String end = "</body>";
            int s = html.indexOf(start) + start.length();
            int e = html.indexOf(end);

            String content = html.substring(s, e);
            Log.i("InJavaScriptLocalObj", "====>html=" + content);

            List<MailAddress.AddressBean> to = new ArrayList<>();
            for (Contacts c : list_contacts) {
                to.add(new MailAddress.AddressBean(c.getUser_name(), c.getUser_code() + "@yong-gang.cn"));
            }
            List<MailAddress.AddressBean> cc = new ArrayList<>();
            for (Contacts c : list_copys) {
                cc.add(new MailAddress.AddressBean(c.getUser_name(), c.getUser_code() + "@yong-gang.cn"));
            }

            String subject = edit_subject.getText().toString().trim();

            RequestBody body_account = RequestBody.create(MediaType.parse("multipart/form-data"), app.getUser().getRsbm_pk());
            RequestBody body_address = RequestBody.create(MediaType.parse("multipart/form-data"), JSON.toJSONString(new MailAddress(to)));
            RequestBody body_cc = cc.isEmpty() ? null : RequestBody.create(MediaType.parse("multipart/form-data"), JSON.toJSONString(new MailAddress(cc)));
            RequestBody body_subject = RequestBody.create(MediaType.parse("multipart/form-data"), subject);
            RequestBody body_context = RequestBody.create(MediaType.parse("multipart/form-data"), content);
            RequestBody body_folder = RequestBody.create(MediaType.parse("multipart/form-data"), "INBOX");

            RequestBody body_ref = mail == null ? null : RequestBody.create(MediaType.parse("multipart/form-data"), mail.getId() + "");
            RequestBody body_isReply = isReplay == 1 ? RequestBody.create(MediaType.parse("multipart/form-data"), 1 + "") : null;
            List<String> files = new ArrayList<>();
            for (MailNew.ResultBean.MailsBean.AttachmentsBean bean : photoPaths) {
                if (bean.getType() != 0) {
                    files.add(bean.getPath());
                }
            }
            RequestBody[] index = null;
            if (!indexes.isEmpty()) {
                index = new RequestBody[indexes.size()];
                for (int i = 0; i < indexes.size(); i++) {
                    index[i] = RequestBody.create(MediaType.parse("multipart/form-data"), indexes.get(i) + "");
                }
            }
            sendMail(body_account,
                    body_address,
                    body_cc,
                    body_subject,
                    body_context,
                    body_ref,
                    index,
                    body_folder,
                    body_isReply,
                    filesToMultipartBodyParts(files)
            );
        }

        @JavascriptInterface
        public void saveDraft(String html) {
            String start = "<body contenteditable=\"true\">";
            String end = "</body>";
            int s = html.indexOf(start) + start.length();
            int e = html.indexOf(end);

            String content = html.substring(s, e);
            Log.i("InJavaScriptLocalObj", "====>saveDraft=" + content);

            List<MailAddress.AddressBean> to = new ArrayList<>();
            for (Contacts c : list_contacts) {
                to.add(new MailAddress.AddressBean(c.getUser_name(), c.getUser_code() + "@yong-gang.cn"));
            }
            List<MailAddress.AddressBean> cc = new ArrayList<>();
            for (Contacts c : list_copys) {
                cc.add(new MailAddress.AddressBean(c.getUser_name(), c.getUser_code() + "@yong-gang.cn"));
            }
            String subject = edit_subject.getText().toString().trim();
            RequestBody body_account = RequestBody.create(MediaType.parse("multipart/form-data"), app.getUser().getRsbm_pk());
            RequestBody body_address = RequestBody.create(MediaType.parse("multipart/form-data"), JSON.toJSONString(new MailAddress(to)));
            RequestBody body_cc = cc.isEmpty() ? null : RequestBody.create(MediaType.parse("multipart/form-data"), JSON.toJSONString(new MailAddress(cc)));
            RequestBody body_subject = RequestBody.create(MediaType.parse("multipart/form-data"), subject);
            RequestBody body_context = RequestBody.create(MediaType.parse("multipart/form-data"), content);

            RequestBody body_folder = folder == null ? null : RequestBody.create(MediaType.parse("multipart/form-data"), folder);
            RequestBody body_ref = mail == null ? null : RequestBody.create(MediaType.parse("multipart/form-data"), mail.getId() + "");
            List<String> files = new ArrayList<>();
            for (MailNew.ResultBean.MailsBean.AttachmentsBean bean : photoPaths) {
                if (bean.getType() != 0) {
                    files.add(bean.getPath());
                }
            }
            RequestBody[] index = null;
            if (!indexes.isEmpty()) {
                index = new RequestBody[indexes.size()];
                for (int i = 0; i < indexes.size(); i++) {
                    index[i] = RequestBody.create(MediaType.parse("multipart/form-data"), indexes.get(i) + "");
                }
            }

            saveToDrafts(body_account,
                    body_address,
                    body_cc,
                    body_subject,
                    body_context,
                    body_ref,
                    body_folder,
                    index,
                    filesToMultipartBodyParts(files)
            );
        }
    }

    /**
     * 转发邮件
     */
    private void re_send() {
        edit_subject.setText("转发：" + mail.getSubject());
        StringBuffer sb = new StringBuffer();
        sb.append("<br>----原始邮件----</br>");
        sb.append("<br>发件人：" + mail.getSender().getUserName() + mail.getSender().getAddress() + "</br>");
        for (int i = 0; i < mail.getReceive().size(); i++) {
            sb.append("<br>");
            if (i == 0)
                sb.append("发件人");
            sb.append(mail.getSender().getUserName() + mail.getSender().getAddress());
            sb.append("</br");
        }
        sb.append("<br>发送时间：" + mail.getSentDate() + "</br>");
        sb.append("<br>主题：" + mail.getSubject() + "</br>");
        sb.append("<br></br>");
        sb.append("<br></br>");
        sb.append("<br></br>");

        Log.i("text", mail.getTextContent());
        sb.append(mail.getTextContent());
        content = sb.toString();
    }

    /**
     * 回复邮件
     */
    private void replay() {
        //转化标题
        MailNew.ResultBean.MailsBean.SenderBean from = mail.getSender();
        String address = from.getAddress().substring(0, from.getAddress().indexOf("@"));
        list_contacts.add(new Contacts(from.getUserName(), address));
        edit_subject.setText("回复：" + mail.getSubject());
    }

    /**
     * 编辑
     */
    private void edit() {
        List<MailNew.ResultBean.MailsBean.ReceiveBean> list_receiver = mail.getReceive();
        for (MailNew.ResultBean.MailsBean.ReceiveBean bean : list_receiver) {
            list_contacts.add(new Contacts(bean.getUserName(), bean.getAddress()));
        }
        List<MailNew.ResultBean.MailsBean.CcBean> list_cc = mail.getCc();
        for (MailNew.ResultBean.MailsBean.CcBean bean : list_cc) {
            list_copys.add(new Contacts(bean.getUserName(), bean.getAddress()));
        }

        edit_subject.setText(mail.getSubject());
        content = mail.getTextContent();
    }

    @OnClick({R.id.img_back, R.id.send, R.id.to_add, R.id.address_add, R.id.btn_camera, R.id.btn_gallary, R.id.btn_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                AlertDialog alertDialog = new AlertDialog(this).builder();
                alertDialog.setTitle("提示")
                        .setMsg("是否保存为草稿？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        web_content.loadUrl("javascript:window.java_obj.saveDraft("
                                + "document.getElementsByTagName('html')[0].innerHTML);");
                    }
                }).show();
                break;
            case R.id.send:
                if (list_contacts.isEmpty()) {
                    Toast.makeText(app, "请选择收件人", Toast.LENGTH_SHORT).show();
                    return;
                }
                web_content.loadUrl("javascript:window.java_obj.send("
                        + "document.getElementsByTagName('html')[0].innerHTML);");
                break;
            case R.id.to_add:
                Intent intent = new Intent(SendMailActivity.this, SelectDepartmentActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.address_add:
                Intent intent2 = new Intent(SendMailActivity.this, SelectDepartmentActivity.class);
                startActivityForResult(intent2, 0x321);
                break;
            case R.id.btn_camera:
                ArrayList<String> paths = new ArrayList<>();
                for (MailNew.ResultBean.MailsBean.AttachmentsBean obj : photoPaths) {
                    if (obj.getType() == 0) {
                        paths.add(obj.getPath());
                    }
                }
                PhotoPicker.builder()
                        .setPhotoCount(10)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .setSelected(paths)
                        .start(this);
                break;
            case R.id.btn_gallary:
                ArrayList<String> paths_gallary = new ArrayList<>();
                for (MailNew.ResultBean.MailsBean.AttachmentsBean obj : photoPaths) {
                    if (obj.getType() == 0) {
                        paths_gallary.add(obj.getPath());
                    }
                }
                PhotoPicker.builder()
                        .setPhotoCount(10)
                        .setShowCamera(false)
                        .setPreviewEnabled(true)
                        .setSelected(paths_gallary)
                        .start(this);
                break;
            case R.id.btn_file:
                ActionSheetDialog action = new ActionSheetDialog(this).builder();
                action.setTitle("请选择方式")
                        .addSheetItem("按文件类型选择", ActionSheetDialog.SheetItemColor.Black, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                FilePicker
                                        .from(SendMailActivity.this)
                                        .chooseForMimeType()
                                        .setMaxCount(1)
                                        .setFileTypes("doc", "xls", "ppt", "pdf", "apk", "mp3", "gif", "txt", "mp4", "zip", "rar")
                                        .requestCode(0x999)
                                        .start();
                            }
                        }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            photoPaths.clear();
            if (photos != null) {
                Log.i("photos", photos.toString());
//                photoPaths.addAll(photos);
                for (String path : photos) {
                    File file = new File(path);
                    MailNew.ResultBean.MailsBean.AttachmentsBean bean = new MailNew.ResultBean.MailsBean.AttachmentsBean(file.getName(), (file.length() / 1024), 0, path);
                    photoPaths.add(bean);
                }
            }
            adapter.notifyDataSetChanged();
        } else if (requestCode == 0x123) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    List<Contacts> list = (List<Contacts>) bundle.getSerializable("list");
                    for (Contacts c : list) {
                        if (!check(list_contacts, c)) {
                            list_contacts.add(c);
                        }
                    }
                }
            }
            input_adapter.notifyDataSetChanged();
        } else if (requestCode == 0x321) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    List<Contacts> list = (List<Contacts>) bundle.getSerializable("list");
                    for (Contacts c : list) {
                        if (!check(list_copys, c)) {
                            list_copys.add(c);
                        }
                    }

                }
            }
            input_adapter2.notifyDataSetChanged();
        } else if (requestCode == 0x999 && resultCode == RESULT_OK) {
            ArrayList<EssFile> files = data.getParcelableArrayListExtra(Const.EXTRA_RESULT_SELECTION);
            Log.i("files", JSON.toJSONString(files));
            if (!files.isEmpty()) {
                String path = files.get(0).getAbsolutePath();
                File file = new File(path);
                MailNew.ResultBean.MailsBean.AttachmentsBean bean = new MailNew.ResultBean.MailsBean.AttachmentsBean(file.getName(), file.length() / 1024, 1, path);
                photoPaths.add(bean);
            } else {
                Toast.makeText(app, "未找到该文件", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 去除重复添加的人员
     *
     * @param list
     * @param c
     * @return
     */
    private boolean check(List<Contacts> list, Contacts c) {
        for (Contacts e : list) {
            if (e.getUser_code().equals(c.getUser_code()))
                return true;
        }
        return false;
    }

    class TextWatch implements TextWatcher {
        public AutoCompleteTextView textView;
        public List<Contacts> data;

        public TextWatch(AutoCompleteTextView textView, List<Contacts> data) {
            this.textView = textView;
            this.data = data;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getPerson(textView.getText().toString(), textView, data);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 获取通讯录人
     *
     * @param keyword
     * @param textView
     * @param list_data
     */
    private void getPerson(String keyword, final AutoCompleteTextView textView, final List<Contacts> list_data) {
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
                MemberAdapter adapter = new MemberAdapter(data.getRows(), SendMailActivity.this);
                adapter.setOnItemClickListener(new MemberAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        list_data.add(data.getRows().get(position));
                        Log.i("list_data", JSON.toJSONString(list_data));
                        input_adapter.notifyDataSetChanged();
                    }
                });
                textView.setAdapter(adapter);
            }
        };

        HttpUtil.getInstance(this, false).getMembers(subscriber, keyword, 1, 10000);
    }


    /**
     * 联系人适配
     */
    class SendMailAdapter extends RecyclerView.Adapter<SendMailAdapter.ViewHolder> {
        private static final int TYPE_NOMAL = 0;
        private static final int TYPE_INOPUT = 1;

        List<Contacts> data;
        private Context context;

        public SendMailAdapter(List<Contacts> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public SendMailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = null;
            switch (viewType) {
                case TYPE_NOMAL:
                    view = LayoutInflater.from(context).inflate(R.layout.item_send_mail, parent, false);
                    break;
                case TYPE_INOPUT:
                    view = LayoutInflater.from(context).inflate(R.layout.item_send_input, parent, false);
                    break;
            }
            return new SendMailAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SendMailAdapter.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_NOMAL:
                    holder.name.setText(data.get(position).getUser_name());
                    break;
                case TYPE_INOPUT:
                    holder.address.setText("");
                    holder.address.addTextChangedListener(new TextWatch(holder.address, list_contacts));
                    holder.address.requestFocus();
                    holder.address.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (holder.address.getText().toString().trim().length() < 1) {
                                        if (!data.isEmpty()) {
                                            data.remove(data.size() - 1);
                                            input_adapter.notifyDataSetChanged();
                                        }
                                        Log.d("keyCode", data.size() + "");
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == data.size()) {
                return TYPE_INOPUT;
            } else {
                return TYPE_NOMAL;
            }
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Nullable
            @BindView(R.id.name)
            TextView name;

            @Nullable
            @BindView(R.id.address)
            AutoCompleteTextView address;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    /**
     * 抄送人适配
     */
    class SendMailAdapter2 extends RecyclerView.Adapter<SendMailAdapter2.ViewHolder> {
        private static final int TYPE_NOMAL = 0;
        private static final int TYPE_INOPUT = 1;

        List<Contacts> data;
        private Context context;

        public SendMailAdapter2(List<Contacts> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public SendMailAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = null;
            switch (viewType) {
                case TYPE_NOMAL:
                    view = LayoutInflater.from(context).inflate(R.layout.item_send_mail, parent, false);
                    break;
                case TYPE_INOPUT:
                    view = LayoutInflater.from(context).inflate(R.layout.item_send_input, parent, false);
                    break;

            }
            return new SendMailAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SendMailAdapter2.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_NOMAL:
                    holder.name.setText(data.get(position).getUser_name());
                    break;
                case TYPE_INOPUT:
                    holder.address.setText("");
                    holder.address.addTextChangedListener(new TextWatch(holder.address, list_copys));
                    holder.address.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (holder.address.getText().toString().trim().length() < 1) {
                                        if (!data.isEmpty()) {
                                            data.remove(data.size() - 1);
                                            input_adapter2.notifyDataSetChanged();
                                        }
                                        Log.d("keyCode", data.size() + "");
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == data.size()) {
                return TYPE_INOPUT;
            } else {
                return TYPE_NOMAL;
            }
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Nullable
            @BindView(R.id.name)
            TextView name;

            @Nullable
            @BindView(R.id.address)
            AutoCompleteTextView address;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    /**
     * 发送邮件
     */
    private void sendMail(
            RequestBody account,
            RequestBody address,
            RequestBody cc,
            RequestBody subject,
            RequestBody context,
            RequestBody ref,
            RequestBody[] index,
            RequestBody folder,
            RequestBody isReply,
            List<MultipartBody.Part> parts) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MailMessage>() {
            @Override
            public void onNext(MailMessage data) {
                Log.i("sendMail", data.toString());
                if (data.getCode().equals("0000")) {
                    finish();
                    Toast.makeText(app, "发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil3.getInstance(this, false).sendMail(new ProgressSubscriber(onNextListener, this, "发送中"),
                app.getUser().getRsbm_pk(),
                account,
                address,
                subject,
                context,
                cc,
                ref,
                index,
                folder,
                isReply,
                parts
        );
    }

    /**
     *
     */

    private void saveToDrafts(RequestBody account,
                              RequestBody address,
                              RequestBody cc,
                              RequestBody subject,
                              RequestBody context,
                              RequestBody ref,
                              RequestBody folder,
                              RequestBody[] index,
                              List<MultipartBody.Part> parts) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MailMessage>() {
            @Override
            public void onNext(MailMessage data) {
                Log.i("saveToDrafts", data.toString());
                if (data.getCode().equals("0000")) {
                    finish();
                    Toast.makeText(app, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil3.getInstance(this, false).saveToDrafts(new ProgressSubscriber(onNextListener, this, "保存中"),
                app.getUser().getRsbm_pk(),
                account,
                address,
                cc,
                subject,
                context,
                ref,
                folder,
                index,
                parts
        );
    }

    /**
     * 将文件转化成MultipartBody.Part
     *
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (String path : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/*"), new File(path));
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", new File(path).getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


}
