package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.TaskLine;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class LineAdapter extends BaseAdapter {

    private List<TaskLine.LineBean> data;
    private Context context;

    public LineAdapter(List<TaskLine.LineBean> data, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_line, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).getApprovedate() != null) {
            String[] str = data.get(position).getApprovedate().split("T");
            holder.date.setText(str[0]);
            holder.time.setText(str[1]);
        } else {
            holder.date.setText("");
            holder.time.setText("");
        }
        holder.name.setText(data.get(position).getApproveusername() + "【" + data.get(position).getApproveresult() + "】");
        holder.content.setText(data.get(position).getCmnt());
        if (position == 0) {
            holder.line_top.setVisibility(View.INVISIBLE);
            holder.circleBlue.setVisibility(View.VISIBLE);
        } else {
            holder.line_top.setVisibility(View.VISIBLE);
            holder.circleBlue.setVisibility(View.INVISIBLE);
        }
        if (position % 2 == 0) {
            holder.layout_line.setBackgroundColor(Color.parseColor("#f6fbff"));
        } else {
            holder.layout_line.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.circle_gray)
        ImageView circleGray;
        @BindView(R.id.circle_blue)
        ImageView circleBlue;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.line_top)
        View line_top;
        @BindView(R.id.layout_line)
        LinearLayout layout_line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
