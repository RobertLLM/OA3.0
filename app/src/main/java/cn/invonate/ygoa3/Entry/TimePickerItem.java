package cn.invonate.ygoa3.Entry;

import java.util.List;

public class TimePickerItem {
    private String hour;
    private List<String> minute;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public List<String> getMinute() {
        return minute;
    }

    public void setMinute(List<String> minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return this.hour;
    }
}
