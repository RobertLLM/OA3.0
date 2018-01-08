package cn.invonate.ygoa.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by liyangyang on 2018/1/4.
 */

public class Department implements Serializable{
    private String department_name;
    private String id_;

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
