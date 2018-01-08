package cn.invonate.ygoa.Contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.Adapter.ContactsAdapter;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.Contacts;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.pingyin.CharacterParser;
import cn.invonate.ygoa.Util.pingyin.ClearEditText;
import cn.invonate.ygoa.Util.pingyin.PinyinComparator;
import cn.invonate.ygoa.Util.pingyin.SideBar;
import cn.invonate.ygoa.httpUtil.HttpUtil;
import rx.Subscriber;

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.list_connect)
    ListView listConnect;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
//    @BindView(R.id.filter_edit)
    ClearEditText filterEdit;
    private String id;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        name.setText(getIntent().getExtras().getString("name"));
        id = getIntent().getExtras().getString("id");
        View view= LayoutInflater.from(this).inflate(R.layout.item_input,null);
        filterEdit=view.findViewById(R.id.filter_edit);
        listConnect.addHeaderView(view);
        getContacts(id);
        sideBar.setTextView(dialog);
    }

    /**
     * 通讯录部门成员
     *
     * @param id 部门id
     */
    private void getContacts(String id) {
        Subscriber subscriber = new Subscriber<ArrayList<Contacts>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final ArrayList<Contacts> data) {
                Log.i("getContacts", data.toString());
                filledData(data);
                Collections.sort(data, pinyinComparator);
                adapter = new ContactsAdapter(data, ContactsActivity.this);
                listConnect.setAdapter(adapter);
                listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("contacts",data.get(position));
                        stepActivity(bundle,ContactsDetailActivity.class);
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
                        filterData(s.toString(), data);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        };
        HttpUtil.getInstance().getContacts(subscriber, id);
    }

    /**
     * 添加首字母
     *
     * @param data
     */
    private void filledData(List<Contacts> data) {
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

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr, List<Contacts> data) {
        List<Contacts> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = data;
        } else {
            filterDateList.clear();
            for (Contacts contacts : data) {
                String name = contacts.getUser_name();
                String all_name = contacts.getName_all_spell();
                String single_name = contacts.getName_first_spell();
                String code = contacts.getUser_code();
                if (name.contains(filterStr) || all_name.contains(filterStr) || single_name.contains(filterStr) || code.contains(filterStr)) {
                    filterDateList.add(contacts);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
