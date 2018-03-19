package cn.invonate.ygoa3.Contacts.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.DepartmentAdapter;
import cn.invonate.ygoa3.Contacts.ContactsActivity;
import cn.invonate.ygoa3.Contacts.DepartmentActivity;
import cn.invonate.ygoa3.Contacts.SearchContactsActivity;
import cn.invonate.ygoa3.Entry.Department;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/3/1.
 */

public class CompanyFragment extends Fragment {
    @BindView(R.id.filter_edit)
    TextView filterEdit;
    @BindView(R.id.list_connect)
    LYYPullToRefreshListView listConnect;
    Unbinder unbinder;

    private DepartmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_company, container, false);
        unbinder = ButterKnife.bind(this, view);
        getDepartment("0", "");
        listConnect.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getDepartment("0", "");
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                if ("0".equals(id)) {
                    adapter = new DepartmentAdapter(data, getActivity());
                    listConnect.setAdapter(adapter);
                    listConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            getDepartment(data.get(position - 1).getId_(), data.get(position - 1).getDepartment_name());
                        }
                    });
                    listConnect.onRefreshComplete();
                } else {
                    if (data.isEmpty()) {// 为空  跳转至成员列表界面
                        Intent intent = new Intent(getActivity(), ContactsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("name", name);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {// 还有子部门  跳转至部门界面
                        Intent intent = new Intent(getActivity(), DepartmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putSerializable("list", data);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

            }
        };
        HttpUtil.getInstance(getActivity(), false).getDepartment(subscriber, id);
    }

    @OnClick(R.id.filter_edit)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), SearchContactsActivity.class);
        startActivity(intent);
    }
}
