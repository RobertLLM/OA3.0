package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/19.
 */

public class AutoCompleteAdapter extends ArrayAdapter<Contacts> {
    List<Contacts> data;
    Context context;

    public AutoCompleteAdapter(@NonNull Context context, int resource, List<Contacts> data) {
        super(context, resource);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MemberAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
            holder = new MemberAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MemberAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getUser_name());
        holder.depart.setText(data.get(position).getDepartment_name());
        return convertView;
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
}
