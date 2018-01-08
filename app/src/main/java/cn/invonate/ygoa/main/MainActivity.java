package cn.invonate.ygoa.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.R;

public class MainActivity extends BaseActivity {
    @BindViews({R.id.pic_message, R.id.pic_work, R.id.pic_connect, R.id.pic_mine,})
    List<ImageView> list_imgs;

    @BindViews({R.id.text_message, R.id.text_work, R.id.text_connect, R.id.text_mine})
    List<TextView> list_txt;

    //点击的字体颜色
    @BindColor(R.color.colorPrimary)
    public int colorMainClick;

    //未点击的字体颜色
    @BindColor(R.color.fragemnt_text)
    public int colorMainUnClick;

    private Fragment[] fragments;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments = new Fragment[]{new MessageFragment(), new WorkFragment(), new ContactsFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragments[0])
                .add(R.id.content, fragments[1])
                .add(R.id.content, fragments[2])
                .add(R.id.content, fragments[3])
                .hide(fragments[1]).hide(fragments[2])
                .hide(fragments[3])
                .show(fragments[0])
                .commit();
        list_imgs.get(0).setSelected(true);
        list_txt.get(0).setTextColor(colorMainClick);
    }

    /**
     * 切换Fragment的方法
     *
     * @param index 页码
     */
    private void changeFragment(int index) {
        if (currentItem != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentItem]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        list_imgs.get(currentItem).setSelected(false);
        list_imgs.get(index).setSelected(true);

        list_txt.get(currentItem).setTextColor(colorMainUnClick);
        list_txt.get(index).setTextColor(colorMainClick);
        currentItem = index;
    }

    @OnClick({R.id.layout_message, R.id.layout_work, R.id.layout_connect, R.id.layout_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_message:
                changeFragment(0);
                break;
            case R.id.layout_work:
                changeFragment(1);
                break;
            case R.id.layout_connect:
                changeFragment(2);
                break;
            case R.id.layout_mine:
                changeFragment(3);
                break;
        }
    }
}
