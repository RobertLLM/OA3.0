package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meeting {

    /**
     * code : 0000
     * message : OK
     * result : {"endRow":2,"firstPage":1,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":true,"lastPage":1,"list":[{"addressId":"b66114ef-3d05-46a8-8d05-664e02a2056d","attendNum":0,"createTime":1527579231000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamic":2,"endTime":1527732000000,"id":"b4bcfef9-e6b9-4ab4-9f89-470c960573d6","joinStatus":"0","meetingStatus":"0","recordPersonCode":"034488","recordPersonId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","recordPersonName":"谢凌云","startTime":1527728400000,"title":"123456","totalNum":6},{"addressId":"b66114ef-3d05-46a8-8d05-664e02a2056d","attendNum":0,"createTime":1527583406000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamic":1,"endTime":1527645600000,"id":"308dde7c-a883-4e95-9231-bbbae47760f1","joinStatus":"0","meetingStatus":"0","recordPersonCode":"034488","recordPersonId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","recordPersonName":"谢凌云","startTime":1527642000000,"title":"112233445566","totalNum":1}],"navigatePages":8,"navigatepageNums":[1],"nextPage":0,"pageNum":1,"pageSize":10,"pages":1,"prePage":0,"size":2,"startRow":1,"total":2}
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
        /**
         * endRow : 2
         * firstPage : 1
         * hasNextPage : false
         * hasPreviousPage : false
         * isFirstPage : true
         * isLastPage : true
         * lastPage : 1
         * list : [{"addressId":"b66114ef-3d05-46a8-8d05-664e02a2056d","attendNum":0,"createTime":1527579231000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamic":2,"endTime":1527732000000,"id":"b4bcfef9-e6b9-4ab4-9f89-470c960573d6","joinStatus":"0","meetingStatus":"0","recordPersonCode":"034488","recordPersonId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","recordPersonName":"谢凌云","startTime":1527728400000,"title":"123456","totalNum":6},{"addressId":"b66114ef-3d05-46a8-8d05-664e02a2056d","attendNum":0,"createTime":1527583406000,"creatorCode":"034488","creatorId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","creatorName":"谢凌云","dynamic":1,"endTime":1527645600000,"id":"308dde7c-a883-4e95-9231-bbbae47760f1","joinStatus":"0","meetingStatus":"0","recordPersonCode":"034488","recordPersonId":"4CE0C753-9239-4B49-AABF-BFD3E50F46C5","recordPersonName":"谢凌云","startTime":1527642000000,"title":"112233445566","totalNum":1}]
         * navigatePages : 8
         * navigatepageNums : [1]
         * nextPage : 0
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * prePage : 0
         * size : 2
         * startRow : 1
         * total : 2
         */

        private int endRow;
        private int firstPage;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private int lastPage;
        private int navigatePages;
        private int nextPage;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int prePage;
        private int size;
        private int startRow;
        private int total;
        private ArrayList<MeetBean> list;
        private List<Integer> navigatepageNums;

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public ArrayList<MeetBean> getList() {
            return list;
        }

        public void setList(ArrayList<MeetBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class MeetBean implements Serializable {
            /**
             * addressId : b66114ef-3d05-46a8-8d05-664e02a2056d
             * attendNum : 0
             * createTime : 1527579231000
             * creatorCode : 034488
             * creatorId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
             * creatorName : 谢凌云
             * dynamic : 2
             * endTime : 1527732000000
             * id : b4bcfef9-e6b9-4ab4-9f89-470c960573d6
             * joinStatus : 0
             * meetingStatus : 0
             * recordPersonCode : 034488
             * recordPersonId : 4CE0C753-9239-4B49-AABF-BFD3E50F46C5
             * recordPersonName : 谢凌云
             * startTime : 1527728400000
             * title : 123456
             * totalNum : 6
             */

            private String addressId;
            private int attendNum;
            private long createTime;
            private String creatorCode;
            private String creatorId;
            private String creatorName;
            private int dynamic;
            private long endTime;
            private String id;
            private String joinStatus;
            private String meetingStatus;// 0正常 1取消
            private String recordPersonCode;
            private String recordPersonId;
            private String recordPersonName;
            private long startTime;
            private String title;
            private int totalNum;
            private String meetingGoingStatus;// 0未进行，1已结束。2进行中
            private String addressName;
            private String roomName;

            public String getAddressId() {
                return addressId;
            }

            public void setAddressId(String addressId) {
                this.addressId = addressId;
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

            public String getMeetingGoingStatus() {
                return meetingGoingStatus;
            }

            public void setMeetingGoingStatus(String meetingGoingStatus) {
                this.meetingGoingStatus = meetingGoingStatus;
            }

            public String getAddressName() {
                return addressName;
            }

            public void setAddressName(String addressName) {
                this.addressName = addressName;
            }

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
