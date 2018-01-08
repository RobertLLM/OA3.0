package cn.invonate.ygoa.Contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.Contacts;
import cn.invonate.ygoa.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ButterKnife.bind(this);
        contacts = (Contacts) getIntent().getExtras().getSerializable("contacts");
        if (contacts != null) {
            name.setText(contacts.getUser_name());
            num.setText(contacts.getUser_code());
            sex.setText(contacts.getSex_());
            depart.setText(contacts.getDepartment_name());
            phone.setText(contacts.getUser_phone());
            tel.setText(contacts.getOffice_phone());
            mail.setText(contacts.getUser_code() + "@yong-gang.cn");
        }
    }

    @OnClick({R.id.pic_back, R.id.layout_tel, R.id.layout_message, R.id.layout_mail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                break;
            case R.id.layout_tel:
                ActionSheetDialog dialog = new ActionSheetDialog(this).builder();
                if (contacts.getUser_phone() != null && !"".equals(contacts.getUser_phone())) {
                    dialog.addSheetItem(contacts.getUser_phone(), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int wh) {
                            AlertDialog alert=new AlertDialog(ContactsDetailActivity.this).builder();
                            alert.setPositiveButton("呼叫", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    call(contacts.getUser_phone());
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setMsg(contacts.getUser_phone()).show();
                        }
                    });
                }
                if (contacts.getOffice_phone() != null && !"".equals(contacts.getOffice_phone())) {
                    dialog.addSheetItem(contacts.getOffice_phone(), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            AlertDialog alert=new AlertDialog(ContactsDetailActivity.this).builder();
                            alert.setPositiveButton("呼叫", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    call(contacts.getOffice_phone());
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setMsg(contacts.getOffice_phone()).show();
                        }
                    });
                }
                dialog.setTitle("请选择联系方式").show();
                break;
            case R.id.layout_message:
                break;
            case R.id.layout_mail:
                break;
        }
    }

    // 调用系统电话功能
    private void call(String number) {
        //用intent启动拨打电话
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
