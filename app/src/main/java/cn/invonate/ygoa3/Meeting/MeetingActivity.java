package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Meeting.Fragment.MeetingFragment;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CustomViewPager;
import cn.invonate.ygoa3.View.FloatingDraftButton;

public class MeetingActivity extends BaseActivity {

    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    CustomViewPager vpContent;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;

    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);

        FloatingDraftButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iew) {
                Intent intent=new Intent(MeetingActivity.this,AddMeetingActivity.class);
                startActivity(intent);
            }
        });

        // 初始化标题数组
        tabIndicators = new ArrayList<>();
        tabIndicators.add("未结束");
        tabIndicators.add("已结束/取消/不参加");
        tabIndicators.add("我发出的");

        tabFragments = new ArrayList<>();
        for (int i = 0; i < tabIndicators.size(); i++) {
            tabFragments.add(MeetingFragment.newInstance(i));
        }
        vpContent.setOffscreenPageLimit(3);
        vpContent.setScanScroll(false);
        tlTab.setTabMode(TabLayout.MODE_FIXED);
        tlTab.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.colorPrimary));
        tlTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setElevation(tlTab, 10);
        tlTab.setupWithViewPager(vpContent);

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(contentAdapter);
    }

    @OnClick({R.id.pic_back, R.id.pic_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.pic_search:

                break;
        }
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
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }
}
