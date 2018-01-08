package cn.invonate.ygoa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa.Entry.Department;
import cn.invonate.ygoa.R;

/**
 * Created by liyangyang on 2018/1/5.
 */

public class DepartmentAdapter extends BaseAdapter {
    private List<Department> data;
    private LayoutInflater inflater;

    public DepartmentAdapter(List<Department> data, Context context) {
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
        ViewHolder holder=null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_department, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(data.get(position).getDepartment_name());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
