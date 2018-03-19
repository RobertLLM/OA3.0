package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/2/27.
 */

public class Fund {

    /**
     * data : [{"account_amt":8.5,"appid":"手机点餐","description":"用餐(2.26午餐)","operate_time":"2018-02-26 11:18:02","opt_type":"5"},{"account_amt":8.5,"appid":"手机点餐","description":"订餐(2.26午餐)","operate_time":"2018-02-26 08:08:59","opt_type":"3"},{"account_amt":11.5,"appid":"手机点餐","description":"用餐(2.24午餐)","operate_time":"2018-02-24 11:18:51","opt_type":"5"},{"account_amt":11.5,"appid":"手机点餐","description":"订餐(2.24午餐)","operate_time":"2018-02-24 08:05:29","opt_type":"3"},{"account_amt":4.5,"appid":"手机点餐","description":"取消订餐(2.24午餐)","operate_time":"2018-02-24 08:04:45","opt_type":"4"},{"account_amt":6.5,"appid":"手机点餐","description":"用餐(2.23午餐)","operate_time":"2018-02-23 11:32:15","opt_type":"5"},{"account_amt":6.5,"appid":"手机点餐","description":"用餐(2.22午餐)","operate_time":"2018-02-22 11:16:53","opt_type":"5"},{"account_amt":4.5,"appid":"手机点餐","description":"订餐(2.24午餐)","operate_time":"2018-02-18 12:30:44","opt_type":"3"},{"account_amt":6.5,"appid":"手机点餐","description":"订餐(2.23午餐)","operate_time":"2018-02-18 12:30:30","opt_type":"3"},{"account_amt":6.5,"appid":"手机点餐","description":"订餐(2.22午餐)","operate_time":"2018-02-18 12:30:19","opt_type":"3"}]
     * success : 0
     */

    private int success;
    private List<FundBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<FundBean> getData() {
        return data;
    }

    public void setData(List<FundBean> data) {
        this.data = data;
    }

    public static class FundBean {
        /**
         * account_amt : 8.5
         * appid : 手机点餐
         * description : 用餐(2.26午餐)
         * operate_time : 2018-02-26 11:18:02
         * opt_type : 5
         */

        private double account_amt;
        private String appid;
        private String description;
        private String operate_time;
        private String opt_type;

        public double getAccount_amt() {
            return account_amt;
        }

        public void setAccount_amt(double account_amt) {
            this.account_amt = account_amt;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOperate_time() {
            return operate_time;
        }

        public void setOperate_time(String operate_time) {
            this.operate_time = operate_time;
        }

        public String getOpt_type() {
            return opt_type;
        }

        public void setOpt_type(String opt_type) {
            this.opt_type = opt_type;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
