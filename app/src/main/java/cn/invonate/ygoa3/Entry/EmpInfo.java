package cn.invonate.ygoa3.Entry;

import java.util.List;

public class EmpInfo {

    /**
     * code : 0000
     * message : 查询成功
     * result : [{"cadreAge":"","education":"本科","empAge":"37","empJob":"主任","empName":"谢章建","empNo":"010888","empSex":"男","phoneNum":"18962200571","specialty":"机械设计","stockLevel":"副科","wageLevel":"32级","workingAge":"12"}]
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
         * cadreAge :
         * education : 本科
         * empAge : 37
         * empJob : 主任
         * empName : 谢章建
         * empNo : 010888
         * empSex : 男
         * phoneNum : 18962200571
         * specialty : 机械设计
         * stockLevel : 副科
         * wageLevel : 32级
         * workingAge : 12
         */

        private String cadreAge;
        private String education;
        private String empAge;
        private String empJob;
        private String empName;
        private String empNo;
        private String empSex;
        private String phoneNum;
        private String specialty;
        private String stockLevel;
        private String wageLevel;
        private String workingAge;

        public String getCadreAge() {
            return cadreAge;
        }

        public void setCadreAge(String cadreAge) {
            this.cadreAge = cadreAge;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getEmpAge() {
            return empAge;
        }

        public void setEmpAge(String empAge) {
            this.empAge = empAge;
        }

        public String getEmpJob() {
            return empJob;
        }

        public void setEmpJob(String empJob) {
            this.empJob = empJob;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getEmpNo() {
            return empNo;
        }

        public void setEmpNo(String empNo) {
            this.empNo = empNo;
        }

        public String getEmpSex() {
            return empSex;
        }

        public void setEmpSex(String empSex) {
            this.empSex = empSex;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getStockLevel() {
            return stockLevel;
        }

        public void setStockLevel(String stockLevel) {
            this.stockLevel = stockLevel;
        }

        public String getWageLevel() {
            return wageLevel;
        }

        public void setWageLevel(String wageLevel) {
            this.wageLevel = wageLevel;
        }

        public String getWorkingAge() {
            return workingAge;
        }

        public void setWorkingAge(String workingAge) {
            this.workingAge = workingAge;
        }
    }
}
