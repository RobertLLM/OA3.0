package cn.invonate.ygoa3.Util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun timeFormatWithDoday(time: Long): String {
        val pattern = StringBuffer()
        val today = Date()
        val receivedDate = Date(time)
        if (today.year != receivedDate.year) {
            pattern.append("yyyy-")
        }
        if (today.year == receivedDate.year && today.month == receivedDate.month && today.date - 1 == receivedDate.date) {
            pattern.append("昨天")
        }

        if (today.year == receivedDate.year && today.date - 1 != receivedDate.date && today.date != receivedDate.date) {
            pattern.append("MM-dd ")
        }
        pattern.append("HH:mm")
        return SimpleDateFormat(pattern.toString()).format(receivedDate)

    }

    fun timeFormatNoYear(time: Long): String {
        val pattern = StringBuffer()
        pattern.append("MM-dd EE HH:mm")
        return SimpleDateFormat(pattern.toString()).format(Date(time))
    }

    fun timeFormatJustMMHH(time: Long): String {
        val pattern = StringBuffer()
        pattern.append("HH:mm")
        return SimpleDateFormat(pattern.toString()).format(Date(time))
    }
}
