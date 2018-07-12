package cn.invonate.ygoa3.Util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Created by liyangyang on 2017/5/22.
 */

object SpUtil {
    private val FIRST = "first"
    private val USER_SHARED = "user_shared"
    private val APP_NAME = "oa"

    /**
     * 读取用户名
     *
     * @param context
     * @return
     */
    fun getName(context: Context): String {
        val sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE)
        return sp.getString("userName", "")
    }

    /**
     * 读取用户名
     *
     * @param context
     * @return
     */
    fun getPass(context: Context): String {
        val sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE)
        return sp.getString("password", "")
    }

    /**
     * 保存用户名
     *
     * @param context
     * @param name
     */
    fun setName(context: Context, name: String) {
        val sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("userName", name)
        editor.apply()
    }

    /**
     * 保存密码
     *
     * @param context
     * @param pass
     */
    fun setPass(context: Context, pass: String) {
        val sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("password", pass)
        editor.apply()
    }

    /**
     * 获取是否第一次登陆
     *
     * @param context
     * @return
     */
    fun getFirst(context: Context): Boolean {
        val sp = context.getSharedPreferences(FIRST, MODE_PRIVATE)
        return sp.getBoolean("first", true)
    }

    /**
     * 设置是否第一次
     *
     * @param context
     * @param is
     */
    fun setFirst(context: Context, `is`: Boolean) {
        val sp = context.getSharedPreferences(FIRST, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean("first", `is`)
        editor.apply()
    }


    fun readString(context: Context, key: String): String {
        return getSharedPreferences(context).getString(key, "")
    }

    fun writeString(context: Context, key: String, value: String) {
        getSharedPreferences(context).edit().putString(key, value).apply()
    }

    fun remove(context: Context, key: String) {
        getSharedPreferences(context).edit().remove(key).apply()
    }

    fun removeAll(context: Context) {
        getSharedPreferences(context).edit().clear().apply()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
    }

}
