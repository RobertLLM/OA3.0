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
import cn.invonate.ygoa3.Entry.Mail;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/29.
 */

public class ToListAdapter extends BaseAdapter {

    private List<Mail.Address> data;
    private Context context;

    public ToListAdapter(List<Mail.Address> data, Context context) {
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
        holder.name.setText(data.get(position).getPersonal());
        String address = data.get(position).getAddress();
        if (address.contains("<") && address.contains(">")) {
            holder.address.setText(address.substring(address.indexOf("<") + 1, address.indexOf(">")));
        } else {
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
