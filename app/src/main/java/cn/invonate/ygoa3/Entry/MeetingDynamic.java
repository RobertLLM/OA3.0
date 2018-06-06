package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class MeetingDynamic {

    /**
     * code : 0000
     * message : OK
     * result : [{"content":"【035298】薛成 签到 会议。","createTime":1527751300000,"creatorCode":"035298","creatorId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","creatorName":"薛成","dynamicType":"SI","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"054d6291-b58a-4479-ab8f-239a495b3a79"},{"content":"【034488】谢凌云 修改 会议。","createTime":1527748069000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamicType":"M","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"17e3388c-5e20-416c-bd9d-17ea9e290a4d"},{"content":"【034488】谢凌云 修改 会议。","createTime":1527747785000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamicType":"M","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"41a815fb-60c2-47f7-90c9-b61ec3bb7d58"},{"content":"【034488】谢凌云 创建 会议。","createTime":1527745392000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamicType":"C","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"5db8d5b6-973a-43b7-b5e4-b57d0320dc01"},{"content":"【034488】谢凌云 签到 会议。","createTime":1527751040000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamicType":"SI","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"a8882acc-0055-487d-8fe6-836e18b15bf3"},{"content":"【034488】谢凌云 签到 会议。","createTime":1527750076000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamicType":"SI","meetingId":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","rowGuid":"c4bcf070-a92e-42e3-861e-23a345b22b6e"}]
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
         * content : 【035298】薛成 签到 会议。
         * createTime : 1527751300000
         * creatorCode : 035298
         * creatorId : 4C9FFBC3-1049-45D1-938A-15E9B484A4B2
         * creatorName : 薛成
         * dynamicType : SI
         * meetingId : 6cad52d1-00ea-47b9-88e6-90b57d06a2a2
         * rowGuid : 054d6291-b58a-4479-ab8f-239a495b3a79
         */

        private String content;
        private long createTime;
        private String creatorCode;
        private String creatorId;
        private String creatorName;
        private String dynamicType;
        private String meetingId;
        private String rowGuid;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreatorCode() {
            return creatorCode;
        }

        public void setCreatorCode(String creatorCode) {
            this.creatorCode = creatorCode;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public String getDynamicType() {
            return dynamicType;
        }

        public void setDynamicType(String dynamicType) {
            this.dynamicType = dynamicType;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
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
