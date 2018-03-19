package cn.invonate.ygoa3;

import android.content.Intent;
import android.os.Bundle;

import com.zhy.autolayout.AutoLayoutActivity;


/**
 * Created by liyangyang on 2017/10/20.
 */

public class BaseActivity extends AutoLayoutActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void goActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        this.finish();
    }

    protected void goActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    protected void stepActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    protected void stepActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
