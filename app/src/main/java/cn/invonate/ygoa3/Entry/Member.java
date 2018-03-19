package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

/**
 * Created by liyangyang on 2018/1/9.
 */

public class Member {
    private int total;
    private ArrayList<Contacts> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Contacts> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Contacts> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
