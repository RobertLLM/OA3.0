package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Room;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.TimeUtil;

public class RoomAdapter extends BaseAdapter {
    private List<Room.ResultBean.ListBean> data;
    private LayoutInflater inflater;
    private OnTimeClickListener onTimeClickListener;

    public OnTimeClickListener getOnTimeClickListener() {
        return onTimeClickListener;
    }

    public void setOnTimeClickListener(OnTimeClickListener onTimeClickListener) {
        this.onTimeClickListener = onTimeClickListener;
    }

    private int index = -1;

    public RoomAdapter(List<Room.ResultBean.ListBean> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
            convertView = inflater.inflate(R.layout.item_room, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).getRoomBuilding() != null && !data.get(position).getRoomBuilding().isEmpty()) {
            if (data.get(position).getRoomFloor() != null && !data.get(position).getRoomFloor().isEmpty()) {
                holder.name.setText(String.format("%s楼%s层%s", data.get(position).getRoomBuilding(), data.get(position).getRoomFloor(), data.get(position).getRoomNo()));
            } else {
                holder.name.setText(String.format("%s楼%s", data.get(position).getRoomBuilding(), data.get(position).getRoomNo()));
            }
        } else {
            if (data.get(position).getRoomFloor() != null && !data.get(position).getRoomFloor().isEmpty()) {
                holder.name.setText(String.format("%s层%s", data.get(position).getRoomFloor(), data.get(position).getRoomNo()));
            } else {
                holder.name.setText(String.format("%s", data.get(position).getRoomNo()));
            }
        }
        holder.sum.setText(data.get(position).getRoomSize() == null ? "" : data.get(position).getRoomSize() + "人");
        holder.device.setText(data.get(position).getLayout());
        for (int i = 0; i < holder.cells.length; i++) {
            if (check(i, data.get(position).getIndexList())) {
                holder.cells[i].setBackgroundColor(Color.parseColor("#638ec5"));
            } else {
                if (data.get(position).getSelectList() != null && check(i, data.get(position).getSelectList())) {
                    holder.cells[i].setBackgroundColor(Color.parseColor("#FF0000"));
                } else {
                    holder.cells[i].setBackgroundColor(Color.parseColor("#e5effd"));
                }
            }
        }
        if (data.get(position).getTimeList() != null && !data.get(position).getTimeList().isEmpty()) {
            StringBuffer checked_time = new StringBuffer();
            for (Room.ResultBean.ListBean.TimeListBean bean : data.get(position).getTimeList()) {
                checked_time.append(TimeUtil.INSTANCE.timeFormatJustMMHH(bean.getStartTime()));
                checked_time.append("-");
                checked_time.append(TimeUtil.INSTANCE.timeFormatJustMMHH(bean.getEndTime()));
                checked_time.append("，");
            }
            holder.checked_time.setVisibility(View.VISIBLE);
            holder.checked_time.setText("会议占用：" + checked_time.substring(0, checked_time.length() - 1));
        } else {
            holder.checked_time.setVisibility(View.GONE);
        }

        if (position == index) {
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }
        holder.time.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, data.get(position).getStart_h(), data.get(position).getStart_m(), data.get(position).getStart_s())) + "-"
                + new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, data.get(position).getEnd_h(), data.get(position).getEnd_m(), data.get(position).getEnd_s())));
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimeClickListener != null) {
                    onTimeClickListener.onClick(v, position);
                }
            }
        });
        return convertView;
    }

    private boolean check(int i, List<Integer> check) {
        for (int index : check) {
            if (i == index)
                return true;
        }
        return false;
    }

    static class ViewHolder {
        @BindView(R.id.check)
        CheckBox check;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.sum)
        TextView sum;
        @BindView(R.id.device)
        TextView device;
        @BindViews({R.id.cell0, R.id.cell1, R.id.cell2, R.id.cell3, R.id.cell4, R.id.cell5, R.id.cell6, R.id.cell7, R.id.cell8, R.id.cell9,
                R.id.cell10, R.id.cell11, R.id.cell12, R.id.cell13, R.id.cell14, R.id.cell15, R.id.cell16, R.id.cell17, R.id.cell18, R.id.cell19,
                R.id.cell20, R.id.cell21, R.id.cell22, R.id.cell23})
        View[] cells;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.checked_time)
        TextView checked_time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnTimeClickListener {
        void onClick(View view, int position);
    }
}
