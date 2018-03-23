package cn.invonate.ygoa3.login.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.GroupDetailAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.PersonGroup;
import cn.invonate.ygoa3.Entry.RequestGroup2;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class GroupDetailActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.list_member)
    RecyclerView listMember;

    PersonGroup.GroupBean group;

    GroupDetailAdapter adapter;

    @BindView(R.id.edit)
    TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        group = (PersonGroup.GroupBean) getIntent().getExtras().getSerializable("group");
        name.setText("群组名称：" + group.getGroup_name());
        description.setText("群组描述：" + group.getGroup_desc());

        listMember.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupDetailAdapter(group.getMembers(), this);
        listMember.setAdapter(adapter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    if (!adapter.isIs_select()) {
                        adapter.setIs_select(true);
                        adapter.notifyDataSetChanged();
                        edit.setText("删除");
                    } else {
                        //删除
                        deleteGroup();
                    }
                }
            }
        });
        adapter.setOnItemClickLitener(new GroupDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.isIs_select()) {
                    group.getMembers().get(position).setIs_select(!group.getMembers().get(position).isIs_select());
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @OnClick({R.id.pic_back, R.id.layout_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_add:
                Intent intent = new Intent(this, SelectDepartmentActivity.class);
                startActivityForResult(intent, 0x123);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<Contacts> select = (ArrayList<Contacts>) data.getSerializableExtra("list");
            Log.i("select", JSON.toJSONString(select));
            for (Contacts c : select) {
                if (!check(c)) {
                    PersonGroup.GroupBean.MembersBean member = new PersonGroup.GroupBean.MembersBean();
                    member.setUser_code(c.getUser_code());
                    member.setUser_name(c.getUser_name());
                    member.setUser_photo(c.getUser_photo());
                    member.setDepartment_name(c.getDepartment_name());
                    member.setId_(c.getId_());
                    group.getMembers().add(member);
                }
                addGroup();
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * @param contacts
     */
    private boolean check(Contacts contacts) {
        for (PersonGroup.GroupBean.MembersBean members : group.getMembers()) {
            if (members.getUser_code().equals(contacts.getUser_code()))
                return true;
        }
        return false;
    }

    /**
     * 新增群组
     */
    private void addGroup() {
        for (PersonGroup.GroupBean.MembersBean m : group.getMembers()) {
            m.setUser_name(m.getUser_name() + "【" + m.getUser_code() + "】");
        }

        RequestGroup2 rg = new RequestGroup2(group.getGroup_name(), group.getGroup_desc(), group.getMembers(), group.getGroup_id());
        Log.i("addGroup", JSON.toJSONString(rg));

        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("AddGroup", data);

            }
        };
        HttpUtil.getInstance(this, false).savePersonGroup(new ProgressSubscriber(onNextListener, this, "提交中"), JSON.toJSONString(rg));
    }

    /**
     * 删除群组
     */
    private void deleteGroup() {
        for (int i = 0; i < group.getMembers().size(); i++) {
            if (group.getMembers().get(i).isIs_select()) {
                group.getMembers().remove(i);
            }
        }
        RequestGroup2 rg = new RequestGroup2(group.getGroup_name(), group.getGroup_desc(), group.getMembers(), group.getGroup_id());
        Log.i("addGroup", JSON.toJSONString(rg));

        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("AddGroup", data);
                adapter.setIs_select(false);
                adapter.notifyDataSetChanged();
                edit.setText("编辑");
            }
        };
        HttpUtil.getInstance(this, false).savePersonGroup(new ProgressSubscriber(onNextListener, this, "提交中"), JSON.toJSONString(rg));
    }

}
