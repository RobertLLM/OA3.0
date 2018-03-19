package cn.invonate.ygoa3.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.io.FileOutputStream;

import cn.invonate.ygoa3.PhotoView.PhotoViewAttacher;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.httpUtil.HttpUtil;

/**
 * Created by liyangyang on 2018/3/15.
 */

public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

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
        Glide.with(ImageDetailFragment.this)
                .load(HttpUtil.URL_FILE + mImageUrl)
                //.asBitmap()
                //.error(R.mipmap.pic_loading_error)

                .into(new GlideDrawableImageViewTarget(mImageView) {
                    @Override
                    public void onResourceReady(final GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作
                        progressBar.setVisibility(View.GONE);
                        mAttacher.update();
//                        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View v) {
//                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
//                                builder.setTitle("是否将图片保存至相册?")
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                saveMyBitmap(getActivity(), drawableToBitmap(drawable));
//                                            }
//                                        }).setNegativeButton("取消",null)
//                                        .create().show();
//                                return true;
//                            }
//                        });

                    }
                });

    }

    //保存文件到指定路径
    public void saveMyBitmap(Context context, Bitmap bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "智慧永联");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "zhyl" + System.currentTimeMillis() + ".jpg";
        File f = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "图片已保存至相册", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);

    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
}
