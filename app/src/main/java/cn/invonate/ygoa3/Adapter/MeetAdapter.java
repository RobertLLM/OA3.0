package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

    private OnAttendClickListener onAttendClickListener;

    public MeetAdapter(List<Meeting.ResultBean.MeetBean> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public OnAttendClickListener getOnAttendClickListener() {
        return onAttendClickListener;
    }

    public void setOnAttendClickListener(OnAttendClickListener onAttendClickListener) {
        this.onAttendClickListener = onAttendClickListener;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.locate.setText(data.get(position).getAddressName());
        holder.dynamic.setText(data.get(position).getDynamic() + "条动态");
        holder.sum.setText(data.get(position).getAttendNum() + "/" + data.get(position).getTotalNum() + "人参加");
        if ("1".equals(data.get(position).getMeetingStatus())) {//取消
            holder.status.setText("取消");
            holder.status.setBackgroundResource(R.drawable.back_meet_grey);
            holder.status.setTextColor(Color.parseColor("#A4A4A4"));
        } else if ("0".equals(data.get(position).getMeetingStatus())) {
            String status = data.get(position).getMeetingGoingStatus();
            if (status != null) {
                switch (status) {
                    case "0":
                        holder.status.setText("未进行");
                        holder.status.setBackgroundResource(R.drawable.back_meet_grey);
                        holder.status.setTextColor(Color.parseColor("#A4A4A4"));
                        break;
                    case "1":
                        holder.status.setText("已结束");
                        holder.status.setBackgroundResource(R.drawable.back_meet_red);
                        holder.status.setTextColor(Color.parseColor("#FF0000"));
                        break;
                    case "2":
                        holder.status.setText("进行中");
                        holder.status.setBackgroundResource(R.drawable.back_meet_blue);
                        holder.status.setTextColor(Color.parseColor("#0099FF"));
                        break;
                    default:
                        holder.status.setVisibility(View.GONE);
                        break;
                }
            } else {
                holder.status.setVisibility(View.GONE);
            }
        } else {
            holder.status.setVisibility(View.GONE);
        }
        if ("0".equals(data.get(position).getMeetingStatus()) && "0".equals(data.get(position).getMeetingGoingStatus())) {
            if (onAttendClickListener != null && "0".equals(data.get(position).getJoinStatus())) {
                holder.layout_attend.setVisibility(View.VISIBLE);
                holder.attend_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAttendClickListener.onAttend(v, position);
                    }
                });
                holder.attend_not.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAttendClickListener.onNotAttend(v, position);
                    }
                });
            } else {
                holder.layout_attend.setVisibility(View.GONE);
            }
        } else {
            holder.layout_attend.setVisibility(View.GONE);
        }
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
        @BindView(R.id.layout_attend)
        LinearLayout layout_attend;
        @BindView(R.id.attend_sure)
        TextView attend_sure;
        @BindView(R.id.attend_not)
        TextView attend_not;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnAttendClickListener {
        void onAttend(View view, int position);

        void onNotAttend(View view, int position);
    }
}
