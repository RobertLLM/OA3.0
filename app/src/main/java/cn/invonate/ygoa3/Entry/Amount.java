package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/2/28.
 */

public class Amount {

    /**
     * data : 239.68
     * success : 0
     */

    private String data;
    private int success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
