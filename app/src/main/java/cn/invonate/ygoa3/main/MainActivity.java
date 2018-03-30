package cn.invonate.ygoa3.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;

public class MainActivity extends BaseActivity {
    @BindViews({R.id.pic_message, R.id.pic_work, R.id.pic_pic, R.id.pic_connect, R.id.pic_mine,})
    List<ImageView> list_imgs;

    @BindViews({R.id.text_message, R.id.text_work, R.id.text_pic, R.id.text_connect, R.id.text_mine})
    List<TextView> list_txt;

    //点击的字体颜色
    @BindColor(R.color.colorPrimary)
    public int colorMainClick;

    //未点击的字体颜色
    @BindColor(R.color.fragemnt_text)
    public int colorMainUnClick;

    private Fragment[] fragments;

    private int currentItem = 1;

    private FinishBroadCast finish;

    private RefreshBroadCast refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments = new Fragment[]{new MessageFragment(), new WorkFragment(), new PicFragment(), new ContactsFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragments[0])
                .add(R.id.content, fragments[1])
                .add(R.id.content, fragments[2])
                .add(R.id.content, fragments[3])
                .add(R.id.content, fragments[4])
                .hide(fragments[0]).hide(fragments[2])
                .hide(fragments[3])
                .hide(fragments[4])
                .show(fragments[1])
                .commit();
        list_imgs.get(1).setSelected(true);
        list_txt.get(1).setTextColor(colorMainClick);

        finish = new FinishBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        registerReceiver(finish, intentFilter);

        refresh = new RefreshBroadCast();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("refresh");
        registerReceiver(refresh, intentFilter2);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finish);
        unregisterReceiver(refresh);
        super.onDestroy();
    }

    /**
     * 切换Fragment的方法
     *
     * @param index 页码
     */
    private void changeFragment(int index) {
        if (index == 0) {
            Toast.makeText(this, "该功能暂未开放", Toast.LENGTH_SHORT).show();
            return;
        }
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

    @OnClick({R.id.layout_message, R.id.layout_work, R.id.layout_pic, R.id.layout_connect, R.id.layout_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_message:
                changeFragment(0);
                break;
            case R.id.layout_work:
                changeFragment(1);
                break;
            case R.id.layout_pic:
                changeFragment(2);
                break;
            case R.id.layout_connect:
                changeFragment(3);
                break;
            case R.id.layout_mine:
                changeFragment(4);
                break;
        }
    }

    class FinishBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    class RefreshBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (fragments != null && fragments[3] != null)
                ((ContactsFragment) fragments[3]).refreshLocal();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
