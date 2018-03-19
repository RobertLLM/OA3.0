package cn.invonate.ygoa3.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.Lomo;
import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/2/22.
 */

public class PicFragment extends Fragment {
    @BindView(R.id.list_news)
    LYYPullToRefreshListView listNews;
    Unbinder unbinder;

    private LomoAdapter adapter;

    private int total;

    private List<Lomo.LomoBean> list_lomo;

    private YGApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_pic, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        listNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getLomoList(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        getLomoList(1);
        return view;
    }

    /**
     * @param page
     */
    private void getLomoList(final int page) {
        Subscriber subscriber = new Subscriber<Lomo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(Lomo data) {
                total = data.getTotal();
                data.initImage();
                Log.i("getLomoList", data.toString());
                if (page == 1) {
                    list_lomo = data.getRows();
                    adapter = new LomoAdapter(list_lomo, getActivity(), app.getUser());
                    listNews.setAdapter(adapter);
                    listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
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
        HttpUtil.getInstance(getActivity(), false).getLomoList(subscriber, page, 20);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                holder.user.setText(user.getUser_name());
                Glide.with(context)
                        .load(HttpUtil.URL_FILE + user.getUser_photo())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                        .skipMemoryCache(true).into(holder.img_user);
            } else {
                Glide.with(context)
                        .load(HttpUtil.URL_FILE + data.get(position - 1).getUser_photo())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                        .skipMemoryCache(true).into(holder.head);
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
                    holder.newsPic.setImageBitmap(getNetVideoBitmap(HttpUtil.URL_FILE + data.get(position - 1).getLOMO_VIDEO()));
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
                            Intent intent = new Intent(getActivity(), VideoActivity.class);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(holder.newsPic5);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(holder.newsPic6);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(holder.newsPic7);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(holder.newsPic7);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(7)).into(holder.newsPic8);
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
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(0)).into(holder.newsPic);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(1)).into(holder.newsPic2);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(2)).into(holder.newsPic3);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(3)).into(holder.newsPic4);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(4)).into(holder.newsPic5);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(5)).into(holder.newsPic6);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(6)).into(holder.newsPic7);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(7)).into(holder.newsPic8);
                            Glide.with(context).load(HttpUtil.URL_FILE + list_imgs.get(8)).into(holder.newsPic9);
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
                if (sum_zan > 0) {
                    holder.imgZan.setImageResource(R.mipmap.heat1);
                } else {
                    holder.imgZan.setImageResource(R.mipmap.heat0);
                }
                holder.sumZan.setText(sum_zan + "");

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
                Intent intent = new Intent(getActivity(), BasePicActivity.class);
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
    }


}
