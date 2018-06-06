package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.MeetingDynamic;
import cn.invonate.ygoa3.R;

public class DynamicAdapter extends BaseAdapter {

    private List<MeetingDynamic.ResultBean> data;
    private LayoutInflater inflater;

    public DynamicAdapter(List<MeetingDynamic.ResultBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_dynamic, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.message.setText(data.get(position).getContent());
        holder.time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(data.get(position).getCreateTime())));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
