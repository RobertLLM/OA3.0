package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/2/6.
 */

public class TaskCopy {
    private int success;
    private List<Approved.ApprovedBean> data;
    private List<Approved.ApprovedBean> dataUn;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<Approved.ApprovedBean> getData() {
        return data;
    }

    public void setData(List<Approved.ApprovedBean> data) {
        this.data = data;
    }

    public List<Approved.ApprovedBean> getDataUn() {
        return dataUn;
    }

    public void setDataUn(List<Approved.ApprovedBean> dataUn) {
        this.dataUn = dataUn;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
