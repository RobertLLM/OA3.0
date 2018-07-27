package cn.invonate.ygoa3.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MeetingDetail {

    /**
     * code : 0000
     * message : OK
     * result : {"addressId":"ca9db0af-532e-460b-a269-a84ed2394a78","addressName":"集团公司新办公楼","attendList":[{"userCode":"034488","userId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","userName":"谢凌云"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"}],"attendNum":0,"createTime":1527745392000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamic":3,"endTime":1530331200000,"id":"6cad52d1-00ea-47b9-88e6-90b57d06a2a2","joinStatus":"0","meetingGoingStatus":"0","meetingStatus":"0","recordPersonCode":"034488","recordPersonId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","recordPersonName":"谢凌云","startTime":1530327600000,"title":"123456","totalNum":5}
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

    public static class ResultBean implements Serializable {
        /**
         * addressId : ca9db0af-532e-460b-a269-a84ed2394a78
         * addressName : 集团公司新办公楼
         * attendList : [{"userCode":"034488","userId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","userName":"谢凌云"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"},{"userCode":"035298","userId":"4C9FFBC3-1049-45D1-938A-15E9B484A4B2","userName":"薛成"}]
         * attendNum : 0
         * createTime : 1527745392000
         * creatorCode : 034488
         * creatorId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
         * creatorName : 谢凌云
         * dynamic : 3
         * endTime : 1530331200000
         * id : 6cad52d1-00ea-47b9-88e6-90b57d06a2a2
         * joinStatus : 0
         * meetingGoingStatus : 0
         * meetingStatus : 0
         * recordPersonCode : 034488
         * recordPersonId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
         * recordPersonName : 谢凌云
         * startTime : 1530327600000
         * title : 123456
         * totalNum : 5
         */

        private String addressId;
        private String addressName;
        private int attendNum;
        private long createTime;
        private String creatorCode;
        private String creatorId;
        private String creatorName;
        private int dynamic;
        private long endTime;
        private String id;
        private String joinStatus;
        private String meetingGoingStatus;
        private String meetingStatus;
        private String recordPersonCode;
        private String recordPersonId;
        private String recordPersonName;
        private long startTime;
        private String title;
        private int totalNum;
        private ArrayList<AttendListBean> attendList;
        private String roomName;
        private List<MeetFile> fileList;

        public List<MeetFile> getFileList() {
            return fileList;
        }

        public void setFileList(List<MeetFile> fileList) {
            this.fileList = fileList;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public int getAttendNum() {
            return attendNum;
        }

        public void setAttendNum(int attendNum) {
            this.attendNum = attendNum;
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

        public int getDynamic() {
            return dynamic;
        }

        public void setDynamic(int dynamic) {
            this.dynamic = dynamic;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJoinStatus() {
            return joinStatus;
        }

        public void setJoinStatus(String joinStatus) {
            this.joinStatus = joinStatus;
        }

        public String getMeetingGoingStatus() {
            return meetingGoingStatus;
        }

        public void setMeetingGoingStatus(String meetingGoingStatus) {
            this.meetingGoingStatus = meetingGoingStatus;
        }

        public String getMeetingStatus() {
            return meetingStatus;
        }

        public void setMeetingStatus(String meetingStatus) {
            this.meetingStatus = meetingStatus;
        }

        public String getRecordPersonCode() {
            return recordPersonCode;
        }

        public void setRecordPersonCode(String recordPersonCode) {
            this.recordPersonCode = recordPersonCode;
        }

        public String getRecordPersonId() {
            return recordPersonId;
        }

        public void setRecordPersonId(String recordPersonId) {
            this.recordPersonId = recordPersonId;
        }

        public String getRecordPersonName() {
            return recordPersonName;
        }

        public void setRecordPersonName(String recordPersonName) {
            this.recordPersonName = recordPersonName;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public ArrayList<AttendListBean> getAttendList() {
            return attendList;
        }

        public void setAttendList(ArrayList<AttendListBean> attendList) {
            this.attendList = attendList;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public static class AttendListBean implements Serializable {
            /**
             * userCode : 034488
             * userId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
             * userName : 谢凌云
             */

            private String userCode;
            private String userId;
            private String userName;

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

        public static class MeetFile implements Serializable {
            private String fileId;
            private String fileName;
            private String fileURL;
            private long size;

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getFileURL() {
                return fileURL;
            }

            public void setFileURL(String fileURL) {
                this.fileURL = fileURL;
            }

            public long getSize() {
                return size;
            }

            public void setSize(long size) {
                this.size = size;
            }
        }
    }
}
