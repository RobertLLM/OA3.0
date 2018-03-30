package cn.invonate.ygoa3.Contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.MemberAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Member;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.pingyin.ClearEditText;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class SearchContactsActivity extends BaseActivity {

    @BindView(R.id.filter_edit)
    ClearEditText filterEdit;
    @BindView(R.id.list_connect)
    LYYPullToRefreshListView listConnect;

    private int total;

    private MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contacts);
        ButterKnife.bind(this);
        //根据输入框输入值的改变来过滤搜索
        filterEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getMembers(filterEdit.getText().toString().trim(), 1);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listConnect.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMembers(filterEdit.getText().toString().trim(), 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMembers(filterEdit.getText().toString().trim(), adapter.getCount() / 20 + 1);
            }
        });

    }

    /**
     * 模糊搜索员工信息
     */
    private void getMembers(final String keyword, final int page) {
        if ("".equals(keyword)) {
            return;
        }
        Subscriber subscriber = new Subscriber<Member>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Member data) {
                Log.i("getMembers", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    adapter = new MemberAdapter(data.getRows(), SearchContactsActivity.this);
                    listConnect.setAdapter(adapter);
                } else {
                    adapter.getData().addAll(data.getRows());
                    adapter.notifyDataSetChanged();
                }
                listConnect.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listConnect.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listConnect.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                adapter.setOnItemClickListener(new MemberAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contacts", data.getRows().get(position));
                        stepActivity(bundle, ContactsDetailActivity.class);
                    }
                });
            }
        };
        HttpUtil.getInstance(this,false).getMembers(subscriber, keyword, page, 20);
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }
}
