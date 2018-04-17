package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Mail;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2017/10/22.
 */

public class MailAdapter extends BaseAdapter {
    private List<Mail> data;
    private LayoutInflater inflater;
    private String type;
    private boolean select_mode;

    public MailAdapter(List<Mail> data, Context context, String type) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
    }

    public boolean isSelect_mode() {
        return select_mode;
    }

    public void setSelect_mode(boolean select_mode) {
        this.select_mode = select_mode;
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
            convertView = inflater.inflate(R.layout.item_mail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case "INBOX":
                if (data.get(position).isSeen()) {
                    holder.mail_img.setImageResource(R.mipmap.icon_get);
                } else {
                    holder.mail_img.setImageResource(R.mipmap.icon_read);
                }
                break;
            case "Sent":
                holder.mail_img.setImageResource(R.mipmap.icon_sent);
                break;
            case "Drafts":
                holder.mail_img.setImageResource(R.mipmap.icon_drafts);
                break;
            case "Trash":
                holder.mail_img.setImageResource(R.mipmap.icon_trash);
                break;
        }
        if (select_mode) {
            holder.select.setVisibility(View.VISIBLE);
        } else {
            holder.select.setVisibility(View.GONE);
        }
        holder.mail_sender.setText(data.get(position).getPersonal());
        holder.mail_time.setText(data.get(position).getSend_date());
        holder.mail_title.setText(data.get(position).getSubject());
        holder.select.setChecked(data.get(position).isIs_selected());
        if (data.get(position).isContainerAttachment()){
            holder.img_file.setVisibility(View.VISIBLE);
        }else{
            holder.img_file.setVisibility(View.GONE);
        }
        AutoUtils.autoSize(convertView);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mail_img)
        ImageView mail_img;
        @BindView(R.id.mail_sender)
        TextView mail_sender;
        @BindView(R.id.mail_time)
        TextView mail_time;
        @BindView(R.id.mail_title)
        TextView mail_title;
        @BindView(R.id.select)
        CheckBox select;
        @BindView(R.id.img_file)
        ImageView img_file;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
