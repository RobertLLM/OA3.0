package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.ReportManAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.R;

public class ReportManActivity extends BaseActivity {

    @BindView(R.id.list_person)
    ListView listPerson;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<MeetingDetail.ResultBean.AttendListBean> list_data;
    private ReportManAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_man);
        ButterKnife.bind(this);
        list_data = (List<MeetingDetail.ResultBean.AttendListBean>) getIntent().getExtras().getSerializable("list");
        String code = getIntent().getExtras().getString("code");
        adapter = new ReportManAdapter(list_data, this);
        adapter.setIndex(findIndex(code));
        listPerson.setAdapter(adapter);
        listPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setIndex(position);
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 查找默认记录人
     */
    private int findIndex(String code) {
        for (int i = 0; i < list_data.size(); i++) {
            if (list_data.get(i).getUserCode().equals(code))
                return i;
        }
        return -1;
    }

    @OnClick({R.id.pic_back, R.id.pic_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.pic_sure:
                int index = adapter.getIndex();
                if (index == -1) {
                    Toast.makeText(this, "请选择记录人", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("code", list_data.get(index).getUserCode());
                bundle.putString("id", list_data.get(index).getUserId());
                bundle.putString("name", list_data.get(index).getUserName());
                intent.putExtras(bundle);
                setResult(0x321, intent);
                finish();
                break;
        }
    }
}
