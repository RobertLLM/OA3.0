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
import cn.invonate.ygoa3.Entry.Welfare;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class WelfareAdapter extends BaseAdapter {

    private List<Welfare.WelfareBean> data;
    private LayoutInflater inflater;

    public WelfareAdapter(List<Welfare.WelfareBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_welfare, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.welfareName.setText(data.get(position).getWelfare_name());
        String time = data.get(position).getEnd_date();
        String[] times = time.split("T");
        if (times.length != 0) {
            holder.endDate.setText("截止日期：" + times[0]);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.welfare_name)
        TextView welfareName;
        @BindView(R.id.end_date)
        TextView endDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
