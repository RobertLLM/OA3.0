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
import cn.invonate.ygoa3.Task.fragment.TaskDetailFragment;
import cn.invonate.ygoa3.Task.fragment.TaskLineFragment;

public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.task_tab)
    SegmentControlView taskTab;
    @BindView(R.id.pager_task)
    ViewPager pagerTask;
    //private Mission.MissionBean task;
    private String businessId;
    private String taskId;
    private String workflowType;

    Fragment[] fragments = new Fragment[2];

    private boolean isXt;
    private boolean need_layout;//是否需要审批意见、抄送等

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detaail);
        ButterKnife.bind(this);
        businessId = getIntent().getExtras().getString("businessId");
        taskId = getIntent().getExtras().getString("taskId");
        workflowType = getIntent().getExtras().getString("workflowType");
        need_layout = getIntent().getExtras().getBoolean("need_layout");
        isXt = getIntent().getExtras().getBoolean("isXt");
        initFragment();
        taskTab.setTexts(new String[]{"待办详情", "审批轨迹"});
        pagerTask.setAdapter(new TaskPagerAdapter(getSupportFragmentManager()));
        taskTab.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                pagerTask.setCurrentItem(newSelectedIndex);
            }
        });
        taskTab.setViewPager(pagerTask);
    }

    private void initFragment() {
        TaskDetailFragment f1 = new TaskDetailFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("businessId", businessId);
        bundle1.putString("taskId", taskId);
        bundle1.putString("workflowType", workflowType);
        bundle1.putBoolean("need_layout", need_layout);
        bundle1.putBoolean("isXt", isXt);
        f1.setArguments(bundle1);
        fragments[0] = f1;
        TaskLineFragment f2 = new TaskLineFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("businessId", businessId);
        f2.setArguments(bundle2);
        fragments[1] = f2;
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
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
