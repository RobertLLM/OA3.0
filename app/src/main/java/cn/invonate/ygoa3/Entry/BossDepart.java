package cn.invonate.ygoa3.Entry;

import java.io.Serializable;
import java.util.List;

public class BossDepart {

    /**
     * code : 0000
     * message : 查询成功
     * result : [{"depPK":"6fb2ce7c-8899-474c-91ef-f7c3b60faa3f","depFatherPK":"","depNo":"00","depName":"董事局","leafNode":"0"}]
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

    public static class ResultBean implements Serializable {
        /**
         * depPK : 6fb2ce7c-8899-474c-91ef-f7c3b60faa3f
         * depFatherPK :
         * depNo : 00
         * depName : 董事局
         * leafNode : 0
         */

        private String depPK;
        private String depFatherPK;
        private String depNo;
        private String depName;
        private int leafNode;

        public String getDepPK() {
            return depPK;
        }

        public void setDepPK(String depPK) {
            this.depPK = depPK;
        }

        public String getDepFatherPK() {
            return depFatherPK;
        }

        public void setDepFatherPK(String depFatherPK) {
            this.depFatherPK = depFatherPK;
        }

        public String getDepNo() {
            return depNo;
        }

        public void setDepNo(String depNo) {
            this.depNo = depNo;
        }

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public int getLeafNode() {
            return leafNode;
        }

        public void setLeafNode(int leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public String toString() {
            return depName;
        }
    }
}
