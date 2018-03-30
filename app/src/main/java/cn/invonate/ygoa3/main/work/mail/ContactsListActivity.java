package cn.invonate.ygoa3.main.work.mail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;

public class ContactsListActivity extends BaseActivity {

    @BindView(R.id.contacts)
    TextView contacts;
    @BindView(R.id.copy)
    TextView copy;
    @BindView(R.id.view_contacts)
    View viewContacts;
    @BindView(R.id.view_copy)
    View viewCopy;
    @BindView(R.id.pager)
    ViewPager pager;

    private Fragment[] fragments = new Fragment[2];

    private ArrayList<String> List_receiver;
    private ArrayList<String> list_copy;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        List_receiver = getIntent().getExtras().getStringArrayList("receiver");
        list_copy = getIntent().getExtras().getStringArrayList("copy");

        ToListFragment f1 = new ToListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putStringArrayList("list", List_receiver);
        f1.setArguments(bundle1);
        fragments[0] = f1;

        ToListFragment f2 = new ToListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putStringArrayList("list", list_copy);
        f2.setArguments(bundle2);
        fragments[1] = f2;

        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    contacts.setTextColor(Color.parseColor("#3cbaff"));
                    copy.setTextColor(Color.parseColor("#000000"));
                    viewContacts.setVisibility(View.VISIBLE);
                    viewCopy.setVisibility(View.INVISIBLE);
                } else {
                    copy.setTextColor(Color.parseColor("#3cbaff"));
                    contacts.setTextColor(Color.parseColor("#000000"));
                    viewContacts.setVisibility(View.INVISIBLE);
                    viewCopy.setVisibility(View.VISIBLE);
                }
                ContactsListActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.img_back, R.id.contacts, R.id.copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.contacts:
                if (position != 0) {
                    contacts.setTextColor(Color.parseColor("#3cbaff"));
                    copy.setTextColor(Color.parseColor("#000000"));

                    viewContacts.setVisibility(View.VISIBLE);
                    viewCopy.setVisibility(View.INVISIBLE);

                    pager.setCurrentItem(0);
                    position = 0;
                }
                break;
            case R.id.copy:
                if (position != 1) {
                    copy.setTextColor(Color.parseColor("#3cbaff"));
                    contacts.setTextColor(Color.parseColor("#000000"));

                    viewContacts.setVisibility(View.INVISIBLE);
                    viewCopy.setVisibility(View.VISIBLE);
                    pager.setCurrentItem(1);
                    position = 1;
                }
                break;
        }
    }

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
