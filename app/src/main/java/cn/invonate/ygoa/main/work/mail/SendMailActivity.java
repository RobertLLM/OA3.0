package cn.invonate.ygoa.main.work.mail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.Adapter.PhotoAdapter;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.FileEntry;
import cn.invonate.ygoa.Entry.Mail;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.Domain;
import cn.invonate.ygoa.Util.MailHolder;
import cn.invonate.ygoa.Util.Mails;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class SendMailActivity extends BaseActivity {

    @BindView(R.id.to)
    EditText edit_to;
    @BindView(R.id.address)
    EditText edit_address;
    @BindView(R.id.from)
    TextView edit_from;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("发送中");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int index = bundle.getInt("mail");
            mail = MailHolder.mail_model.get(index);
            re_send(mail);
            // 赋初值
            setInit();
        }
        adapter = new PhotoAdapter(photoPaths, this);
        listFile.setLayoutManager(new LinearLayoutManager(this));
        listFile.setAdapter(adapter);
    }

    /**
     * 转发邮件
     *
     * @param mail
     */
    private void re_send(Mail mail) {
        //转化标题
        mail.setSubject("转：" + mail.getSubject());
        //转化内容
        String content =
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "-----原始邮件-----" +
                "\r\n" +
                "发件人：" + mail.getFrom() +
                "\r\n" +
                "收件人：" + mail.getReceiver() +
                "\r\n" +
                "发送时间：" + mail.getSend_date() +
                "\r\n" +
                "主题：" + mail.getSubject() +
                "\r\n" +
                mail.getContent() +
                "\r\n";
        mail.setContent(content);
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
                        to.add(edit_to.getText().toString());
                        send_email(to, null, null, edit_subject.getText().toString().trim(), edit_content.getText().toString().trim(), true);
                    }
                }).show();
                break;
            case R.id.send:
                dialog.show();
                List<String> to = new ArrayList<>();
                to.add(edit_to.getText().toString());
                send_email(to, null, null, edit_subject.getText().toString().trim(), edit_content.getText().toString().trim(), false);
                break;
            case R.id.to_add:
                break;
            case R.id.address_add:
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
        }
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
        if ("".equals(subject)) {
            Toast.makeText(this, "请输入主题", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(body)) {
            Toast.makeText(this, "请输入正文", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mail != null) {
            body = body +
                    "\r\n" +
                    "\r\n" +
                    "\r\n" +
                    mail.getContent();
        }
        Mails email = new Mails(Domain.user_mail + "yong-gang.cn", to, cc, bcc, subject, body);
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
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "发送失败",
                        Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}
