package cn.invonate.ygoa3.Cycle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Like;
import cn.invonate.ygoa3.Entry.Lomo;
import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import cn.invonate.ygoa3.main.BasePicActivity;
import cn.invonate.ygoa3.main.VideoActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PersonalCycleActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.list_news)
    LYYPullToRefreshListView listNews;

    private LomoAdapter adapter;

    private int total;

    private List<Lomo.LomoBean> list_lomo;

    private YGApplication app;

    private String user_id;
    private String user_pic;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_cycle);
        ButterKnife.bind(this);
        user_id = getIntent().getExtras().getString("user_id");
        user_pic = getIntent().getExtras().getString("user_pic");
        user_name = getIntent().getExtras().getString("user_name");
        title.setText(user_name + "的随手拍");
        app = (YGApplication) getApplication();
        listNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMyLomoList(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        getMyLomoList(1);
    }

    /**
     * @param page
     */
    private void getMyLomoList(final int page) {
        Subscriber subscriber = new Subscriber<Lomo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(final Lomo data) {
                total = data.getTotal();
                data.initImage();
                Log.i("getLomoList", data.toString());
                if (page == 1) {
                    list_lomo = data.getRows();
                    adapter = new LomoAdapter(list_lomo, PersonalCycleActivity.this, app.getUser());
                    listNews.setAdapter(adapter);
                    listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 1) {
                                Intent intent = new Intent(PersonalCycleActivity.this, CycleDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("cycle", data.getRows().get(position - 2));
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0x999);
                            }
                        }
                    });
                } else {
                    list_lomo.addAll(data.getRows());
                    adapter.notifyDataSetChanged();
                }
                listNews.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listNews.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listNews.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance(this, false).getMyLomoList(subscriber, page, 20, app.getUser().getUser_id(), user_id);
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    /**
     *
     */
    public class LomoAdapter extends BaseAdapter {
        private List<Lomo.LomoBean> data;
        private Context context;
        private User user;

        public LomoAdapter(List<Lomo.LomoBean> data, Context context, User user) {
            this.data = data;
            this.context = context;
            this.user = user;
        }

        @Override
        public int getCount() {
            return data.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (position == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lomo_head, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lomo, null);
            }
            holder = new ViewHolder(convertView);
            if (position == 0) {
                holder.user.setText(user_name);
                Glide.with(context)
                        .load(HttpUtil.URL_FILE + user_pic)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                        .skipMemoryCache(true).into(holder.img_user);
            } else {
                Glide.with(context)
                        .load(HttpUtil.URL_FILE + data.get(position - 1).getUser_photo())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                        .dontAnimate().skipMemoryCache(true).into(holder.head);
                holder.name.setText(data.get(position - 1).getUSER_NAME());
                holder.time.setText(data.get(position - 1).getPUBLISH_TIME());
                try {
                    holder.content.setText(new String(Base64.decode(data.get(position - 1).getLOMO_CONTENT(), Base64.DEFAULT), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!"".equals(data.get(position - 1).getLOMO_VIDEO())) {//视频
                    holder.layout_imgs.setVisibility(View.VISIBLE);
                    holder.layout_imgs2.setVisibility(View.GONE);
                    holder.layout_imgs3.setVisibility(View.GONE);
                    holder.img_play.setVisibility(View.VISIBLE);
                    holder.newsPic.setImageResource(R.drawable.image_init);
                    setVideoImage(holder.newsPic, HttpUtil.URL_FILE + data.get(position - 1).getLOMO_VIDEO());
//                    new Thread(new ImageThread(holder.newsPic, HttpUtil.URL_FILE + data.get(position - 1).getLOMO_VIDEO())).start();
//                    holder.newsPic.setImageBitmap(getNetVideoBitmap(HttpUtil.URL_FILE + data.get(position - 1).getLOMO_VIDEO()));
                    holder.newsPic2.setVisibility(View.INVISIBLE);
                    holder.newsPic3.setVisibility(View.INVISIBLE);
                    holder.newsPic4.setVisibility(View.INVISIBLE);
                    holder.newsPic5.setVisibility(View.INVISIBLE);
                    holder.newsPic6.setVisibility(View.INVISIBLE);
                    holder.newsPic7.setVisibility(View.INVISIBLE);
                    holder.newsPic8.setVisibility(View.INVISIBLE);
                    holder.newsPic9.setVisibility(View.INVISIBLE);
                    holder.newsPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PersonalCycleActivity.this, VideoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", data.get(position - 1).getLOMO_VIDEO());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else if (!data.get(position - 1).getList_imgs().isEmpty()) {// 图片
                    List<String> list_imgs = data.get(position - 1).getList_imgs();
                    holder.img_play.setVisibility(View.GONE);
                    holder.newsPic.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 0));
                    holder.newsPic2.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 1));
                    holder.newsPic3.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 2));
                    holder.newsPic4.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 3));
                    holder.newsPic5.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 4));
                    holder.newsPic6.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 5));
                    holder.newsPic7.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 6));
                    holder.newsPic8.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 7));
                    holder.newsPic9.setOnClickListener(new ImageClickListener(data.get(position - 1).getList_imgs(), 8));
                    switch (list_imgs.size()) {
                        case 1:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.GONE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.INVISIBLE);
                            holder.newsPic3.setVisibility(View.INVISIBLE);
                            holder.newsPic4.setVisibility(View.INVISIBLE);
                            holder.newsPic5.setVisibility(View.INVISIBLE);
                            holder.newsPic6.setVisibility(View.INVISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;

                        case 2:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.GONE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.INVISIBLE);
                            holder.newsPic4.setVisibility(View.INVISIBLE);
                            holder.newsPic5.setVisibility(View.INVISIBLE);
                            holder.newsPic6.setVisibility(View.INVISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.GONE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.INVISIBLE);
                            holder.newsPic5.setVisibility(View.INVISIBLE);
                            holder.newsPic6.setVisibility(View.INVISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.INVISIBLE);
                            holder.newsPic6.setVisibility(View.INVISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).dontAnimate().into(holder.newsPic5);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.VISIBLE);
                            holder.newsPic6.setVisibility(View.INVISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 6:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.GONE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).dontAnimate().into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).dontAnimate().into(holder.newsPic6);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.VISIBLE);
                            holder.newsPic6.setVisibility(View.VISIBLE);
                            holder.newsPic7.setVisibility(View.INVISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 7:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.VISIBLE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).dontAnimate().into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).dontAnimate().into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).dontAnimate().into(holder.newsPic7);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.VISIBLE);
                            holder.newsPic6.setVisibility(View.VISIBLE);
                            holder.newsPic7.setVisibility(View.VISIBLE);
                            holder.newsPic8.setVisibility(View.INVISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 8:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.VISIBLE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).dontAnimate().into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).dontAnimate().into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).dontAnimate().into(holder.newsPic7);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(7)).dontAnimate().into(holder.newsPic8);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.VISIBLE);
                            holder.newsPic6.setVisibility(View.VISIBLE);
                            holder.newsPic7.setVisibility(View.VISIBLE);
                            holder.newsPic8.setVisibility(View.VISIBLE);
                            holder.newsPic9.setVisibility(View.INVISIBLE);
                            break;
                        case 9:
                            holder.layout_imgs.setVisibility(View.VISIBLE);
                            holder.layout_imgs2.setVisibility(View.VISIBLE);
                            holder.layout_imgs3.setVisibility(View.VISIBLE);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).dontAnimate().into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).dontAnimate().into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).dontAnimate().into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).dontAnimate().into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).dontAnimate().into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).dontAnimate().into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).dontAnimate().into(holder.newsPic7);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(7)).dontAnimate().into(holder.newsPic8);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(8)).dontAnimate().into(holder.newsPic9);
                            holder.newsPic.setVisibility(View.VISIBLE);
                            holder.newsPic2.setVisibility(View.VISIBLE);
                            holder.newsPic3.setVisibility(View.VISIBLE);
                            holder.newsPic4.setVisibility(View.VISIBLE);
                            holder.newsPic5.setVisibility(View.VISIBLE);
                            holder.newsPic6.setVisibility(View.VISIBLE);
                            holder.newsPic7.setVisibility(View.VISIBLE);
                            holder.newsPic8.setVisibility(View.VISIBLE);
                            holder.newsPic9.setVisibility(View.VISIBLE);
                            break;
                    }
                } else { // 仅文字
                    holder.layout_imgs.setVisibility(View.GONE);
                    holder.layout_imgs2.setVisibility(View.GONE);
                    holder.layout_imgs3.setVisibility(View.GONE);
                }
                int sum_zan = data.get(position - 1).getTh_num();
