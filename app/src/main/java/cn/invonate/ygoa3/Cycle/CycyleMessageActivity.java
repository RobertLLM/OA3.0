package cn.invonate.ygoa3.Cycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;

public class CycyleMessageActivity extends BaseActivity {

    YGApplication app;

    @BindView(R.id.layout_comment)
    TextView layoutComment;
    @BindView(R.id.layout_zan)
    TextView layoutZan;
    @BindView(R.id.layout_replay)
    TextView layoutReplay;
    @BindView(R.id.line_comment)
    View lineComment;
    @BindView(R.id.line_zan)
    View lineZan;
    @BindView(R.id.line_replay)
    View lineReplay;
    @BindView(R.id.pager)
    ViewPager pager;

    Fragment[] fragments = new Fragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycyle_message);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        for (int i = 0; i < 3; i++) {
            Fragment f = new MyCycleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            f.setArguments(bundle);
            fragments[i] = f;
        }

        pager.setAdapter(new PagersAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        lineComment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lineZan.setBackgroundColor(getResources().getColor(R.color.white));
                        lineReplay.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        lineComment.setBackgroundColor(getResources().getColor(R.color.white));
                        lineZan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lineReplay.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        lineComment.setBackgroundColor(getResources().getColor(R.color.white));
                        lineZan.setBackgroundColor(getResources().getColor(R.color.white));
                        lineReplay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.pic_back, R.id.layout_comment, R.id.layout_zan, R.id.layout_replay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.layout_comment:
                pager.setCurrentItem(0);
                lineComment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                lineZan.setBackgroundColor(getResources().getColor(R.color.white));
                lineReplay.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.layout_zan:
                pager.setCurrentItem(1);
                lineComment.setBackgroundColor(getResources().getColor(R.color.white));
                lineZan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                lineReplay.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.layout_replay:
                pager.setCurrentItem(2);
                lineComment.setBackgroundColor(getResources().getColor(R.color.white));
                lineZan.setBackgroundColor(getResources().getColor(R.color.white));
                lineReplay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }


    class PagersAdapter extends LazyFragmentPagerAdapter {

        public PagersAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment getItem(ViewGroup container, int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

}
