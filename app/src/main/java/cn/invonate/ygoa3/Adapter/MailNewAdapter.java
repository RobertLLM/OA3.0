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
import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2017/10/22.
 */

public class MailNewAdapter extends BaseAdapter {
    private List<MailNew.ResultBean.MailsBean> data;
    private LayoutInflater inflater;
    private String type;
    private boolean select_mode;

    public MailNewAdapter(List<MailNew.ResultBean.MailsBean> data, Context context, String type) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
                if (data.get(position).getRead() == 1) {
                    holder.mail_img.setImageResource(R.mipmap.icon_get);
                } else {
                    holder.mail_img.setImageResource(R.mipmap.icon_read);
                }
                holder.mail_sender.setText(data.get(position).getSender().getUserName());
                break;
            case "SENT":
                holder.mail_img.setImageResource(R.mipmap.icon_sent);
                holder.mail_sender.setText(getReceiver(data.get(position).getReceive()));
                break;
            case "DRAFTS":
                holder.mail_img.setImageResource(R.mipmap.icon_drafts);
                holder.mail_sender.setText(getReceiver(data.get(position).getReceive()));
                break;
            case "TRASH":
                holder.mail_img.setImageResource(R.mipmap.icon_trash);
                holder.mail_sender.setText(data.get(position).getSender().getUserName());
                break;
        }
        if (select_mode) {
            holder.select.setVisibility(View.VISIBLE);
        } else {
            holder.select.setVisibility(View.GONE);
        }
        holder.mail_time.setText(data.get(position).getSentDate());
        holder.mail_title.setText(data.get(position).getSubject().equals("") ? "<无主题>" : data.get(position).getSubject());
        holder.select.setChecked(data.get(position).isSelect());
        if (!data.get(position).getAttachments().isEmpty()) {
            holder.img_file.setVisibility(View.VISIBLE);
        } else {
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

    /**
     * 拼接收件人
     *
     * @param receiver
     * @return
     */
    private String getReceiver(List<MailNew.ResultBean.MailsBean.ReceiveBean> receiver) {
        StringBuffer sb = new StringBuffer();
        if(receiver.isEmpty()){
            return "";
        }
        for (MailNew.ResultBean.MailsBean.ReceiveBean bean : receiver) {
            sb.append(bean.getUserName());
            sb.append("，");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }
}
