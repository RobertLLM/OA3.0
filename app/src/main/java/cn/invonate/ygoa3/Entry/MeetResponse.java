package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

public class MeetResponse {

    /**
     * code : 0000
     * message : 取消成功
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