//                if (sum_zan > 0) {
//                    holder.imgZan.setImageResource(R.mipmap.heat1);
//                } else {
//                    holder.imgZan.setImageResource(R.mipmap.heat0);
//                }
                holder.sumZan.setText(sum_zan + "");

                String thum_up = data.get(position - 1).getThumb_up();
                if (thum_up.equals("0")) {
                    holder.imgZan.setImageResource(R.mipmap.heat0);
                    holder.imgZan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setLike(position - 1, data.get(position - 1).getLOMO_ID());
                        }
                    });
                } else {
                    holder.imgZan.setImageResource(R.mipmap.heat1);
                    holder.imgZan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelLike(position - 1, data.get(position - 1).getThumb_up());
                        }
                    });
                }
                holder.sumContent.setText(data.get(position - 1).getInfo_num() + "");

            }
            return convertView;
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
                Intent intent = new Intent(PersonalCycleActivity.this, BasePicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putStringArrayList("imgs", list_imgs);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        class ViewHolder {
            @Nullable
            @BindView(R.id.user)
            TextView user;

            @Nullable
            @BindView(R.id.img_user)
            CircleImageView img_user;

            @Nullable
            @BindView(R.id.head)
            CircleImageView head;

            @Nullable
            @BindView(R.id.name)
            TextView name;

            @Nullable
            @BindView(R.id.time)
            TextView time;

            @Nullable
            @BindView(R.id.content)
            TextView content;

            @Nullable
            @BindView(R.id.img_zan)
            ImageView imgZan;

            @Nullable
            @BindView(R.id.sum_zan)
            TextView sumZan;

            @Nullable
            @BindView(R.id.img_content)
            ImageView imgContent;

            @Nullable
            @BindView(R.id.sum_content)
            TextView sumContent;

            @Nullable
            @BindView(R.id.layout_imgs1)
            LinearLayout layout_imgs;

            @Nullable
            @BindView(R.id.layout_imgs2)
            LinearLayout layout_imgs2;

            @Nullable
            @BindView(R.id.layout_imgs3)
            LinearLayout layout_imgs3;

            @Nullable
            @BindView(R.id.img_play)
            ImageView img_play;

            @Nullable
            @BindView(R.id.news_pic)
            ImageView newsPic;

            @Nullable
            @BindView(R.id.news_pic2)
            ImageView newsPic2;

            @Nullable
            @BindView(R.id.news_pic3)
            ImageView newsPic3;

            @Nullable
            @BindView(R.id.news_pic4)
            ImageView newsPic4;

            @Nullable
            @BindView(R.id.news_pic5)
            ImageView newsPic5;

            @Nullable
            @BindView(R.id.news_pic6)
            ImageView newsPic6;

            @Nullable
            @BindView(R.id.news_pic7)
            ImageView newsPic7;

            @Nullable
            @BindView(R.id.news_pic8)
            ImageView newsPic8;

            @Nullable
            @BindView(R.id.news_pic9)
            ImageView newsPic9;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
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

    private void setVideoImage(final ImageView imageView, final String url) {

        Observer<Bitmap> observer = new Observer<Bitmap>() {
            @Override
            public void onNext(Bitmap bit) {
                imageView.setImageBitmap(bit);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
        Observable observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bit = getNetVideoBitmap(url);
                subscriber.onNext(bit);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()); // 指定 Subscriber 的回调发生在主线程;

        observable.subscribe(observer);
    }

    /**
     * 随手拍点赞
     *
     * @param index
     * @param lomo_id
     */
    private void setLike(final int index, String lomo_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Like>() {
            @Override
            public void onNext(Like data) {
                Log.i("setLike", data.toString());
                if (data.getResult() == 0) {
                    list_lomo.get(index).setThumb_up(data.getThumb_up());
                    list_lomo.get(index).setTh_num(list_lomo.get(index).getTh_num() + 1);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }
        };
        HttpUtil.getInstance(this, false).setLike(new ProgressSubscriber(onNextListener, this), app.getUser().getUser_id(), lomo_id);
    }

    /**
     * 取消赞
     *
     * @param index
     * @param thumb_id
     */
    private void cancelLike(final int index, String thumb_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Like>() {
            @Override
            public void onNext(Like data) {
                Log.i("cancelLike", data.toString());
                if (data.getResult() == 0) {
                    list_lomo.get(index).setThumb_up("0");
                    list_lomo.get(index).setTh_num(list_lomo.get(index).getTh_num() - 1);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }
        };
        HttpUtil.getInstance(this, false).cancelLike(new ProgressSubscriber(onNextListener, this), thumb_id);
    }
}
