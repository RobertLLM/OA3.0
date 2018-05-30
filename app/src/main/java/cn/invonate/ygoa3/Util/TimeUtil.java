package cn.invonate.ygoa3.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String timeFormatWithDoday(long time) {
        StringBuffer pattern = new StringBuffer();
        Date today = new Date();
        Date receivedDate = new Date(time);
        if (today.getYear() != receivedDate.getYear()) {
            pattern.append("yyyy-");
        }
        if (today.getYear() == receivedDate.getYear() && today.getMonth() == receivedDate.getMonth() && today.getDate() - 1 == receivedDate.getDate()) {
            pattern.append("昨天");
        }

        if (today.getYear() == receivedDate.getYear() && today.getDate() - 1 != receivedDate.getDate() && today.getDate() != receivedDate.getDate()) {
            pattern.append("MM-dd ");
        }
        pattern.append("HH:mm");
        return new SimpleDateFormat(pattern.toString()).format(receivedDate);

    }

    public static String timeFormatNoYear(long time) {
        StringBuffer pattern = new StringBuffer();
        pattern.append("MM:mm EE HH:mm");
        return new SimpleDateFormat(pattern.toString()).format(new Date(time));
    }

    public static String timeFormatJustMMHH(long time) {
        StringBuffer pattern = new StringBuffer();
        pattern.append("HH:mm");
        return new SimpleDateFormat(pattern.toString()).format(new Date(time));
    }
}
