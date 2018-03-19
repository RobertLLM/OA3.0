package cn.invonate.ygoa3.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Task.fragment.TaskCopyFragment;
import cn.invonate.ygoa3.Task.fragment.TaskUnCopyFragment;
import cn.invonate.ygoa3.YGApplication;

public class TaskCopyActivity extends BaseActivity {

    @BindView(R.id.task_tab)
    SegmentControlView taskTab;
    @BindView(R.id.pager_task)
    ViewPager pagerTask;

    YGApplication app;

    Fragment[] fragments = new Fragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_copy);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        taskTab.setTexts(new String[]{"已读代办", "未读代办"});
        initFragment();
        pagerTask.setAdapter(new TaskPagerAdapter(getSupportFragmentManager()));
        taskTab.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                pagerTask.setCurrentItem(newSelectedIndex);
            }
        });
        taskTab.setViewPager(pagerTask);
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    private void initFragment() {
        TaskCopyFragment f1 = new TaskCopyFragment();
        fragments[0] = f1;
        TaskUnCopyFragment f2 = new TaskUnCopyFragment();
        fragments[1] = f2;
    }

    class TaskPagerAdapter extends LazyFragmentPagerAdapter {

        public TaskPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        protected Fragment getItem(ViewGroup container, int position) {
            return fragments[position];
        }
    }
}
