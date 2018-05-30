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
import cn.invonate.ygoa3.Entry.Meeting;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.TimeUtil;

public class MeetAdapter extends BaseAdapter {
    private List<Meeting.ResultBean.MeetBean> data;
    private LayoutInflater inflater;

    public MeetAdapter(List<Meeting.ResultBean.MeetBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_meet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(data.get(position).getTitle());
        holder.time.setText(TimeUtil.timeFormatNoYear(data.get(position).getStartTime()) + " - " + TimeUtil.timeFormatJustMMHH(data.get(position).getEndTime()));
        holder.person.setText(data.get(position).getCreatorName());
        holder.dynamic.setText(data.get(position).getDynamic() + "条动态");
        holder.sum.setText(data.get(position).getAttendNum() + "/" + data.get(position).getTotalNum() + "人参加");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.locate)
        TextView locate;
        @BindView(R.id.person)
        TextView person;
        @BindView(R.id.dynamic)
        TextView dynamic;
        @BindView(R.id.sum)
        TextView sum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
