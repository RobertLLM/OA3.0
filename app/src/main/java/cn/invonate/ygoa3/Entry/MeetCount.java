package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

public class MeetCount {

    /**
     * code : 0000
     * message : OK
     * result : {"count":"0"}
     */

    private String code;
    private String message;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * count : 0
         */

        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
