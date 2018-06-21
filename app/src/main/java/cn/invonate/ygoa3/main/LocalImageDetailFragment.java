package cn.invonate.ygoa3.main;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;

import cn.invonate.ygoa3.PhotoView.PhotoViewAttacher;
import cn.invonate.ygoa3.R;


public class LocalImageDetailFragment extends Fragment {
    private String name;// 文件名
    private int index;// 标记第几张图片
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static LocalImageDetailFragment newInstance(String name) {
        final LocalImageDetailFragment f = new LocalImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("name", name);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments() != null ? getArguments().getString("name") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        byte[] data = mail.getAttachmentsInputStreams().get(position);
//        mImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        Glide.with(this).load(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name))
                .into(new GlideDrawableImageViewTarget(mImageView) {
                    @Override
                    public void onResourceReady(final GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作
                        progressBar.setVisibility(View.GONE);
                        mAttacher.update();
                    }
                });
        progressBar.setVisibility(View.GONE);
        mAttacher.update();
    }

}
