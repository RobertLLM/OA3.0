package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

public class Like {

    /**
     * result : 0
     * thumb_up : A2D4C102-806A-4516-B67B-F979F8E5F8D5
     */

    private int result;
    private String thumb_up;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getThumb_up() {
        return thumb_up;
    }

    public void setThumb_up(String thumb_up) {
        this.thumb_up = thumb_up;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

