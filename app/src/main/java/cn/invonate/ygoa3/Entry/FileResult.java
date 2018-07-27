package cn.invonate.ygoa3.Entry;

import java.util.List;

public class FileResult {

    /**
     * code : 0000
     * message : OK
     * result : {"fileIds":["673b7a3e-47fd-4242-ba07-a7f73ab60453"]}
     */

    private String code;
    private String message;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<String> fileIds;

        public List<String> getFileIds() {
            return fileIds;
        }

        public void setFileIds(List<String> fileIds) {
            this.fileIds = fileIds;
        }
    }
}
