package cn.invonate.ygoa3.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.invonate.ygoa3.Entry.MailNew;
import cn.invonate.ygoa3.R;

/**
 * Created by liyangyang on 2017/10/30.
 */

public class PhotoNewAdapter extends RecyclerView.Adapter<PhotoNewAdapter.FileViewHolder> {
    public static final int TYPE_IMG = 0;
    public static final int TYPE_OTHER = 1;

    private static String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
    DecimalFormat df = new DecimalFormat("######0.00");
    private Context context;
    private List<MailNew.ResultBean.MailsBean.AttachmentsBean> data;
    private OnItemClickListener onItemClickListener;
    private OnDeleteItemClickListener onDeleteItemClickListener;

    public PhotoNewAdapter(List<MailNew.ResultBean.MailsBean.AttachmentsBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileViewHolder holder = new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_IMG) {
            if (data.get(position).getType() == 1) {
                Uri uri = Uri.fromFile(new File(data.get(position).getPath()));
                Glide.with(context)
                        .load(uri)
                        //.skipMemoryCache(true)
                        .into(holder.pic);
            }
        }
        holder.name.setText(data.get(position).getFileName());
        holder.size.setText(df.format((double) data.get(position).getSize() / 1024) + "M");
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onDeleteItemClickListener.onDeleteItemClick(holder.itemView, pos);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        MailNew.ResultBean.MailsBean.AttachmentsBean obj = data.get(position);
        if (checkImg(obj.getFileName().substring(obj.getFileName().lastIndexOf(".") + 1, obj.getFileName().length()))) {
            return TYPE_IMG;
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
        private Button delete;

        public FileViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            delete = itemView.findViewById(R.id.btn_delete);
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

    /**
     * 监测是否图片
     *
     * @param name
     * @return
     */
    private boolean checkImg(String name) {
        for (String img : img) {
            if (name.equals(img)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击监听
     */
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(View view, int position);
    }

    /**
     * 设置监听
     *
     * @param onDeleteItemClickListener
     */
    public void setOnDeleteItemClickListener(OnDeleteItemClickListener onDeleteItemClickListener) {
        this.onDeleteItemClickListener = onDeleteItemClickListener;
    }

}
