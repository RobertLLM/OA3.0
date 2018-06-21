package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/29.
 */

public class ToListAdapter extends BaseAdapter {

    private List<MailNew.ResultBean.MailsBean.ReceiveBean> receiver;
    private List<MailNew.ResultBean.MailsBean.CcBean> cc;
    private Context context;

    public ToListAdapter(ArrayList<MailNew.ResultBean.MailsBean.ReceiveBean> receiver, List<MailNew.ResultBean.MailsBean.CcBean> cc, Context context) {
        this.receiver = receiver;
        this.cc = cc;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (receiver != null) {
            return receiver.size();
        } else {
            return cc.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return "";
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
        if (receiver != null) {
            holder.name.setText(receiver.get(position).getUserName());
            String address = receiver.get(position).getAddress();
            if (address.contains("<") && address.contains(">")) {
                holder.address.setText(address.substring(address.indexOf("<") + 1, address.indexOf(">")));
            } else {
                holder.address.setText(address);
            }
        } else {
            holder.name.setText(cc.get(position).getUserName());
            String address = cc.get(position).getAddress();
            if (address.contains("<") && address.contains(">")) {
                holder.address.setText(address.substring(address.indexOf("<") + 1, address.indexOf(">")));
            } else {
                holder.address.setText(address);
            }
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
