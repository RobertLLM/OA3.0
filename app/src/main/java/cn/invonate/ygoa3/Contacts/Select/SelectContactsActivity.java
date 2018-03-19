package cn.invonate.ygoa3.Contacts.Select;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.SelectContactsAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.pingyin.CharacterParser;
import cn.invonate.ygoa3.Util.pingyin.ClearEditText;
import cn.invonate.ygoa3.Util.pingyin.PinyinComparator;
import cn.invonate.ygoa3.Util.pingyin.SideBar;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class SelectContactsActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.list_connect)
    ListView listConnect;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sidebar;

    ClearEditText filterEdit;
    @BindView(R.id.btn_complete)
    TextView btnComplete;
    private String id;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    SelectContactsAdapter adapter;

    private ArrayList<Contacts> list_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        ButterKnife.bind(this);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        name.setText(getIntent().getExtras().getString("name"));
        id = getIntent().getExtras().getString("id");
        View view = LayoutInflater.from(this).inflate(R.layout.item_input, null);
        filterEdit = view.findViewById(R.id.filter_edit);
        listConnect.addHeaderView(view);
        getContacts(id);
        sidebar.setTextView(dialog);
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
                list_contacts = data;
                filledData(list_contacts);
                Collections.sort(list_contacts, pinyinComparator);
                adapter = new SelectContactsAdapter(list_contacts, SelectContactsActivity.this);
                listConnect.setAdapter(adapter);
                listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.getData().get(position - 1).setIs_select(!adapter.getData().get(position - 1).isIs_select());
                        adapter.notifyDataSetChanged();
                    }
                });
                btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SelectContactsActivity.this, "11111", Toast.LENGTH_SHORT).show();
                        ArrayList<Contacts> select = new ArrayList<>();
                        for (Contacts c : list_contacts) {
                            if (c.isIs_select()) {
                                select.add(c);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", select);
                        Intent intent = getIntent();
                        intent.putExtras(bundle);
                        setResult(0x321,intent);
                        finish();
                    }
                });
                //设置右侧触摸监听
                sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

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
                        filterData(s.toString(), list_contacts);
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
        HttpUtil.getInstance(this,false).getContacts(subscriber, id);
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

    @OnClick({R.id.pic_back, R.id.layout_all, R.id.layout_none})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_all:
                if (adapter != null) {
                    for (Contacts c : list_contacts) {
                        c.setIs_select(true);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.layout_none:
                if (adapter != null) {
                    for (Contacts c : list_contacts) {
                        c.setIs_select(false);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
