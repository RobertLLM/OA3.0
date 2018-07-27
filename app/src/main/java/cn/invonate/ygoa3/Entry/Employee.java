package cn.invonate.ygoa3.Entry;

import java.util.List;

public class Employee {

    /**
     * code : 0000
     * message : 查询成功
     * result : [{"depPK":"b351e133-bfac-4cf9-b61f-a8a52acba58a","empJob":"","empNO":"031992","empName":"殷卫星","empPK":"42ed4253-be03-4aed-ac95-4274132c02e2"},{"depPK":"b351e133-bfac-4cf9-b61f-a8a52acba58a","empJob":"主任","empNO":"004420","empName":"蔡永刚","empPK":"13636f9a-5926-4583-8397-932f186c24f0"},{"depPK":"b351e133-bfac-4cf9-b61f-a8a52acba58a","empJob":"副主任","empNO":"005697","empName":"黄裕东","empPK":"afc7ce44-5fea-44ea-bbcd-05750dcbe110"},{"depPK":"b351e133-bfac-4cf9-b61f-a8a52acba58a","empJob":"","empNO":"011891","empName":"高建峰","empPK":"50a787b3-4f48-4b8e-a471-17a45df719d0"}]
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
         * depPK : b351e133-bfac-4cf9-b61f-a8a52acba58a
         * empJob :
         * empNO : 031992
         * empName : 殷卫星
         * empPK : 42ed4253-be03-4aed-ac95-4274132c02e2
         */

        private String depPK;
        private String empJob;
        private String empNO;
        private String empName;
        private String empPK;

        public String getDepPK() {
            return depPK;
        }

        public void setDepPK(String depPK) {
            this.depPK = depPK;
        }

        public String getEmpJob() {
            return empJob;
        }

        public void setEmpJob(String empJob) {
            this.empJob = empJob;
        }

        public String getEmpNO() {
            return empNO;
        }

        public void setEmpNO(String empNO) {
            this.empNO = empNO;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getEmpPK() {
            return empPK;
        }

        public void setEmpPK(String empPK) {
            this.empPK = empPK;
        }
    }
}
