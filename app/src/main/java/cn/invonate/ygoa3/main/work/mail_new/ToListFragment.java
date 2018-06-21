package cn.invonate.ygoa3.main.work.mail_new;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.ToListAdapter;
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/29.
 */

public class ToListFragment extends Fragment {
    @BindView(R.id.list_cc)
    ListView listCc;
    Unbinder unbinder;

    ArrayList<MailNew.ResultBean.MailsBean.ReceiveBean> receiver;
    ArrayList<MailNew.ResultBean.MailsBean.CcBean> cc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getArguments().getInt("index", -1)) {
            case 0:
                receiver = (ArrayList<MailNew.ResultBean.MailsBean.ReceiveBean>) getArguments().getSerializable("list");
                Log.i("list", JSON.toJSONString(receiver));
                break;
            case 1:
                cc = (ArrayList<MailNew.ResultBean.MailsBean.CcBean>) getArguments().getSerializable("list");
                Log.i("list", JSON.toJSONString(cc));
                break;
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        listCc.setAdapter(new ToListAdapter(receiver, cc, getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
