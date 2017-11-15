package cn.invonate.ygoa.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/10/30.
 */

public class FileEntry {

    private String name;
    private String type;
    private byte[] is;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getIs() {
        return is;
    }

    public void setIs(byte[] is) {
        this.is = is;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
