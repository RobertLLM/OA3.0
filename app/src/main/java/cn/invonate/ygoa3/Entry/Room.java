package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room {

    /**
     * code : 0000
     * message : OK
     * result : {"endRow":10,"firstPage":1,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":true,"lastPage":1,"list":[{"deviceAudio":"0","deviceProjector":"1","indexList":["0","1"],"layout":"回行会议桌，椅子28张，有投影","roomBuilding":"档案楼二楼","roomFloor":"2","roomId":"0a11749f-daf3-48d4-9fa8-89578685c553","roomNo":"D21会议室","roomSize":"28","timeList":[{"endTime":1528246800000,"startTime":1528243200000}]},{"deviceAudio":"1","deviceProjector":"1","indexList":[],"layout":"回形会议桌，椅子86张","roomFloor":"4","roomId":"0dab8b47-5326-4b80-a18e-4de4d38c6c93","roomNo":"409会议室","roomSize":"74","timeList":[]},{"deviceAudio":"0","deviceProjector":"1","indexList":[],"layout":"回形会议桌，椅子34张","roomFloor":"3","roomId":"384fb256-3694-4d59-9259-d1b28f22e008","roomNo":"304会议室","roomSize":"30","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子15张","roomFloor":"4","roomId":"3aa369cf-5613-46a9-b8fe-90663413db4c","roomNo":"402会议室","roomSize":"15","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"方形会议桌1张，椅子12张","roomFloor":"3","roomId":"48fe7805-3ed1-4764-a641-3ea46fd326d6","roomNo":"305会议室","roomSize":"15","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子16张","remark":"调配使用","roomFloor":"2","roomId":"5fc43a58-0839-49e8-8e6f-670562aaa7f8","roomNo":"201会议室","roomSize":"16","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"方形会议桌1张，椅子18张","roomFloor":"5","roomId":"7020e104-6861-4cd9-a664-ac0959b19251","roomNo":"504小木屋会议室","roomSize":"18","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子12张","roomFloor":"2","roomId":"83c66cdf-2298-4f05-9271-f0dd8759fab2","roomNo":"206会议室","roomSize":"12","timeList":[]},{"deviceAudio":"0","deviceProjector":"1","indexList":[],"layout":"主席台1张，条形桌19张，椅子57张，","roomFloor":"3","roomId":"933e5e0a-4892-4d4b-a501-7da7c4a51d20","roomNo":"310会议室","roomSize":"57","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"roomFloor":"2","roomId":"c1914c0d-ad3d-4527-8805-3a5852c1dda9","roomNo":"211会议室","timeList":[]}],"navigatePages":8,"navigatepageNums":[1],"nextPage":0,"pageNum":1,"pageSize":10,"pages":1,"prePage":0,"size":10,"startRow":1,"total":10}
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
         * endRow : 10
         * firstPage : 1
         * hasNextPage : false
         * hasPreviousPage : false
         * isFirstPage : true
         * isLastPage : true
         * lastPage : 1
         * list : [{"deviceAudio":"0","deviceProjector":"1","indexList":["0","1"],"layout":"回行会议桌，椅子28张，有投影","roomBuilding":"档案楼二楼","roomFloor":"2","roomId":"0a11749f-daf3-48d4-9fa8-89578685c553","roomNo":"D21会议室","roomSize":"28","timeList":[{"endTime":1528246800000,"startTime":1528243200000}]},{"deviceAudio":"1","deviceProjector":"1","indexList":[],"layout":"回形会议桌，椅子86张","roomFloor":"4","roomId":"0dab8b47-5326-4b80-a18e-4de4d38c6c93","roomNo":"409会议室","roomSize":"74","timeList":[]},{"deviceAudio":"0","deviceProjector":"1","indexList":[],"layout":"回形会议桌，椅子34张","roomFloor":"3","roomId":"384fb256-3694-4d59-9259-d1b28f22e008","roomNo":"304会议室","roomSize":"30","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子15张","roomFloor":"4","roomId":"3aa369cf-5613-46a9-b8fe-90663413db4c","roomNo":"402会议室","roomSize":"15","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"方形会议桌1张，椅子12张","roomFloor":"3","roomId":"48fe7805-3ed1-4764-a641-3ea46fd326d6","roomNo":"305会议室","roomSize":"15","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子16张","remark":"调配使用","roomFloor":"2","roomId":"5fc43a58-0839-49e8-8e6f-670562aaa7f8","roomNo":"201会议室","roomSize":"16","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"方形会议桌1张，椅子18张","roomFloor":"5","roomId":"7020e104-6861-4cd9-a664-ac0959b19251","roomNo":"504小木屋会议室","roomSize":"18","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"layout":"椭圆会议桌1张，椅子12张","roomFloor":"2","roomId":"83c66cdf-2298-4f05-9271-f0dd8759fab2","roomNo":"206会议室","roomSize":"12","timeList":[]},{"deviceAudio":"0","deviceProjector":"1","indexList":[],"layout":"主席台1张，条形桌19张，椅子57张，","roomFloor":"3","roomId":"933e5e0a-4892-4d4b-a501-7da7c4a51d20","roomNo":"310会议室","roomSize":"57","timeList":[]},{"deviceAudio":"0","deviceProjector":"0","indexList":[],"roomFloor":"2","roomId":"c1914c0d-ad3d-4527-8805-3a5852c1dda9","roomNo":"211会议室","timeList":[]}]
         * navigatePages : 8
         * navigatepageNums : [1]
         * nextPage : 0
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * prePage : 0
         * size : 10
         * startRow : 1
         * total : 10
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
        private ArrayList<ListBean> list;
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

        public ArrayList<ListBean> getList() {
            return list;
        }

        public void setList(ArrayList<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean implements Serializable {
            /**
             * deviceAudio : 0
             * deviceProjector : 1
             * indexList : ["0","1"]
             * layout : 回行会议桌，椅子28张，有投影
             * roomBuilding : 档案楼二楼
             * roomFloor : 2
             * roomId : 0a11749f-daf3-48d4-9fa8-89578685c553
             * roomNo : D21会议室
             * roomSize : 28
             * timeList : [{"endTime":1528246800000,"startTime":1528243200000}]
             * remark : 调配使用
             */

            private String deviceAudio;
            private String deviceProjector;
            private String layout;
            private String roomBuilding;
            private String roomFloor;
            private String roomId;
            private String roomNo;
            private String roomSize;
            private String remark;
            private List<Integer> indexList;
            private List<TimeListBean> timeList;
            private List<Integer> selectList;
            private int start_h = 8;
            private int start_m = 0;
            private int start_s = 0;
            private int end_h = 9;
            private int end_m = 0;
            private int end_s = 0;

            public String getDeviceAudio() {
                return deviceAudio;
            }

            public void setDeviceAudio(String deviceAudio) {
                this.deviceAudio = deviceAudio;
            }

            public String getDeviceProjector() {
                return deviceProjector;
            }

            public void setDeviceProjector(String deviceProjector) {
                this.deviceProjector = deviceProjector;
            }

            public String getLayout() {
                return layout;
            }

            public void setLayout(String layout) {
                this.layout = layout;
            }

            public String getRoomBuilding() {
                return roomBuilding;
            }

            public void setRoomBuilding(String roomBuilding) {
                this.roomBuilding = roomBuilding;
            }

            public String getRoomFloor() {
                return roomFloor;
            }

            public void setRoomFloor(String roomFloor) {
                this.roomFloor = roomFloor;
            }

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public String getRoomNo() {
                return roomNo;
            }

            public void setRoomNo(String roomNo) {
                this.roomNo = roomNo;
            }

            public String getRoomSize() {
                return roomSize;
            }

            public void setRoomSize(String roomSize) {
                this.roomSize = roomSize;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public List<Integer> getIndexList() {
                return indexList;
            }

            public void setIndexList(List<Integer> indexList) {
                this.indexList = indexList;
            }

            public List<TimeListBean> getTimeList() {
                return timeList;
            }

            public void setTimeList(List<TimeListBean> timeList) {
                this.timeList = timeList;
            }

            public int getStart_h() {
                return start_h;
            }

            public void setStart_h(int start_h) {
                this.start_h = start_h;
            }

            public int getStart_m() {
                return start_m;
            }

            public void setStart_m(int start_m) {
                this.start_m = start_m;
            }

            public int getStart_s() {
                return start_s;
            }

            public void setStart_s(int start_s) {
                this.start_s = start_s;
            }

            public int getEnd_h() {
                return end_h;
            }

            public void setEnd_h(int end_h) {
                this.end_h = end_h;
            }

            public int getEnd_m() {
                return end_m;
            }

            public void setEnd_m(int end_m) {
                this.end_m = end_m;
            }

            public int getEnd_s() {
                return end_s;
            }

            public void setEnd_s(int end_s) {
                this.end_s = end_s;
            }

            public List<Integer> getSelectList() {
                return selectList;
            }

            public void setSelectList(List<Integer> selectList) {
                this.selectList = selectList;
            }

            public static class TimeListBean implements Serializable{
                /**
                 * endTime : 1528246800000
                 * startTime : 1528243200000
                 */

                private long endTime;
                private long startTime;

                public long getEndTime() {
                    return endTime;
                }

                public void setEndTime(long endTime) {
                    this.endTime = endTime;
                }

                public long getStartTime() {
                    return startTime;
                }

                public void setStartTime(long startTime) {
                    this.startTime = startTime;
                }
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
