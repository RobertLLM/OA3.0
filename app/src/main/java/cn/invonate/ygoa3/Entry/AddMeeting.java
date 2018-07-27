package cn.invonate.ygoa3.Entry;

import java.util.List;

public class AddMeeting {
    private String meetingContent;
    private String startTime;
    private String endTime;
    private String addressId;
    private String roomId;
    private String recordPersonId;
    private String recordPersonName;
    private String recordPersonCode;
    private List<MeetingDetail.ResultBean.AttendListBean> joinList;
    private List<String> fileIds;

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public String getRecordPersonCode() {
        return recordPersonCode;
    }

    public void setRecordPersonCode(String recordPersonCode) {
        this.recordPersonCode = recordPersonCode;
    }

    public List<MeetingDetail.ResultBean.AttendListBean> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<MeetingDetail.ResultBean.AttendListBean> joinList) {
        this.joinList = joinList;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }
}
