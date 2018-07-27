package cn.invonate.ygoa3

import android.content.Intent
import android.os.Bundle
import com.zhy.autolayout.AutoLayoutActivity


/**
 * Created by liyangyang on 2017/10/20.
 */

open class BaseActivity : AutoLayoutActivity() {

    protected fun goActivity(clz: Class<*>) {
        val intent = Intent(this, clz)
        startActivity(intent)
        this.finish()
    }

    protected fun goActivity(bundle: Bundle, clz: Class<*>) {
        val intent = Intent(this, clz)
        intent.putExtras(bundle)
        startActivity(intent)
        this.finish()
    }

    protected fun stepActivity(clz: Class<*>) {
        val intent = Intent(this, clz)
        startActivity(intent)
    }

    protected fun stepActivity(bundle: Bundle, clz: Class<*>) {
        val intent = Intent(this, clz)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
