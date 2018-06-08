package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Meeting.Fragment.MeetingFragment;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CustomViewPager;
import cn.invonate.ygoa3.View.FloatingDraftButton;

public class MeetingActivity extends BaseActivity {

    //    @BindView(R.id.tl_tab)
//    TabLayout tlTab;
    @BindView(R.id.vp_content)
    CustomViewPager vpContent;
    @BindViews({R.id.layout_undone, R.id.layout_done, R.id.layout_mine})
    TextView[] textViews;

    @BindViews({R.id.view_undone, R.id.view_done, R.id.view_mine})
    View[] views;
    //    private List<String> tabIndicators;
    private List<Fragment> tabFragments;

    private ContentPagerAdapter contentAdapter;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);

        FloatingDraftButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iew) {
                Intent intent = new Intent(MeetingActivity.this, AddMeetingActivity.class);
                startActivity(intent);
            }
        });
        tabFragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tabFragments.add(MeetingFragment.newInstance(i));
        }
        vpContent.setOffscreenPageLimit(3);
        vpContent.setScanScroll(false);
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(contentAdapter);
        change(0);
    }

    @OnClick({R.id.pic_back, R.id.pic_search, R.id.layout_undone, R.id.layout_done, R.id.layout_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.pic_search:

                break;
            case R.id.layout_undone:
                change(0);
                break;
            case R.id.layout_done:
                change(1);
                break;
            case R.id.layout_mine:
                change(2);
                break;


        }
    }

    /**
     * 切换
     *
     * @param position
     */
    private void change(int position) {
        if (position != index) {
            vpContent.setCurrentItem(position);
            views[index].setBackgroundColor(Color.parseColor("#E1E1E1"));
            views[position].setBackgroundColor(Color.parseColor("#3cbaff"));
            textViews[index].setTextColor(Color.parseColor("#000000"));
            textViews[position].setTextColor(Color.parseColor("#3cbaff"));
        }
        index = position;
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}
