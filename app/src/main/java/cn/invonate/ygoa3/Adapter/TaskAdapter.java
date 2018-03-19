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
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskAdapter extends BaseAdapter {
    private List<Mission.MissionBean> data;
    private LayoutInflater inflater;

    public TaskAdapter(List<Mission.MissionBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(data.get(position).getLb());
        if (position > 1 && data.get(position).getLb().equals(data.get(position - 1).getLb())) {
            holder.type.setVisibility(View.GONE);
        } else {
            holder.type.setVisibility(View.VISIBLE);
        }
        holder.title.setText(data.get(position).getTitle());
        holder.depart.setText(data.get(position).getApplyDept());
        holder.config.setText(data.get(position).getReject_message());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.depart)
        TextView depart;
        @BindView(R.id.config)
        TextView config;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
