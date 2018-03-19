package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Salary;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    private ArrayList<ArrayList<Salary.GjjDataBean>> data;
    private Context context;

    public SocialAdapter(ArrayList<ArrayList<Salary.GjjDataBean>> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_social, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SocialAdapter.ViewHolder holder, int position) {
        holder.listSocialDetail.setLayoutManager(new LinearLayoutManager(context));
        holder.listSocialDetail.setAdapter(new SocialDetailAdapter(data.get(position), context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_social_detail)
        RecyclerView listSocialDetail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
