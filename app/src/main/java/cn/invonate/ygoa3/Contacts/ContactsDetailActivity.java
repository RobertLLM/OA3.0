package cn.invonate.ygoa3.Contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.RequestLxr;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class ContactsDetailActivity extends BaseActivity {

    Contacts contacts;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.depart)
    TextView depart;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.head_img)
    CircleImageView headImg;
    @BindView(R.id.layout_icon)
    LinearLayout layoutIcon;
    @BindView(R.id.head_text)
    TextView headText;

    YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        contacts = (Contacts) getIntent().getExtras().getSerializable("contacts");
        if (contacts != null) {
            Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + contacts.getUser_photo()).skipMemoryCache(true).error(R.mipmap.pic_head).into(headImg);
            name.setText(contacts.getUser_name());
            num.setText(contacts.getUser_code() == null ? "" : contacts.getUser_code());
            String sex_ = contacts.getSex_();
            if (sex_.equals("1")) {
                sex_ = "男";
            } else if (sex_.equals("0")) {
                sex_ = "女";
            }
            sex.setText(sex_);
            depart.setText(contacts.getDepartment_name() == null ? "" : contacts.getDepartment_name());
            phone.setText(contacts.getUser_phone() == null ? "" : contacts.getUser_phone());
            tel.setText(contacts.getOffice_phone() == null ? "" : contacts.getOffice_phone());
            mail.setText("".equals(contacts.getUser_code()) ? "" : contacts.getUser_code() + "@yong-gang.cn");
            if ("".equals(contacts.getUser_code())) {
                headText.setText(contacts.getUser_name().substring(0, 1));
                headText.setVisibility(View.VISIBLE);
            } else {
                Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + contacts.getUser_photo()).skipMemoryCache(true).error(R.mipmap.pic_head).into(headImg);
            }
        }
        if (getIntent().getExtras().getBoolean("show_layout")) {
            layoutIcon.setVisibility(View.VISIBLE);
        } else {
            layoutIcon.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.pic_back, R.id.layout_message, R.id.layout_friend, R.id.layout_add, R.id.phone, R.id.tel, R.id.mail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;

            case R.id.layout_message:
                Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show();
                break;

            case R.id.layout_friend:
                Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show();
                break;

            case R.id.layout_add:
                add(contacts.getId_());
                break;

            case R.id.phone:
                call(contacts.getUser_phone());
                break;
            case R.id.tel:
                call(contacts.getOffice_phone());
                break;
            case R.id.mail:

                break;
        }
    }

    // 调用系统电话功能
    private void call(final String number) {
        if (ContextCompat.checkSelfPermission(ContactsDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactsDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            AlertDialog alert = new AlertDialog(ContactsDetailActivity.this).builder();
            alert.setPositiveButton("呼叫", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用intent启动拨打电话
                    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).setMsg(number).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call(contacts.getUser_phone());
            }
        } else if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call(contacts.getOffice_phone());
            }
        }
    }

    /**
     * 添加联系人
     */
    private void add(String id) {
        RequestLxr lxr = new RequestLxr(app.getUser().getUser_id(), id);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("add", data);
                JSONObject obj = JSON.parseObject(data);
                Toast.makeText(app, obj.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance(this, false).addToCylxr(new ProgressSubscriber(onNextListener, this, "请稍后"), JSON.toJSONString(lxr));
    }


}
