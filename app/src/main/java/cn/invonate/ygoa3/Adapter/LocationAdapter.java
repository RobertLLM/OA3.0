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
import cn.invonate.ygoa3.Entry.MeetingLocation;
import cn.invonate.ygoa3.R;

public class LocationAdapter extends BaseAdapter {

    private List<MeetingLocation.ResultBean> data;
    private LayoutInflater inflater;

    public LocationAdapter(List<MeetingLocation.ResultBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_location, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.location.setText(data.get(position).getDistrictName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.location)
        TextView location;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
