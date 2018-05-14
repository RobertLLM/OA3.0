package cn.invonate.ygoa3.Cycle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.PhotoPickerAdapter;
import cn.invonate.ygoa3.Adapter.RecyclerItemClickListener;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Like;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.ImageUtils;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class AddCycleActivity extends BaseActivity {

    @BindView(R.id.list_pic)
    RecyclerView listPic;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.no_name)
    CheckBox noName;

    private List<String> images;

    YGApplication app;
    ProgressDialog dialog;

    PhotoPickerAdapter adapter;

    private ArrayList<String> photoPaths = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cycle);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(AddCycleActivity.this);
        app = (YGApplication) getApplication();
        listPic.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        adapter = new PhotoPickerAdapter(this, photoPaths);
        listPic.setAdapter(adapter);
        listPic.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (adapter.getItemViewType(position) == PhotoPickerAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoPickerAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(photoPaths)
                                    .start(AddCycleActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(photoPaths)
                                    .setCurrentItem(position)
                                    .start(AddCycleActivity.this);
                        }
                    }
                }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            photoPaths.clear();
            if (photos != null) {
                photoPaths.addAll(photos);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void addLomoImage(String content, int no_name) {
        String imgs = "";
        for (String i : images) {
            imgs += i + ",";
        }
        imgs = imgs.substring(0, imgs.length() - 1);
        SubscriberOnNextListener subscriberOnNextListener = new SubscriberOnNextListener<Like>() {
            @Override
            public void onNext(Like data) {
                Log.i("addLomoImage", data.toString());
                if (data.getResult() == 0) {
                    Toast.makeText(AddCycleActivity.this, "已发布成功并等待审核", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(AddCycleActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                }

            }
        };
        HttpUtil.getInstance(this, false).addLomoImage(new ProgressSubscriber(subscriberOnNextListener, this, "发布中"), app.getUser().getUser_id(), Base64.encodeToString(content.getBytes(), Base64.DEFAULT), no_name, imgs);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.setMessage("压缩中");
            switch (msg.what) {
                case 0:
                    dialog.show();
                    break;
                case 1:
                    if (dialog.isShowing()) dialog.dismiss();
                    addLomoImage(content.getText().toString().trim(), noName.isChecked() ? 1 : 0);
                    break;
            }
        }
    };

    @OnClick({R.id.pic_back, R.id.layout_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_add:
                new WorkThread().start();
                break;
        }
    }

    /**
     * 用于执行BitMap转Base64操作
     */
    class WorkThread extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            super.run();
            images = new ArrayList<>();
            for (int i = 0; i < photoPaths.size(); i++) {
                try {
                    images.add(ImageUtils.bitmapToString(photoPaths.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("base64", JSON.toJSONString(images));
            handler.sendEmptyMessage(1);
        }
    }
}
