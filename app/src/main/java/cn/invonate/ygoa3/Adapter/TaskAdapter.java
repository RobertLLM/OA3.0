package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskAdapter extends BaseAdapter {
    private List<Mission.MissionBean> data;
    private LayoutInflater inflater;

    public TaskAdapter(List<Mission.MissionBean> data, Context context) {
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
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lb.setText(data.get(position).getLb());
        if (position > 1 && data.get(position).getLb().equals(data.get(position - 1).getLb())) {
            holder.lb.setVisibility(View.GONE);
        } else {
            holder.lb.setVisibility(View.VISIBLE);
        }
        holder.title.setText(data.get(position).getTitle());
        holder.depart.setText(data.get(position).getApplyDept());
        // 是否显示附件
        if ("1".equals(data.get(position).getIs_fj())) {
            holder.file.setVisibility(View.VISIBLE);
        } else {
            holder.file.setVisibility(View.GONE);
        }
        // 是否显示紧急
        if ("1".equals(data.get(position).getImportant_leval())) {
            holder.jinji.setVisibility(View.VISIBLE);
        } else {
            holder.jinji.setVisibility(View.GONE);
        }

        // 是否显示重审
        if ("1".equals(data.get(position).getReject_message())) {
            holder.re.setVisibility(View.VISIBLE);
        } else {
            holder.re.setVisibility(View.GONE);
        }

        // 是否显示协同
        if ("1".equals(data.get(position).getIsXt())) {
            holder.xt.setVisibility(View.VISIBLE);
        } else {
            holder.xt.setVisibility(View.GONE);
        }

        String message = data.get(position).getOpinion_();

        int color = Color.parseColor("#000000");

        if ("Y".equals(message)) {
            message = "同意";
            color = Color.parseColor("#5ABE98");
        } else if ("N".equals(message)) {
            message = "驳回";
            color = Color.parseColor("#E51C19");
        } else if ("doing".equals(message)) {
            message = "处理中";
        } else if ("done".equals(message)) {
            message = "完成";
        } else if ("D".equals(message)) {
            message = "已接收";
        } else if ("B".equals(message)) {
            message = "已撤回";
        } else if ("X".equals(message)) {
            message = "预算已填写";
        } else if ("Q".equals(message)) {
            message = "已确认";
        } else if ("C".equals(message)) {
            message = "协同";
        } else if ("E".equals(message)) {
            message = "结束流程";
        } else if ("T".equals(message)) {
            message = "已处理";
        }

        String[] date = data.get(position).getCreate_().split("T");
        holder.userName.setText(data.get(position).getUser_name() + " " + message + " " + date[0]);
        holder.userName.setTextColor(color);

        //  设置摘要
        if (data.get(position).getZhaiyao() == null || "".equals(data.get(position).getZhaiyao())) {
            holder.zhaiyao.setVisibility(View.GONE);
        } else {
            holder.zhaiyao.setVisibility(View.VISIBLE);
        }
        holder.zhaiyao.setText(data.get(position).getZhaiyao());
        holder.lxdbh.setText(data.get(position).getLxdbh());
        holder.lxdbh.setText(data.get(position).getLxdbh());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.lb)
        TextView lb;
        @BindView(R.id.depart)
        TextView depart;
        @BindView(R.id.lxdbh)
        TextView lxdbh;
        @BindView(R.id.jinji)
        TextView jinji;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.file)
        ImageView file;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.zhaiyao)
        TextView zhaiyao;
        @BindView(R.id.re)
        TextView re;
        @BindView(R.id.xt)
        TextView xt;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
