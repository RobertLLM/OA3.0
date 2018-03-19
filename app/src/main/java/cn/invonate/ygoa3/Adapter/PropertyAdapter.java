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
import cn.invonate.ygoa3.Entry.Property;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/2/26.
 */

public class PropertyAdapter extends BaseAdapter {
    private List<Property.PropertyBean> data;
    private LayoutInflater inflater;

    public PropertyAdapter(List<Property.PropertyBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_property, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getAsset_category_name() == null ? "资产名称：" :"资产名称：" +  data.get(position).getAsset_category_name());
        holder.type.setText(data.get(position).getSpecification_() == null ? "规格型号：" :"规格型号：" +  data.get(position).getSpecification_());
        holder.sum.setText(data.get(position).getQty() + data.get(position).getMeasurement_());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.sum)
        TextView sum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
