package cn.invonate.ygoa3.Contacts;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.LocalContactsAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.LocalContacts;
import cn.invonate.ygoa3.Entry.Request_Contacts;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.pingyin.CharacterParser;
import cn.invonate.ygoa3.Util.pingyin.ClearEditText;
import cn.invonate.ygoa3.Util.pingyin.PinyinComparator3;
import cn.invonate.ygoa3.Util.pingyin.SideBar;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class SelectLocalContactsActivity extends BaseActivity {

    private final int UPDATE_LIST = 1;
    ArrayList<LocalContacts> contactsList = new ArrayList<>(); //得到的所有联系人
    ArrayList<LocalContacts> getcontactsList = new ArrayList<>(); //选择得到联系人

    @BindView(R.id.list_connect)
    ListView listConnect;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidebar)
    SideBar sideBar;

    private ProgressDialog proDialog;

    ClearEditText filterEdit;

    private LocalContactsAdapter adapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator3 pinyinComparator;

    private Thread getcontacts;

    private YGApplication app;

    @SuppressLint("HandlerLeak")
    Handler updateListHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LIST:
                    if (proDialog != null) {
                        proDialog.dismiss();
                    }
                    updateList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_local_contacts);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator3();

        View view = LayoutInflater.from(this).inflate(R.layout.item_input, null);
        filterEdit = view.findViewById(R.id.filter_edit);
        listConnect.addHeaderView(view);
        sideBar.setTextView(dialog);

        getcontacts = new Thread(new GetContacts());
        getcontacts.start();
        proDialog = ProgressDialog.show(SelectLocalContactsActivity.this, "通讯录", "获取联系人", true, true);
    }

    private void updateList() {
        filledData(contactsList);
        Collections.sort(contactsList, pinyinComparator);
        adapter = new LocalContactsAdapter(contactsList, SelectLocalContactsActivity.this);
        listConnect.setAdapter(adapter);
        listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.getData().get(position - 1).setIs_select(!adapter.getData().get(position - 1).isIs_select());
                adapter.notifyDataSetChanged();
            }
        });

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listConnect.setSelection(position);
                }
            }
        });
        //根据输入框输入值的改变来过滤搜索
        filterEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString(), contactsList);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_sure:
                List<LocalContacts> data = new ArrayList<>();
                for (LocalContacts c : contactsList) {
                    if (c.isIs_select()) {
                        data.add(c);
                    }
                }
                if (data.isEmpty()) {
                    Toast.makeText(this, "请至少选择一个联系人", Toast.LENGTH_SHORT).show();
                } else {
                    saveContacts(data);
                }
                break;
        }
    }

    /**
     * 上传手机通讯录
     *
     * @param data
     */
    private void saveContacts(List<LocalContacts> data) {
        Request_Contacts rc = new Request_Contacts(data, app.getUser().getUser_id());
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("saveContacts", data);
                finish();
                Intent intent = new Intent();
                intent.setAction("refresh");
                sendBroadcast(intent);
            }
        };
        HttpUtil.getInstance(this, false).save_contacts(new ProgressSubscriber(onNextListener, this, "上传中"), JSON.toJSONString(rc));
    }

    /**
     * 获取本地联系人
     */
    class GetContacts implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            String[] projection = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID
            };
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
            Cursor cursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
            Cursor phonecur = null;
            while (cursor.moveToNext()) {
                // 取得联系人名字
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String name = cursor.getString(nameFieldColumnIndex);
                // 取得联系人ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                phonecur = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                // 取得电话号码(可能存在多个号码)
                while (phonecur.moveToNext()) {
                    String strPhoneNumber = phonecur.getString(phonecur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (strPhoneNumber.length() > 4)
                        // contactsList.add("18610011001"+"\n测试");
                        contactsList.add(new LocalContacts(name, strPhoneNumber, ""));
                }
            }
            if (phonecur != null)
                phonecur.close();
            cursor.close();
            Message msg1 = new Message();
            msg1.what = UPDATE_LIST;
            updateListHandler.sendMessage(msg1);
        }
    }

    /**
     * 添加首字母
     *
     * @param data
     */
    private void filledData(List<LocalContacts> data) {
        for (int i = 0; i < data.size(); i++) {
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(data.get(i).getUser_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                data.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                data.get(i).setSortLetters("#");
            }
        }
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr, List<LocalContacts> data) {
        List<LocalContacts> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = data;
        } else {
            filterDateList.clear();
            for (LocalContacts contacts : data) {
                String name = contacts.getUser_name();
                if (name.contains(filterStr)) {
                    filterDateList.add(contacts);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


}
