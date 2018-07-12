package cn.invonate.ygoa3.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

import java.io.ByteArrayOutputStream

/**
 * Created by liyangyang on 2017/6/6.
 */

object ImageUtils {
    // 根据路径获得图片并压缩，返回bitmap用于显示
    private fun getSmallBitmap(filePath: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(filePath, options)
    }

    //计算图片的缩放值
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    //把bitmap转换成String
    fun bitmapToString(filePath: String): String {
        val bm = getSmallBitmap(filePath)
        val baos = ByteArrayOutputStream()

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos)
        val b = baos.toByteArray()
        Log.d("d", "压缩后的大小=" + b.size)
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
