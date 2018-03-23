package cn.invonate.ygoa3.login.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.AddGroupDetailAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.RequestGroup;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

public class AddGroupActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.list_member)
    RecyclerView listMember;

    List<Contacts> list_contact = new ArrayList<>();
    AddGroupDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        listMember.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddGroupDetailAdapter(list_contact, this);
        listMember.setAdapter(adapter);
    }

    @OnClick({R.id.pic_back, R.id.edit, R.id.layout_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.edit:
                AddGroup(name.getText().toString(), description.getText().toString());
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
                if (!check(c, list_contact))
                    list_contact.add(c);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 新增群组
     *
     * @param name
     * @param desc
     */
    private void AddGroup(String name, String desc) {
        if ("".equals(name)) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(desc)) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list_contact.isEmpty()) {
            Toast.makeText(this, "请至少添加一个群组成员", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Contacts c : list_contact) {
            c.setUser_name(c.getUser_name() + "【" + c.getUser_code() + "】");
        }
        RequestGroup rg = new RequestGroup(name, desc, list_contact, "");
        Log.i("list_contact", JSON.toJSONString(rg));
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("AddGroup", data);
                finish();
            }
        };
        HttpUtil.getInstance(this, false).savePersonGroup(new ProgressSubscriber(onNextListener, this, "提交中"), JSON.toJSONString(rg));
    }

    private boolean check(Contacts contacts, List<Contacts> list_contact) {
        for (Contacts c : list_contact) {
            if (contacts.getUser_code().equals(c.getUser_code())) {
                return true;
            }
        }
        return false;
    }

}
