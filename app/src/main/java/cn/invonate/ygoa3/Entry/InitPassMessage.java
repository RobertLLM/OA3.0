package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/1/9.
 */

public class InitPassMessage {

    /**
     * cause : 身份验证失败！
     * success : 1
     */

    private String cause;
    private int success;

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
