package cn.invonate.ygoa3.Util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Process


class DownLoadRunnable(private val context: Context, private val url: String, private val title: String, private val state: Int, private val handler: Handler) : Runnable {

    override fun run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        startDownload()
    }

    /**
     * 通过DownloadManager开始下载
     *
     * @return
     */
    private fun startDownload() {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val requestId = downloadManager.enqueue(createRequest(url, title)) ?: 0
        queryDownloadProgress(requestId, downloadManager)
    }

    /**
     * 设置Request参数
     *
     * @param url
     * @return
     */
    private fun createRequest(url: String, title: String): DownloadManager.Request {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(title)// 文件下载时显示的标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// 下载完成后，notification会自动消失
        val file = MyUtils.getCacheFile(MyUtils.APP_NAME, context)
        if (file != null && file.exists()) {
            file.delete()//删除掉存在的apk
        }
        request.setDestinationUri(Uri.fromFile(file))//指定apk缓存路径
        return request
    }

    /**
     * 查询下载进度
     *
     * @param requestId DownloadManager.COLUMN_TOTAL_SIZE_BYTES  下载文件的大小（总字节数）
     * DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR 当前下载文件的字节数
     */
    private fun queryDownloadProgress(requestId: Long, downloadManager: DownloadManager?) {
        val query = DownloadManager.Query()
        query.setFilterById(requestId)
        try {
            var isGoging = true
            while (isGoging) {
                val cursor = downloadManager!!.query(query)
                if (cursor != null && cursor.moveToFirst()) {
                    val state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (state) {
                        DownloadManager.STATUS_SUCCESSFUL//下载成功
                        -> {
                            isGoging = false
                            handler.obtainMessage(DownloadManager.STATUS_SUCCESSFUL).sendToTarget()//发送到主线程，更新ui
                        }
                        DownloadManager.STATUS_FAILED//下载失败
                        -> {
                            isGoging = false
                            handler.obtainMessage(DownloadManager.STATUS_FAILED).sendToTarget()//发送到主线程，更新ui
                        }

                        DownloadManager.STATUS_RUNNING//下载中
                        -> {
                            /*
                             * 计算下载下载率；
                             */
                            val totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            val currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            val progress = (currentSize.toFloat() / totalSize.toFloat() * 100).toInt()
                            handler.obtainMessage(DownloadManager.STATUS_RUNNING, progress).sendToTarget()//发送到主线程，更新ui
                        }

                        DownloadManager.STATUS_PAUSED//下载停止
                        -> handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget()

                        DownloadManager.STATUS_PENDING//准备下载
                        -> handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget()
                    }
                }
                cursor?.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
