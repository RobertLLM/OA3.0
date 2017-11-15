package cn.invonate.ygoa.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.invonate.ygoa.Entry.FileEntry;
import cn.invonate.ygoa.R;

/**
 * Created by liyangyang on 2017/10/30.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.FileViewHolder> {
    public static final int TYPE_IMG = 0;
    public static final int TYPE_OTHER = 1;

    private static String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
    DecimalFormat df = new DecimalFormat("######0.00");
    private Context context;
    private List<Object> path;
    private OnItemClickListener onItemClickListener;

    public PhotoAdapter(List<Object> data, Context context) {
        this.path = data;
        this.context = context;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileViewHolder holder = new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        FileEntry entry = null;
        if (getItemViewType(position) == TYPE_IMG) {
            Uri uri = Uri.fromFile(new File((String) path.get(position)));
            Glide.with(context)
                    .load(uri)
                    .into(holder.pic);
            entry = new FileEntry();
            File file = new File((String) path.get(position));
            entry.setName(file.getName());
            entry.setType(((String) path.get(position)).substring(((String) path.get(position)).lastIndexOf(".") + 1, ((String) path.get(position)).length()).toLowerCase());
            entry.setSize(file.length());
            holder.name.setText(entry.getName());
            holder.size.setText(df.format((double) file.length() / 1024 / 1024) + "M");
        } else {
            entry = (FileEntry) path.get(position);
            holder.name.setText(entry.getName());
            holder.size.setText(df.format((double) ((FileEntry) path.get(position)).getSize() / 1024 / 1024) + "M");
        }

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
    public int getItemViewType(int position) {
        Object obj = path.get(position);
        if (obj instanceof String) {
            return TYPE_IMG;
        }
        return TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return path.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView name;
        private TextView size;

        public FileViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            AutoUtils.autoSize(itemView);
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
     * @param mOnItemClicksListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClicksListener) {
        this.onItemClickListener = mOnItemClicksListener;
    }

}
