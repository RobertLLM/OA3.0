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
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/29.
 */

public class ToListAdapter extends BaseAdapter {

    private List<String> data;
    private Context context;

    public ToListAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_to_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String address = data.get(position);
        if (address.contains("<")) {
            String name = address.substring(address.indexOf("<") + 1, address.lastIndexOf("@"));
            holder.name.setText(name);
            holder.address.setText(address.substring(address.indexOf("<") + 1, address.indexOf(">")));
        } else {
            String[] names = address.split("@");
            holder.name.setText(names[0]);
            holder.address.setText(address);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
