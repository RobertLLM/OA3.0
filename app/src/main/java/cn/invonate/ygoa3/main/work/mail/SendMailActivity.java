package cn.invonate.ygoa3.main.work.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ess.filepicker.FilePicker;
import com.ess.filepicker.model.EssFile;
import com.ess.filepicker.util.Const;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.MemberAdapter;
import cn.invonate.ygoa3.Adapter.PhotoAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.FileEntry;
import cn.invonate.ygoa3.Entry.Mail;
import cn.invonate.ygoa3.Entry.Member;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.Util.Mails;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import rx.Subscriber;

public class SendMailActivity extends BaseActivity {

    @BindView(R.id.list_to)
    RecyclerView list_to;
    @BindView(R.id.list_copy)
    RecyclerView list_copy;
    @BindView(R.id.subject)
    EditText edit_subject;
    @BindView(R.id.content)
    EditText edit_content;
    @BindView(R.id.list_file)
    RecyclerView listFile;

    private ArrayList<Object> photoPaths = new ArrayList<>();

    private PhotoAdapter adapter;

    ProgressDialog dialog;

    private Mail mail;
    private int index;

    private YGApplication app;

    List<Contacts> list_contacts = new ArrayList<>();
    List<Contacts> list_copys = new ArrayList<>();

    private SendMailAdapter input_adapter;
    private SendMailAdapter2 input_adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("发送中");

        adapter = new PhotoAdapter(photoPaths, this);
        listFile.setLayoutManager(new LinearLayoutManager(this));
        listFile.setAdapter(adapter);

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
            mail = MailHolder.mail_model.get(index);
            switch (mode) {
                case 0:
                    edit(mail);
                    break;
                case 1:
                    re_send(mail);
                    break;
                case 2:
                    replay(mail);
                    break;
            }
        }
    }

    /**
     * 转发邮件
     *
     * @param mail
     */
    private void re_send(Mail mail) {
        setInit();
    }

    /**
     * 回复邮件
     *
     * @param mail
     */
    private void replay(Mail mail) {
        //转化标题
        String from = mail.getFrom();
        if (from.contains("@")) {
            String[] address = from.split("@");
            if (address.length > 1) {
                list_contacts.add(new Contacts(mail.getPersonal(), address[0]));
                if (input_adapter != null) {
                    input_adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 编辑
     *
     * @param mail
     */
    private void edit(Mail mail) {
        //设置收件人
        setInit();
    }


    private void setInit() {
        edit_subject.setText(mail.getSubject());
        edit_content.setText(mail.getContent());
        photoPaths.addAll(mail.getFiles());
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
                        dialog.show();
                        List<String> to = new ArrayList<>();
                        send_email(to, null, null, edit_subject.getText().toString().trim(), edit_content.getText().toString().trim(), true);
                    }
                }).show();
                break;
            case R.id.send:
                List<String> to = new ArrayList<>();
                for (Contacts c : list_contacts) {
                    to.add(c.getUser_name() + "<" + c.getUser_code() + "@yong-gang.cn>");
                }
                List<String> cc = new ArrayList<>();
                for (Contacts c : list_copys) {
                    cc.add(c.getUser_name() + "<" + c.getUser_code() + "@yong-gang.cn>");
                }
                send_email(to, cc, null, edit_subject.getText().toString().trim(), edit_content.getText().toString().trim(), false);
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
                for (Object obj : photoPaths) {
                    if (obj instanceof String) {
                        paths.add((String) obj);
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
                for (Object obj : photoPaths) {
                    if (obj instanceof String) {
                        paths_gallary.add((String) obj);
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
//                FilePicker
//                        .from(this)
//                        .chooseForMimeType()
//                        .setMaxCount(1)
//                        .setFileTypes("doc", "xls", "ppt", "pdf", "apk", "mp3", "gif", "txt", "mp4", "zip")
//                        .requestCode(0x999)
//                        .start();
                ActionSheetDialog action = new ActionSheetDialog(this).builder();
                action.setTitle("请选择方式")
                        .addSheetItem("按路径选择", ActionSheetDialog.SheetItemColor.Black, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                FilePicker
                                        .from(SendMailActivity.this)
                                        .chooseForBrowser()
                                        .setMaxCount(1)
                                        .requestCode(0x999)
                                        .start();
                            }
                        })
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
                photoPaths.addAll(photos);
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
            photoPaths.add(files.get(0).getAbsolutePath());
            adapter.notifyDataSetChanged();
        }

    }

    private boolean check(List<Contacts> list, Contacts c) {
        for (Contacts e : list) {
            if (e.getUser_code().equals(c.getUser_code()))
                return true;
        }
        return false;
    }

    /**
     * 发送邮件
     *
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     */
    private void send_email(List<String> to, List<String> cc, List<String> bcc, String subject, String body, boolean is_draft) {
        if (to.size() == 0) {
            Toast.makeText(this, "请输入收件人", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        if (mail != null) {
            body = body +
                    "\r\n" +
                    "\r\n" +
                    "\r\n" +
                    mail.getContent();
        }
        if (body == null) {
            body = "";
        }
        if (subject == null) {
            subject = "";
        }
        Mails email = new Mails(app, app.getUser().getUser_code() + "@yong-gang.cn", app.getUser().getUser_name(), to, cc, bcc, subject, body);
        try {
            for (int i = 0; i < photoPaths.size(); i++) {
                Object obj = photoPaths.get(i);
                if (obj instanceof String) {
                    File file = new File((String) photoPaths.get(i));
                    email.addAttachment(file);
                } else {
                    FileEntry file = (FileEntry) obj;
                    email.addAttachment(file);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        new SendTask(email, is_draft).execute();
    }

    /**
     * 异步任务处理邮件发送操作
     */
    class SendTask extends AsyncTask<Integer, Integer, String> {
        private Mails mails;
        private boolean is_draft;

        public SendTask(Mails mails, boolean is_draft) {
            this.mails = mails;
            this.is_draft = is_draft;
        }

        @Override
        protected void onPreExecute() {
            // 第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String isok = "";
            try {
                // m.addAttachment("/sdcard/filelocation");
                if (mails.send(is_draft)) {
                    isok = "ok";
                } else {
                    isok = "no";
                }
            } catch (Exception e) {
                Log.e("wxl", "Could not send email", e);
            }
            return isok;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            // doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
            // 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
            // setTitle(result);
            dialog.dismiss();
            if ("ok".equals(result)) {
                Toast.makeText(getApplicationContext(), "发送成功",
                        Toast.LENGTH_SHORT).show();
                String code = "";
                for (Contacts c : list_contacts) {
                    code += c.getUser_code() + ",";
                }
                for (Contacts c : list_copys) {
                    code += c.getUser_code() + ",";
                }
                code = code.substring(0, code.length() - 1);//去除最后一个逗号
                Log.i("code", code);
                push("您有一封新的未读邮件", code);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "发送失败",
                        Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


    private void push(String msg, String code) {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(String data) {
                Log.i("push", data);
            }
        };
        HttpUtil.getInstance(this, false).push(subscriber, msg, code);
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

        void request() {

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
                                    Log.d("keyCode", "6666666666666666666666666666666");
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
     * 联系人适配
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


}
