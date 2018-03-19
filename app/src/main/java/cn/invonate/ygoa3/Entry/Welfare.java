package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class Welfare {

    /**
     * data : [{"cdts":1,"end_date":"2018-02-28T00:00:00","welfare_name":"蔬菜"}]
     * success : 0
     */

    private int success;
    private List<WelfareBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<WelfareBean> getData() {
        return data;
    }

    public void setData(List<WelfareBean> data) {
        this.data = data;
    }

    public static class WelfareBean {
        /**
         * cdts : 1
         * end_date : 2018-02-28T00:00:00
         * welfare_name : 蔬菜
         */

        private int cdts;
        private String end_date;
        private String welfare_name;

        public int getCdts() {
            return cdts;
        }

        public void setCdts(int cdts) {
            this.cdts = cdts;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getWelfare_name() {
            return welfare_name;
        }

        public void setWelfare_name(String welfare_name) {
            this.welfare_name = welfare_name;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
