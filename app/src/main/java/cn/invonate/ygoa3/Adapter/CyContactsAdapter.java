package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.CyContacts;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;

/**
 * Created by liyangyang on 2018/3/1.
 */

public class CyContactsAdapter extends BaseAdapter {

    private List<CyContacts.CyContactsBean> data;
    private Context context;

    public CyContactsAdapter(List<CyContacts.CyContactsBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public List<CyContacts.CyContactsBean> getData() {
        return data;
    }

    public void setData(List<CyContacts.CyContactsBean> data) {
        this.data = data;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param data
     */
    public void updateListView(List<CyContacts.CyContactsBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cy_contacts, parent, false);
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
        holder.name.setText(data.get(position).getUser_name());
        holder.userPhone.setText(data.get(position).getUser_phone());
        holder.depart.setText(data.get(position).getDepartment_name());
        String url = data.get(position).getUser_photo();
        if (url == null || url.equals("")) {
            holder.headText.setVisibility(View.VISIBLE);
            String n = data.get(position).getUser_name().substring(0, 1);
            holder.headText.setText(n);
            holder.headImg.setVisibility(View.GONE);
        } else {
            holder.headText.setVisibility(View.GONE);
            holder.headImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(HttpUtil.URL_FILE + url).skipMemoryCache(true).error(R.mipmap.pic_head).into(holder.headImg);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.catalog)
        TextView catalog;
        @BindView(R.id.head_img)
        CircleImageView headImg;
        @BindView(R.id.head_text)
        TextView headText;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.user_phone)
        TextView userPhone;
        @BindView(R.id.depart)
        TextView depart;

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


}
