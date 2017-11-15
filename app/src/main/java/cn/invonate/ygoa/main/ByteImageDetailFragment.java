package cn.invonate.ygoa.main;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import cn.invonate.ygoa.Entry.Mail;
import cn.invonate.ygoa.PhotoView.PhotoViewAttacher;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.MailHolder;


public class ByteImageDetailFragment extends Fragment {
    private int position;// 标记第几份邮件
    private int index;// 标记第几张图片
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ByteImageDetailFragment newInstance(int position, int index) {
        final ByteImageDetailFragment f = new ByteImageDetailFragment();
        final Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("position") : 0;
        index = getArguments() != null ? getArguments().getInt("index") : 0;
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
        Mail mail = MailHolder.mail_model.get(index);
        byte[] data = mail.getAttachmentsInputStreams().get(position);
        mImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        progressBar.setVisibility(View.GONE);
        mAttacher.update();
    }

}
