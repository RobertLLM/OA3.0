package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.httpUtil.HttpUtil;

/**
 * Created by liyangyang on 2018/3/19.
 */

public class AddGroupDetailAdapter extends RecyclerView.Adapter<AddGroupDetailAdapter.ViewHolder> {

    private List<Contacts> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AddGroupDetailAdapter(List<Contacts> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public AddGroupDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_groupdetail, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final AddGroupDetailAdapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getUser_name());
        holder.depart.setText(data.get(position).getDepartment_name());
        holder.check.setVisibility(View.GONE);
        holder.check.setChecked(data.get(position).isIs_select());
        Glide.with(context).load(HttpUtil.URL_FILE + data.get(position).getUser_photo()).into(holder.head);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check)
        CheckBox check;
        @BindView(R.id.head)
        CircleImageView head;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.depart)
        TextView depart;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 点击监听
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 设置监听
     *
     * @param mOnItemClickLitener
     */
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.onItemClickListener = mOnItemClickLitener;
    }

}
