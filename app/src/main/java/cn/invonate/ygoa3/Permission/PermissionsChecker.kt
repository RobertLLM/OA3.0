package cn.invonate.ygoa3.Permission

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * 检查权限的工具类
 */
class PermissionsChecker(context: Context) {
    private val mContext: Context = context.applicationContext

    // 判断权限集合
    fun lacksPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    // 判断是否缺少权限
    private fun lacksPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED
    }
}
