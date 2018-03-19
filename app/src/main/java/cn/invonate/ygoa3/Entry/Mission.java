package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class Mission {
    private int success;
    private ArrayList<MissionBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<MissionBean> getData() {
        return data;
    }

    public void setData(ArrayList<MissionBean> data) {
        this.data = data;
    }

    public static class MissionBean implements Serializable{
        private String applyDept;
        private String businessId;
        private String createTime;
        private String description;
        private String executionId;
        private int flag;
        private int fyh;
        private String id;
        private String lb;
        private String reject_message;
        private String title;
        private String url;
        private String workflowType;
        private boolean isXt;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getExecutionId() {
            return executionId;
        }

        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getFyh() {
            return fyh;
        }

        public void setFyh(int fyh) {
            this.fyh = fyh;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLb() {
            return lb;
        }

        public void setLb(String lb) {
            this.lb = lb;
        }

        public String getReject_message() {
            return reject_message;
        }

        public void setReject_message(String reject_message) {
            this.reject_message = reject_message;
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

        public boolean isXt() {
            return isXt;
        }

        public void setXt(boolean xt) {
            isXt = xt;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
