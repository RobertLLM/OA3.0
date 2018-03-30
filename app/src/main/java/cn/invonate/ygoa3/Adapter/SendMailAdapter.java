package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2018/3/28.
 */

public class SendMailAdapter extends RecyclerView.Adapter<SendMailAdapter.ViewHolder> {
    private static final int TYPE_NOMAL = 0;
    private static final int TYPE_INOPUT = 1;

    List<Contacts> data;
    private Context context;

    public SendMailAdapter(List<Contacts> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public SendMailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        switch (viewType) {
            case TYPE_NOMAL:
                view = LayoutInflater.from(context).inflate(R.layout.item_send_mail, parent, false);
                break;
            case TYPE_INOPUT:
                view = LayoutInflater.from(context).inflate(R.layout.item_send_input, parent, false);
                break;

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SendMailAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NOMAL:
                holder.name.setText(data.get(position).getUser_name());
                break;
            case TYPE_INOPUT:
//                holder;
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return TYPE_INOPUT;
        } else {
            return TYPE_NOMAL;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.name)
        TextView name;

        @Nullable
        @BindView(R.id.address)
        AutoCompleteTextView address;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
