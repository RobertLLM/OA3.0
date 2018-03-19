package cn.invonate.ygoa3.login.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Adapter.SocialAdapter;
import cn.invonate.ygoa3.Entry.Salary;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class SocialFragment extends Fragment {
    @BindView(R.id.list_social)
    RecyclerView listSocial;

    ArrayList<ArrayList<Salary.GjjDataBean>> gjjData;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_layout_social, container, false);
        unbinder = ButterKnife.bind(this, view);
        listSocial.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData(ArrayList<ArrayList<Salary.GjjDataBean>> gjjData) {
        listSocial.setAdapter(new SocialAdapter(gjjData, getActivity()));
    }
}
