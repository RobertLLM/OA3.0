package cn.invonate.ygoa3.Contacts.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.CyContactsAdapter;
import cn.invonate.ygoa3.Contacts.ContactsDetailActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.CyContacts;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.pingyin.CharacterParser;
import cn.invonate.ygoa3.Util.pingyin.ClearEditText;
import cn.invonate.ygoa3.Util.pingyin.PinyinComparator2;
import cn.invonate.ygoa3.Util.pingyin.SideBar;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/3/1.
 */

public class CyContactsFragment extends Fragment {

    ClearEditText filterEdit;
    @BindView(R.id.list_connect)
    SwipeMenuListView listConnect;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidebar)
    SideBar sidebar;
    Unbinder unbinder;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator2 pinyinComparator;

    private YGApplication app;

    private CyContactsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_cy_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator2();
        View head = LayoutInflater.from(getActivity()).inflate(R.layout.item_input, null);
        filterEdit = head.findViewById(R.id.filter_edit);
        listConnect.addHeaderView(head);
        sidebar.setTextView(dialog);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(250);
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };

        // set creator
        listConnect.setMenuCreator(creator);
        getCyContacts();
        return view;
    }

    /**
     * 获取常用联系人
     */
    public void getCyContacts() {
        Subscriber subscriber = new Subscriber<CyContacts>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final CyContacts data) {
                Log.i("getCyContacts", data.toString());
                filledData(data.getRows());
                Collections.sort(data.getRows(), pinyinComparator);
                adapter = new CyContactsAdapter(data.getRows(), getActivity());
                listConnect.setAdapter(adapter);
                listConnect.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                        Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT).show();
                        delete_contacts(adapter.getData().get(position).getC_id());
                        return false;
                    }
                });
                listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ContactsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contacts", CyToNormal(adapter.getData().get(position - 1)));
                        bundle.putBoolean("show_layout", false);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
                        filterData(s.toString(), data.getRows());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        };
        HttpUtil.getInstance(getActivity(), false).getCyContacts(subscriber, app.getUser().getSessionId(), 1, 10000);
    }


    /**
     * 添加首字母
     *
     * @param data
     */
    private void filledData(List<CyContacts.CyContactsBean> data) {
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
    private void filterData(String filterStr, List<CyContacts.CyContactsBean> data) {
        List<CyContacts.CyContactsBean> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = data;
        } else {
            filterDateList.clear();
            for (CyContacts.CyContactsBean contacts : data) {
                String name = contacts.getUser_name();
                String code = contacts.getUser_code();
                if (name.contains(filterStr) || code.contains(filterStr)) {
                    filterDateList.add(contacts);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Contacts CyToNormal(CyContacts.CyContactsBean cy) {
        Contacts result = new Contacts();
        result.setUser_code(cy.getUser_code());
        result.setUser_name(cy.getUser_name());
        result.setDepartment_name(cy.getDepartment_name());
        result.setUser_phone(cy.getUser_phone());
        result.setUser_photo(cy.getUser_photo());
        return result;
    }


    private void delete_contacts(String c_id) {
        Map<String, String> json = new HashMap<>();
        json.put("c_id", c_id);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("delete_contacts", data);
                getCyContacts();
            }
        };
        HttpUtil.getInstance(getActivity(), false).delete_contacts(new ProgressSubscriber(onNextListener, getActivity(), "提交中"), JSON.toJSONString(json));
    }


}
