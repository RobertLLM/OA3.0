package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2018/3/29.
 */

public class Sum {

    /**
     * data : 0
     * success : 0
     */

    private int data;
    private int success;

    public int getData() {
        return data;
    }

    public void setData(int data) {
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
