package cn.invonate.ygoa3.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.MailHolder;
import cn.invonate.ygoa3.View.HackyViewPager;

public class BytePicActivity extends AppCompatActivity {


    @BindView(R.id.page_pic)
    HackyViewPager pagePic;

    private int index;

    private int position; //标记第几份邮件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        position = getIntent().getExtras().getInt("position");
        index = getIntent().getExtras().getInt("index");
        pagePic.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pagePic.setCurrentItem(position);
    }

    /**
     * 相册适配器
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ByteImageDetailFragment.newInstance(position, index);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "pic";
        }

        @Override
        public int getCount() {
            return MailHolder.mail_model.get(index).getAttachmentsInputStreams().size();
        }

    }
}
