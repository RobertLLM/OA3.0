package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.List;

import cn.invonate.ygoa3.Entry.FileEntry;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2017/10/30.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    public static final int TYPE_IMG = 0;
    public static final int TYPE_OTHER = 1;

    private static String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
    DecimalFormat df = new DecimalFormat("######0.00");
    private List<FileEntry> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public FileAdapter(List<FileEntry> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileViewHolder holder = new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_IMG){
            Bitmap bitmap = BitmapFactory.decodeByteArray(data.get(position).getIs(), 0, data.get(position).getIs().length);
            holder.pic.setImageBitmap(bitmap);
        }
        holder.name.setText(data.get(position).getName());
        holder.size.setText(df.format((double) data.get(position).getSize() / 1024 / 1024) + "M");
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    try {
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        for (String img : img) {
            if (data.get(position).getType().equals(img)) {
                return TYPE_IMG;
            }
        }
        return TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return data.size();
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
        void onItemClick(View view, int position) throws FileNotFoundException;
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
