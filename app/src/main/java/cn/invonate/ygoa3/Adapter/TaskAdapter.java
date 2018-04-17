package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.invonate.ygoa3.Entry.TaskEntry;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskAdapter extends BaseExpandableListAdapter {
    private List<TaskEntry> data;
    private LayoutInflater inflater;

    public TaskAdapter(List<TaskEntry> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getTasks().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getTasks().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_task_head, null);
        }
        convertView.setTag(R.layout.item_task_head, groupPosition);
        convertView.setTag(R.layout.item_task, -1);
        TextView lb = convertView.findViewById(R.id.lb);
        lb.setText(data.get(groupPosition).getLb() + "(" + data.get(groupPosition).getTasks().size() + ")");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_task, null);
        }
        convertView.setTag(R.layout.item_task_head, groupPosition);
        convertView.setTag(R.layout.item_task, childPosition);

        TextView depart = convertView.findViewById(R.id.depart);
        TextView lxdbh = convertView.findViewById(R.id.lxdbh);
        TextView jinji = convertView.findViewById(R.id.jinji);
        TextView title = convertView.findViewById(R.id.title);
        ImageView file = convertView.findViewById(R.id.file);
        TextView userName = convertView.findViewById(R.id.user_name);
        TextView zhaiyao = convertView.findViewById(R.id.zhaiyao);
        TextView re = convertView.findViewById(R.id.re);
        TextView xt = convertView.findViewById(R.id.xt);

        title.setText(data.get(groupPosition).getTasks().get(childPosition).getTitle());
        depart.setText(data.get(groupPosition).getTasks().get(childPosition).getApplyDept());
        // 是否显示附件
        if ("1".equals(data.get(groupPosition).getTasks().get(childPosition).getIs_fj())) {
            file.setVisibility(View.VISIBLE);
        } else {
            file.setVisibility(View.GONE);
        }
        // 是否显示紧急
        if ("1".equals(data.get(groupPosition).getTasks().get(childPosition).getImportant_leval())) {
            jinji.setVisibility(View.VISIBLE);
        } else {
            jinji.setVisibility(View.GONE);
        }

        // 是否显示重审
        if ("1".equals(data.get(groupPosition).getTasks().get(childPosition).getReject_message())) {
            re.setVisibility(View.VISIBLE);
        } else {
            re.setVisibility(View.GONE);
        }

        // 是否显示协同
        if ("1".equals(data.get(groupPosition).getTasks().get(childPosition).getIsXt())) {
            xt.setVisibility(View.VISIBLE);
        } else {
            xt.setVisibility(View.GONE);
        }

        String message = data.get(groupPosition).getTasks().get(childPosition).getOpinion_();

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

        String[] date = data.get(groupPosition).getTasks().get(childPosition).getCreate_().split("T");
        userName.setText(data.get(groupPosition).getTasks().get(childPosition).getUser_name() + " " + message + " " + date[0]);
        userName.setTextColor(color);

        //  设置摘要
        if (data.get(groupPosition).getTasks().get(childPosition).getZhaiyao() == null || "".equals(data.get(groupPosition).getTasks().get(childPosition).getZhaiyao())) {
            zhaiyao.setVisibility(View.GONE);
        } else {
            zhaiyao.setVisibility(View.VISIBLE);
        }
        zhaiyao.setText(data.get(groupPosition).getTasks().get(childPosition).getZhaiyao());
        lxdbh.setText(data.get(groupPosition).getTasks().get(childPosition).getLxdbh());
        lxdbh.setText(data.get(groupPosition).getTasks().get(childPosition).getLxdbh());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
