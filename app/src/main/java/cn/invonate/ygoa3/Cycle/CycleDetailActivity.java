package cn.invonate.ygoa3.Cycle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.CommentAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Comment;
import cn.invonate.ygoa3.Entry.Like;
import cn.invonate.ygoa3.Entry.Lomo;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.View.LinearLayoutForListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import cn.invonate.ygoa3.main.BasePicActivity;
import cn.invonate.ygoa3.main.VideoActivity;

public class CycleDetailActivity extends BaseActivity {

    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.news_pic)
    ImageView newsPic;
    @BindView(R.id.img_play)
    ImageView img_play;
    @BindView(R.id.news_pic2)
    ImageView newsPic2;
    @BindView(R.id.news_pic3)
    ImageView newsPic3;
    @BindView(R.id.layout_imgs1)
    LinearLayout layout_imgs;
    @BindView(R.id.news_pic4)
    ImageView newsPic4;
    @BindView(R.id.news_pic5)
    ImageView newsPic5;
    @BindView(R.id.news_pic6)
    ImageView newsPic6;
    @BindView(R.id.layout_imgs2)
    LinearLayout layout_imgs2;
    @BindView(R.id.news_pic7)
    ImageView newsPic7;
    @BindView(R.id.news_pic8)
    ImageView newsPic8;
    @BindView(R.id.news_pic9)
    ImageView newsPic9;
    @BindView(R.id.layout_imgs3)
    LinearLayout layout_imgs3;
    @BindView(R.id.img_zan)
    ImageView imgZan;
    @BindView(R.id.sum_zan)
    TextView sumZan;
    @BindView(R.id.img_content)
    ImageView imgContent;
    @BindView(R.id.sum_content)
    TextView sumContent;

    @BindView(R.id.list_comments)
    LinearLayoutForListView listComments;
    @BindView(R.id.et_comment)
    EditText etComment;

    Lomo.LomoBean bean;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        bean = (Lomo.LomoBean) getIntent().getExtras().getSerializable("cycle");
        initValue();
        getComments();
    }

    private void initValue() {
        if (bean.getIS_ANONYMOUS() == 1) {
            head.setImageResource(R.mipmap.pic_head);
            head.setOnClickListener(null);
        } else {
            Glide.with(this)
                    .load(HttpUtil.URL_FILE + bean.getUser_photo())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                    .dontAnimate().skipMemoryCache(true).into(head);
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CycleDetailActivity.this, PersonalCycleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", bean.getUSER_ID());
                    bundle.putString("user_pic", bean.getUser_photo());
                    bundle.putString("user_name", bean.getUSER_NAME());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        if (bean.getIS_ANONYMOUS() == 1) {
            name.setText("匿名");
        } else {
            name.setText(bean.getUSER_NAME());
        }
        time.setText(bean.getPUBLISH_TIME());
        try {
            content.setText(new String(Base64.decode(bean.getLOMO_CONTENT(), Base64.DEFAULT), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(bean.getLOMO_VIDEO())) {//视频
            layout_imgs.setVisibility(View.VISIBLE);
            layout_imgs2.setVisibility(View.GONE);
            layout_imgs3.setVisibility(View.GONE);
            img_play.setVisibility(View.VISIBLE);
            newsPic.setImageBitmap(getNetVideoBitmap(HttpUtil.URL_FILE + bean.getLOMO_VIDEO()));
            newsPic2.setVisibility(View.INVISIBLE);
            newsPic3.setVisibility(View.INVISIBLE);
            newsPic4.setVisibility(View.INVISIBLE);
            newsPic5.setVisibility(View.INVISIBLE);
            newsPic6.setVisibility(View.INVISIBLE);
            newsPic7.setVisibility(View.INVISIBLE);
            newsPic8.setVisibility(View.INVISIBLE);
            newsPic9.setVisibility(View.INVISIBLE);
            newsPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CycleDetailActivity.this, VideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", bean.getLOMO_VIDEO());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else if (!bean.getList_imgs().isEmpty()) {// 图片
            List<String> list_imgs = bean.getList_imgs();
            img_play.setVisibility(View.GONE);
            newsPic.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 0));
            newsPic2.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 1));
            newsPic3.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 2));
            newsPic4.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 3));
            newsPic5.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 4));
            newsPic6.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 5));
            newsPic7.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 6));
            newsPic8.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 7));
            newsPic9.setOnClickListener(new ImageClickListener(bean.getList_imgs(), 8));
            switch (list_imgs.size()) {
                case 1:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.GONE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.INVISIBLE);
                    newsPic3.setVisibility(View.INVISIBLE);
                    newsPic4.setVisibility(View.INVISIBLE);
                    newsPic5.setVisibility(View.INVISIBLE);
                    newsPic6.setVisibility(View.INVISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.GONE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.INVISIBLE);
                    newsPic4.setVisibility(View.INVISIBLE);
                    newsPic5.setVisibility(View.INVISIBLE);
                    newsPic6.setVisibility(View.INVISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.GONE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.INVISIBLE);
                    newsPic5.setVisibility(View.INVISIBLE);
                    newsPic6.setVisibility(View.INVISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.INVISIBLE);
                    newsPic6.setVisibility(View.INVISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(newsPic5);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.VISIBLE);
                    newsPic6.setVisibility(View.INVISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 6:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(newsPic5);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(newsPic6);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.VISIBLE);
                    newsPic6.setVisibility(View.VISIBLE);
                    newsPic7.setVisibility(View.INVISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 7:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(newsPic5);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(newsPic6);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(newsPic7);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.VISIBLE);
                    newsPic6.setVisibility(View.VISIBLE);
                    newsPic7.setVisibility(View.VISIBLE);
                    newsPic8.setVisibility(View.INVISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 8:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(newsPic5);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(newsPic6);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(newsPic7);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(7)).into(newsPic8);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.VISIBLE);
                    newsPic6.setVisibility(View.VISIBLE);
                    newsPic7.setVisibility(View.VISIBLE);
                    newsPic8.setVisibility(View.VISIBLE);
                    newsPic9.setVisibility(View.INVISIBLE);
                    break;
                case 9:
                    layout_imgs.setVisibility(View.VISIBLE);
                    layout_imgs2.setVisibility(View.VISIBLE);
                    layout_imgs3.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(newsPic);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(newsPic2);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(newsPic3);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(newsPic4);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(newsPic5);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(newsPic6);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(newsPic7);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(7)).into(newsPic8);
                    Glide.with(getApplicationContext()).load(HttpUtil.URL_FILE + list_imgs.get(8)).into(newsPic9);
                    newsPic.setVisibility(View.VISIBLE);
                    newsPic2.setVisibility(View.VISIBLE);
                    newsPic3.setVisibility(View.VISIBLE);
                    newsPic4.setVisibility(View.VISIBLE);
                    newsPic5.setVisibility(View.VISIBLE);
                    newsPic6.setVisibility(View.VISIBLE);
                    newsPic7.setVisibility(View.VISIBLE);
                    newsPic8.setVisibility(View.VISIBLE);
                    newsPic9.setVisibility(View.VISIBLE);
                    break;
            }
        } else { // 仅文字
            layout_imgs.setVisibility(View.GONE);
            layout_imgs2.setVisibility(View.GONE);
            layout_imgs3.setVisibility(View.GONE);
        }
        int sum_zan = bean.getTh_num();
        sumZan.setText(sum_zan + "");
        String thum_up = bean.getThumb_up();
        if (thum_up.equals("0")) {
            imgZan.setImageResource(R.mipmap.heat0);
            imgZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLike(bean.getLOMO_ID());
                }
            });
        } else {
            imgZan.setImageResource(R.mipmap.heat1);
            imgZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelLike(bean.getThumb_up());
                }
            });
        }
        sumContent.setText(bean.getInfo_num() + "");
    }

    @OnClick({R.id.pic_back, R.id.txt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.txt_send:
                sendComment(etComment.getText().toString().trim());
                break;
        }
    }

    class ImageClickListener implements View.OnClickListener {

        private ArrayList<String> list_imgs;
        private int index;

        public ImageClickListener(ArrayList<String> list_imgs, int index) {
            this.list_imgs = list_imgs;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CycleDetailActivity.this, BasePicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putStringArrayList("imgs", list_imgs);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 获取视频第一帧
     *
     * @param videoUrl
     * @return
     */
    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取评论
     */
    private void getComments() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Comment>() {
            @Override
            public void onNext(Comment data) {
                Log.i("getComments", data.toString());
                if (data.getResult() == 0) {
                    CommentAdapter adapter = new CommentAdapter(data.getCommList(), CycleDetailActivity.this);
                    listComments.setAdapter(adapter);
                }
            }
        };
        HttpUtil.getInstance(this, false).getComments(new ProgressSubscriber(onNextListener, this), bean.getLOMO_ID());
    }

    /**
     * 发表评论
     *
     * @param comments
     */
    private void sendComment(String comments) {
        if (comments.length() == 0) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("sendComment", data);
                getComments();
                etComment.clearFocus();
                etComment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
        HttpUtil.getInstance(this, false).sendComments(new ProgressSubscriber(onNextListener, this, "发送中"), app.getUser().getUser_id(), Base64.encodeToString(comments.getBytes(), Base64.DEFAULT), bean.getLOMO_ID());
    }

    /**
     * 随手拍点赞
     *
     * @param lomo_id
     */
    private void setLike(String lomo_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Like>() {
            @Override
            public void onNext(Like data) {
                Log.i("setLike", data.toString());
                if (data.getResult() == 0) {
                    bean.setThumb_up(data.getThumb_up());
                    bean.setTh_num(bean.getTh_num() + 1);
                    sumZan.setText(bean.getTh_num() + "");
                    imgZan.setImageResource(R.mipmap.heat1);
                    imgZan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelLike(bean.getThumb_up());
                        }
                    });
                } else {

                }
            }
        };
        HttpUtil.getInstance(this, false).setLike(new ProgressSubscriber(onNextListener, this), app.getUser().getUser_id(), lomo_id);
    }

    /**
     * 取消赞
     *
     * @param thumb_id
     */
    private void cancelLike(String thumb_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Like>() {
            @Override
            public void onNext(Like data) {
                Log.i("cancelLike", data.toString());
                if (data.getResult() == 0) {
                    bean.setThumb_up("0");
                    bean.setTh_num(bean.getTh_num() - 1);
                    sumZan.setText(bean.getTh_num() + "");
                    imgZan.setImageResource(R.mipmap.heat0);
                    imgZan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setLike(bean.getLOMO_ID());
                        }
                    });
                } else {

                }
            }
        };
        HttpUtil.getInstance(this, false).cancelLike(new ProgressSubscriber(onNextListener, this), thumb_id);
    }

}
