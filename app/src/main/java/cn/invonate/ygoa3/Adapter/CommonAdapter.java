package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.CycleMessage;
import cn.invonate.ygoa3.R;

public class CommonAdapter extends BaseAdapter {

    private int type;
    private CycleMessage bean;
    private Context context;

    @Override
    public int getCount() {
        int result = 0;
        switch (type) {
            case 0:
                result = bean.getInfolist().size();
                break;
            case 1:
                result = bean.getThumblist().size();
                break;
            case 2:
                result = bean.getReplyList().size();
                break;
        }
        return result;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_common, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case 0:
                holder.name.setText(bean.getInfolist().get(position).getUSER_NAME());
                holder.content.setText(new String(Base64.decode(bean.getInfolist().get(position).getCOMM_CONT().getBytes(), Base64.DEFAULT)));
                holder.time.setText(bean.getInfolist().get(position).getCOMM_TIME());
                holder.like.setVisibility(View.GONE);
                holder.content.setVisibility(View.VISIBLE);
//                holder.title.setText(bean.getInfolist().get(position).get);

                break;
            case 1:

                break;
            case 2:
                break;

        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
