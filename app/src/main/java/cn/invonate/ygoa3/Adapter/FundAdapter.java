package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Fund;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class FundAdapter extends BaseAdapter {

    private List<Fund.FundBean> data;
    private LayoutInflater inflater;

    public FundAdapter(List<Fund.FundBean> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.item_fund_nomal,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.description.setText(data.get(position).getDescription());
        holder.appid.setText("[" + data.get(position).getAppid() + "]");
        holder.operateTime.setText(data.get(position).getOperate_time());
        switch (data.get(position).getOpt_type()) {
            case "0":
            case "4":
            case "6":
            case "C":
                holder.accountAmt.setTextColor(Color.parseColor("#FF0000"));
                holder.accountAmt.setText("+" + data.get(position).getAccount_amt() + "元");
                break;
            case "1":
            case "3":
            case "5":
            case "8":
                holder.accountAmt.setTextColor(Color.parseColor("#000000"));
                holder.accountAmt.setText("-" + data.get(position).getAccount_amt() + "元");
                break;
        }
        return convertView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.appid)
        TextView appid;
        @BindView(R.id.operate_time)
        TextView operateTime;
        @BindView(R.id.account_amt)
        TextView accountAmt;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
