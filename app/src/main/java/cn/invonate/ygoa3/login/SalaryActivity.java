package cn.invonate.ygoa3.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Salary;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.login.Fragment.SalaryFragment;
import cn.invonate.ygoa3.login.Fragment.SocialFragment;
import rx.Subscriber;

public class SalaryActivity extends BaseActivity {

    @BindView(R.id.tab_salary)
    SegmentControlView tabSalary;
    @BindView(R.id.pager_salary)
    ViewPager pagerSalary;

    Fragment[] fragments = new Fragment[2];

    private YGApplication app;

    private int newValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        initFragment();
        getSalary();
    }

    /**
     * 获取工资信息
     */
    private void getSalary() {
        Subscriber subscriber = new Subscriber<Salary>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Salary data) {
                Log.i("getSalary", data.toString());
                if (data.getSuccess() == 0) {
                    ((SalaryFragment) fragments[0]).setData(data.getData());
                    ((SocialFragment) fragments[1]).setData(data.getGjjData());
                } else {
                    Toast.makeText(SalaryActivity.this, "无查询数据", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(this, false).getSalary(subscriber, app.getUser().getSessionId(), newValue);
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        Fragment f1 = new SalaryFragment();
        Fragment f2 = new SocialFragment();
        fragments[0] = f1;
        fragments[1] = f2;
        tabSalary.setTexts(new String[]{"工资详情", "五险一金"});
        pagerSalary.setAdapter(new TaskPagerAdapter(getSupportFragmentManager()));

        tabSalary.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                pagerSalary.setCurrentItem(newSelectedIndex);
            }
        });
        tabSalary.setViewPager(pagerSalary);
    }

    @OnClick({R.id.pic_back, R.id.btn_pre, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.btn_pre:
                newValue--;
                getSalary();
                break;
            case R.id.btn_next:
                newValue++;
                getSalary();
                break;
        }
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
