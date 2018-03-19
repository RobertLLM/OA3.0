package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/9.
 */

public class MemberAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Contacts> data;
    private LayoutInflater inflater;
    private Filter mFilter;
    //private ArrayList<Contacts> mUnfilteredData;

    public List<Contacts> getData() {
        return data;
    }

    public void setData(ArrayList<Contacts> data) {
        this.data = data;
    }

    public MemberAdapter(ArrayList<Contacts> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_member, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getUser_name());
        holder.depart.setText(data.get(position).getDepartment_name());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.depart)
        TextView depart;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Contacts> list = data;
                results.values = list;
                results.count = list.size();
            } else {


                ArrayList<Contacts> newValues = data;

//                for (int i = 0; i < count; i++) {
//                    Contacts pc = unfilteredValues.get(i);
//                    if (pc != null) {
//                        if (pc.getUser_name() != null && pc.getUser_name().contains(prefixString)) {
//                            newValues.add(pc);
//                        } else if (pc.getUser_code() != null && pc.getUser_code().contains(prefixString)) {
//                            newValues.add(pc);
//                        }
//                    }
//                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            data = (ArrayList<Contacts>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
