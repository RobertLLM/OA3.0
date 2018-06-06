package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class MeetingLocation {


    /**
     * code : 0000
     * message : OK
     * result : [{"districtName":"15号门联峰实业办公楼","rowGuid":"b66114ef-3d05-46a8-8d05-664e02a2056d"},{"districtName":"集团公司新办公楼","rowGuid":"ca9db0af-532e-460b-a269-a84ed2394a78"}]
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
         * districtName : 15号门联峰实业办公楼
         * rowGuid : b66114ef-3d05-46a8-8d05-664e02a2056d
         */

        private String districtName;
        private String rowGuid;

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getRowGuid() {
            return rowGuid;
        }

        public void setRowGuid(String rowGuid) {
            this.rowGuid = rowGuid;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
