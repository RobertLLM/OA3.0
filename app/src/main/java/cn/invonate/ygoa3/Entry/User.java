package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/1/4.
 */

public class User {


    /**
     * user_code : 021274
     * user_id : C4BD95E0-A0EE-4F21-8359-A5CC468E899A
     * rsbm_pk : 1152A4B6-5839-40DE-B2EB-801AE2299B20
     * success : 0
     * user_name : 沈浩
     * sessionId : 8519420FDEE301A17E772FE0FCC24B23
     * xmppPassword : 627D34CC0C0A5689E0500A0A39054F56
     * user_photo : personal/021274.jpg
     * mailPassword : 6202A4C6495AB039E0500A0A390563D7
     */

    private String user_code;
    private String user_id;
    private String rsbm_pk;
    private int success;
    private String user_name;
    private String sessionId;
    private String xmppPassword;
    private String user_photo;
    private String mailPassword;
    private String msg;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRsbm_pk() {
        return rsbm_pk;
    }

    public void setRsbm_pk(String rsbm_pk) {
        this.rsbm_pk = rsbm_pk;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getXmppPassword() {
        return xmppPassword;
    }

    public void setXmppPassword(String xmppPassword) {
        this.xmppPassword = xmppPassword;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
