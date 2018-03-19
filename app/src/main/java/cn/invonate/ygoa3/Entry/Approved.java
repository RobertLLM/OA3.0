package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/1/16.
 */

public class Approved {
    private List<ApprovedBean> data;
    private int success;

    public static class ApprovedBean{
        private String id;
        private String applyDate;
        private String applyDept;
        private String businessId;
        private int fyh;
        private String processInstanceId;
        private String title;
        private String url;
        private String workflowType;
        private String approveResult;
        private boolean isXt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getApplyDept() {
            return applyDept;
        }

        public void setApplyDept(String applyDept) {
            this.applyDept = applyDept;
        }

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public int getFyh() {
            return fyh;
        }

        public void setFyh(int fyh) {
            this.fyh = fyh;
        }

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWorkflowType() {
            return workflowType;
        }

        public void setWorkflowType(String workflowType) {
            this.workflowType = workflowType;
        }

        public String getApproveResult() {
            return approveResult;
        }

        public void setApproveResult(String approveResult) {
            this.approveResult = approveResult;
        }

        public boolean isXt() {
            return isXt;
        }

        public void setXt(boolean xt) {
            isXt = xt;
        }
    }

    public List<ApprovedBean> getData() {
        return data;
    }

    public void setData(List<ApprovedBean> data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
