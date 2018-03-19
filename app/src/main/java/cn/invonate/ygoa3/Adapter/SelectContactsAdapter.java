package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/1/23.
 */

public class SelectContactsAdapter extends BaseAdapter {
    private List<Contacts> data;
    private LayoutInflater inflater;

    public SelectContactsAdapter(List<Contacts> data, Context context) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public List<Contacts> getData() {
        return data;
    }

    public void setData(List<Contacts> data) {
        this.data = data;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param data
     */
    public void updateListView(List<Contacts> data) {
        this.data = data;
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.item_select_contacts, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.catalog.setVisibility(View.VISIBLE);
            holder.catalog.setText(data.get(position).getSortLetters());
        } else {
            holder.catalog.setVisibility(View.GONE);
        }
        holder.name.setText(data.get(position).getUser_name() + "(" + data.get(position).getUser_code() + ")");
        if (data.get(position).isIs_select()) {
            holder.selected.setChecked(true);
            holder.layout.setBackgroundColor(Color.parseColor("#eaf0f9"));
        } else {
            holder.selected.setChecked(false);
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.catalog)
        TextView catalog;
        @BindView(R.id.selected)
        CheckBox selected;
        @BindView(R.id.name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return data.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }
}
