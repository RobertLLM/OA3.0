package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class Mission {

    /**
     * data : [{"applyDept":"恒创软件软件开发科","approve_date_":"2018-03-20 09:09:24.0","businessId":"50157F0A-46ED-4C25-B15F-E8CA856A45C5","createTime":"03-20 08:38","create_":"2018-03-20T08:38:00","description":"审核","executionId":"xxlldlc.56790016","flag":"1","fyh":0,"id":"56790024","important_leval":"","isXt":"0","is_fj":"0","lb":"信息联络单","lxdbh":"LLD180201002","opinion_":"填写申请","title":"石丽君","url":"/ygoa/view/xxll/loadXxlldFcbm.action","user_name":"高扬","workflowType":"112","zhaiyao":"0"},{"applyDept":"恒创软件软件开发科","approve_date_":"2018-03-20 09:02:28.0","businessId":"D3C8C3EC-3533-4B89-B1D5-7A4444820104","createTime":"03-20 08:38","create_":"2018-03-20T08:38:00","description":"审核","executionId":"stProjectRelationBill.56260001","flag":"1","fyh":0,"id":"56790014","isXt":"0","lb":"工程联系单","opinion_":"填写申请","title":"测试","url":"/ygoa/view/projectRelationBill/loadStDeptAdmin.action","user_name":"谢凌云","workflowType":"110","zhaiyao":""},{"applyDept":"恒创软件软件开发科","approve_date_":"2018-02-22 14:35:58.0","businessId":"627DE8EF-286A-420D-ACF4-178928F7F22B","createTime":"01-25 15:11","create_":"2018-01-25T15:11:33","description":"评审","executionId":"wjps.6200020","flag":"1","fyh":0,"id":"6430019","isXt":"0","lb":"文件评审","opinion_":"C","title":"文件评审180124001","url":"/ygoa/view/wjps/loadWjpsPs.action","user_name":"沈浩","workflowType":"108","zhaiyao":""},{"applyDept":"恒创软件软件开发科","approve_date_":"2018-03-20 08:46:23.0","businessId":"0486D8E1-470C-4559-941D-CF12038D00AB","createTime":"03-20 08:38","create_":"2018-03-20T08:38:00","description":"审核","executionId":"wjps.56790003","flag":"0","fyh":0,"id":"56790010","important_leval":"","isXt":"0","is_fj":"1","lb":"文件评审","lxdbh":"180314001","opinion_":"填写申请","title":"评审:永钢IT运维管理平台项目方案2.28","url":"/ygoa/view/wjps/loadWjpsLeader.action","user_name":"高扬","workflowType":"108","zhaiyao":"闫秋粉拟稿"},{"applyDept":"恒创软件软件开发科","approve_date_":"2018-03-20 08:48:47.0","businessId":"016FE186-5560-445A-B78B-026896BF7982","createTime":"03-20 08:38","create_":"2018-03-20T08:38:00","description":"审核","executionId":"wjps.55340003","flag":"0","fyh":0,"id":"56790012","isXt":"0","lb":"文件评审","opinion_":"填写申请","title":"评审:11","url":"/ygoa/view/wjps/loadWjpsLeader.action","user_name":"石丽君","workflowType":"108","zhaiyao":""},{"applyDept":"恒创软件软件开发科","approve_date_":"2018-03-20 09:46:17.0","businessId":"4fe927a0-2be0-11e8-9e21-1866daf6a144","createTime":"03-19 13:59","create_":"2018-03-19T13:59:52","description":"审核","executionId":"carWantedBill.56730059","flag":"1","fyh":0,"id":"56730066","important_leval":"","isXt":"0","is_fj":"0","lb":"用车申请","lxdbh":"20180320000","opinion_":"填写申请","title":"事由:777","url":"/ygoa/view/car/loadprocessDeptAdmin.action","user_name":"李阳洋","workflowType":"7","zhaiyao":"777,000"}]
     * success : 0
     */

    private int success;
    private List<MissionBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<MissionBean> getData() {
        return data;
    }

    public void setData(List<MissionBean> data) {
        this.data = data;
    }


    public static class MissionBean {
        /**
         * applyDept : 恒创软件软件开发科
         * approve_date_ : 2018-03-20 09:09:24.0
         * businessId : 50157F0A-46ED-4C25-B15F-E8CA856A45C5
         * createTime : 03-20 08:38
         * create_ : 2018-03-20T08:38:00
         * description : 审核
         * executionId : xxlldlc.56790016
         * flag : 1
         * fyh : 0
         * id : 56790024
         * important_leval :
         * isXt : 0
         * is_fj : 0
         * lb : 信息联络单
         * lxdbh : LLD180201002
         * opinion_ : 填写申请
         * title : 石丽君
         * url : /ygoa/view/xxll/loadXxlldFcbm.action
         * user_name : 高扬
         * workflowType : 112
         * zhaiyao : 0
         */

        private String applyDept;
        private String approve_date_;
        private String businessId;
        private String createTime;
        private String create_;
        private String description;
        private String executionId;
        private String flag;
        private int fyh;
        private String id;
        private String important_leval;
        private String isXt;
        private String is_fj;
        private String lb;
        private String lxdbh;
        private String opinion_;
        private String title;
        private String url;
        private String user_name;
        private String workflowType;
        private String zhaiyao;
        private String reject_message;

        public String getApplyDept() {
            return applyDept;
        }

        public void setApplyDept(String applyDept) {
            this.applyDept = applyDept;
        }

        public String getApprove_date_() {
            return approve_date_;
        }

        public void setApprove_date_(String approve_date_) {
            this.approve_date_ = approve_date_;
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

        public String getCreate_() {
            return create_;
        }

        public void setCreate_(String create_) {
            this.create_ = create_;
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

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
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

        public String getImportant_leval() {
            return important_leval;
        }

        public void setImportant_leval(String important_leval) {
            this.important_leval = important_leval;
        }

        public String getIsXt() {
            return isXt;
        }

        public void setIsXt(String isXt) {
            this.isXt = isXt;
        }

        public String getIs_fj() {
            return is_fj;
        }

        public void setIs_fj(String is_fj) {
            this.is_fj = is_fj;
        }

        public String getLb() {
            return lb;
        }

        public void setLb(String lb) {
            this.lb = lb;
        }

        public String getLxdbh() {
            return lxdbh;
        }

        public void setLxdbh(String lxdbh) {
            this.lxdbh = lxdbh;
        }

        public String getOpinion_() {
            return opinion_;
        }

        public void setOpinion_(String opinion_) {
            this.opinion_ = opinion_;
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

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getWorkflowType() {
            return workflowType;
        }

        public void setWorkflowType(String workflowType) {
            this.workflowType = workflowType;
        }

        public String getZhaiyao() {
            return zhaiyao;
        }

        public void setZhaiyao(String zhaiyao) {
            this.zhaiyao = zhaiyao;
        }

        public String getReject_message() {
            return reject_message;
        }

        public void setReject_message(String reject_message) {
            this.reject_message = reject_message;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
