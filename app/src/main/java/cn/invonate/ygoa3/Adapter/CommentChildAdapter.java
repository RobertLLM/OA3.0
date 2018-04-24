package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Comment;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;

public class CommentChildAdapter extends BaseAdapter {
    private List<Comment.CommList.Reply> data;
    private Context context;

    public CommentChildAdapter(List<Comment.CommList.Reply> data, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_child, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            convertView.setTag(convertView);
        }
        Glide.with(context)
                .load(HttpUtil.URL_FILE + data.get(position).getUser_photo())
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .dontAnimate()
                .skipMemoryCache(true)
                .into(holder.head);
        holder.commentsAuthor.setText(data.get(position).getUSER_NAME());
        holder.commentsContent.setText(new String(Base64.decode(data.get(position).getReplay_content(), Base64.DEFAULT)));
        holder.commentsTime.setText(data.get(position).getReplay_time());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.head)
        CircleImageView head;
        @BindView(R.id.comments_author)
        TextView commentsAuthor;
        @BindView(R.id.comments_time)
        TextView commentsTime;
        @BindView(R.id.comments_content)
        TextView commentsContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
