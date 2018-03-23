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
import cn.invonate.ygoa3.Entry.PersonGroup;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/19.
 */

public class GroupAdapter extends BaseAdapter {

    private List<PersonGroup.GroupBean> data;
    private Context context;

    public GroupAdapter(List<PersonGroup.GroupBean> data, Context context) {
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
        ViewHolder holder=null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getGroup_name());
        holder.description.setText(data.get(position).getGroup_desc());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
