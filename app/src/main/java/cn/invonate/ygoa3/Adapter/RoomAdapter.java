package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Room;
import cn.invonate.ygoa3.R;

public class RoomAdapter extends BaseAdapter {
    private List<Room.ResultBean.ListBean> data;
    private LayoutInflater inflater;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            for (int j = 0; j < data.get(position).getIndexList().size(); j++) {
                if (i == data.get(position).getIndexList().get(j)) {
                    holder.cells[i].setBackgroundColor(Color.parseColor("#FF0000"));
                    continue;
                }
                holder.cells[i].setBackgroundColor(Color.parseColor("#e5effd"));
            }
        }
        if (position == index) {
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }
        return convertView;
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
                R.id.cell10, R.id.cell11, R.id.cell12, R.id.cell13, R.id.cell14, R.id.cell15, R.id.cell16, R.id.cell17, R.id.cell18, R.id.cell19})
        View[] cells;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
