package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskLine {

    /**
     * data : [{"approvedatestr":"","approveresult":"处理中","approveusername":"沈浩","cmnt":""},{"approvedate":"2017-12-12T13:02:57","approvedatestr":"2017-12-12 13:02:57","approveresult":"完成","approveusername":"周琦","cmnt":"填写联系单"}]
     * success : 0
     */

    private String success;
    private List<LineBean> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<LineBean> getData() {
        return data;
    }

    public void setData(List<LineBean> data) {
        this.data = data;
    }

    public static class LineBean {
        /**
         * approvedatestr :
         * approveresult : 处理中
         * approveusername : 沈浩
         * cmnt :
         * approvedate : 2017-12-12T13:02:57
         */

        private String approvedatestr;
        private String approveresult;
        private String approveusername;
        private String cmnt;
        private String approvedate;

        public String getApprovedatestr() {
            return approvedatestr;
        }

        public void setApprovedatestr(String approvedatestr) {
            this.approvedatestr = approvedatestr;
        }

        public String getApproveresult() {
            return approveresult;
        }

        public void setApproveresult(String approveresult) {
            this.approveresult = approveresult;
        }

        public String getApproveusername() {
            return approveusername;
        }

        public void setApproveusername(String approveusername) {
            this.approveusername = approveusername;
        }

        public String getCmnt() {
            return cmnt;
        }

        public void setCmnt(String cmnt) {
            this.cmnt = cmnt;
        }

        public String getApprovedate() {
            return approvedate;
        }

        public void setApprovedate(String approvedate) {
            this.approvedate = approvedate;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
