package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/3/2.
 */

public class LocalContacts {
    private String user_name;
    private String user_phone;
    private String user_email;
    private String sortLetters;  //显示数据拼音的首字母
    private boolean is_select;

    public LocalContacts(String user_name, String user_phone, String user_email) {
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
