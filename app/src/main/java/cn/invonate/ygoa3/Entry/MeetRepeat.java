package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class MeetRepeat {

    /**
     * code : 0000
     * message : OK
     * result : [{"reason":"123456","userCode":"034488","userId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","userName":"谢凌云"}]
     */

    private String code;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * reason : 123456
         * userCode : 034488
         * userId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
         * userName : 谢凌云
         */

        private String reason;
        private String userCode;
        private String userId;
        private String userName;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
