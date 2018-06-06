package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.R;

public class AttendAdapter extends BaseAdapter {

    private ArrayList<MeetingDetail.ResultBean.AttendListBean> data;
    private LayoutInflater inflater;

    public AttendAdapter(ArrayList<MeetingDetail.ResultBean.AttendListBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_attend, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getUserName());
        holder.code.setText(data.get(position).getUserCode());
        holder.head.setText(data.get(position).getUserName().substring(data.get(position).getUserName().length() - 2, data.get(position).getUserName().length()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.head)
        TextView head;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.code)
        TextView code;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
