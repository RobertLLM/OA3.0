package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/2/26.
 */

public class ChangePass {
    private int success;
    private String cause;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
