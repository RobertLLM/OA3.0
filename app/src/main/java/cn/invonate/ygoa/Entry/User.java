package cn.invonate.ygoa.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/1/4.
 */

public class User {

    /**
     * sessionId : 1A7365D4097B6A81217D5DA6CC2FA984
     * success : 0
     * user_code : 033523
     * user_name : 李阳洋
     * user_photo : personal/033523.jpg
     */

    private String sessionId;
    private int success;
    private String user_code;
    private String user_name;
    private String user_photo;
    private String msg;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
