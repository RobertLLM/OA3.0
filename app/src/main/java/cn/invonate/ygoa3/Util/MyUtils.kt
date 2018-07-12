package cn.invonate.ygoa3.Util

import android.content.Context
import android.os.Environment
import android.util.DisplayMetrics
import android.view.WindowManager

import java.io.File


object MyUtils {

    val APP_NAME = "oa.apk"//名字

    val PACKAGE_NAME = "cn.invonate"

    /**
     * 获得存储文件
     *
     * @param
     * @param
     * @return
     */
    fun getCacheFile(name: String, context: Context): File {
        val cachePath: String
        if (Environment.MEDIA_MOUNTED == Environment
                        .getExternalStorageState() || !Environment.isExternalStorageRemovable()) {

            cachePath = context.externalCacheDir!!.path
        } else {
            cachePath = context.cacheDir.path
        }
        return File(Environment.getExternalStorageDirectory().toString() + "/" + MyUtils.PACKAGE_NAME + "/" + MyUtils.APP_NAME)
    }

    /**
     * 获取手机大小，px
     *
     * @param context
     * @return
     */
    fun getPhoneMetrics(context: Context): DisplayMetrics {// 获取手机分辨率
        val dm = DisplayMetrics()
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        manager.defaultDisplay.getMetrics(dm)
        return dm
    }
}
