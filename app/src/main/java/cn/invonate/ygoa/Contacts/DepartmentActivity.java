package cn.invonate.ygoa.Contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.Adapter.DepartmentAdapter;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Entry.Department;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.View.LYYPullToRefreshListView;
import cn.invonate.ygoa.httpUtil.HttpUtil;
import rx.Subscriber;

public class DepartmentActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.list_connect)
    LYYPullToRefreshListView listConnect;

    DepartmentAdapter adapter;

    private ArrayList<Department> list_depart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        ButterKnife.bind(this);
        name.setText(getIntent().getExtras().getString("name"));
        list_depart = (ArrayList<Department>) getIntent().getExtras().getSerializable("list");
        adapter = new DepartmentAdapter(list_depart, this);
        listConnect.setAdapter(adapter);
        listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDepartment(list_depart.get(position - 1).getId_(), list_depart.get(position - 1).getDepartment_name());
            }
        });
    }

    /**
     * 通讯录查询部门
     */
    private void getDepartment(final String id, final String name) {
        Subscriber subscriber = new Subscriber<ArrayList<Department>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listConnect.onRefreshComplete();
            }

            @Override
            public void onNext(final ArrayList<Department> data) {
                Log.i("getDepartment", data.toString());
                if (data.isEmpty()) {// 为空  跳转至成员列表界面
                    Intent intent = new Intent(DepartmentActivity.this, ContactsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("name", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {// 还有子部门  跳转至部门界面
                    Intent intent = new Intent(DepartmentActivity.this, DepartmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putSerializable("list", data);
                    startActivity(intent);
                }
            }
        };
        HttpUtil.getInstance().getDepartment(subscriber, id);
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }
}
