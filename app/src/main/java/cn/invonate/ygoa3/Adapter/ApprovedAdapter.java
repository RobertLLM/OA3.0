package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Approved;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/16.
 */

public class ApprovedAdapter extends BaseAdapter {

    private List<Approved.ApprovedBean> data;


    private LayoutInflater inflater;

    public ApprovedAdapter(List<Approved.ApprovedBean> data, Context context) {
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
        ViewHolder holder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_approved, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.title.setText(data.get(position).getTitle());
        holder.time.setText(data.get(position).getApplyDate());
        if (data.get(position).getApproveResult()!=null){
            holder.result.setText(data.get(position).getApproveResult());
        }else{
            holder.result.setText("");
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.result)
        TextView result;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
