package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class CycleMessage {

    /**
     * infolist : [{"COMM_CONT":"NjY2NjY=","COMM_ID":"3798ad57-4e96-11e8-a143-1866daf6a144","COMM_TIME":"2018-05-03 13:52:51","COMM_USER":"","INFO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","USER_CODE":"034488","USER_NAME":"谢凌云","VALID_":"","replyList":[],"user_photo":"personal/034488.jpg","userid":""}]
     * replyList : [{"USER_CODE":"034488","USER_NAME":"谢凌云","comm_id":"","comm_usre_id":"","lomo_id":"c95a02a0-3a2a-11e8-95ae-1866daf6a144","replay_content":"Nzc3","replay_content_id":"","replay_time":"2018-05-03 13:53:16","user_photo":"personal/034488.jpg","userid":"","valid_":""}]
     * result : 0
     * thumblist : [{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-05-03 13:52:00","THUMB_ID":"3E416010-1B9A-41D4-9AB9-F1DBF7D1D09D","USER_CODE":"034488","USER_ID":"000000005d124941015d21cc56a07bbf","USER_NAME":"谢凌云","VALID_":"","count":"","user_photo":"personal/034488.jpg","userid":""},{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-05-02 16:13:00","THUMB_ID":"04D2F4E0-47E1-488F-A279-8F554BB0A551","USER_CODE":"019021","USER_ID":"8ad98d3950c1babf0150c1c19e2c0ac8","USER_NAME":"蔡建飞","VALID_":"","count":"","user_photo":"personal/019021.jpg","userid":""},{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-05-01 19:12:00","THUMB_ID":"BE3D75B5-99C4-4931-BF05-D7436B8017D3","USER_CODE":"012030","USER_ID":"8ad98d3950c1babf0150c1bc52a302b0","USER_NAME":"朱长州","VALID_":"","count":"","user_photo":"personal/012030.jpg","userid":""},{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-04-27 13:10:00","THUMB_ID":"3676307F-6748-40B6-ADE2-8645C827B335","USER_CODE":"033216","USER_ID":"000000005611de1101561aafba0d4628","USER_NAME":"赵日阳","VALID_":"","count":"","user_photo":"personal/033216.jpg","userid":""},{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-04-26 14:25:00","THUMB_ID":"D44FDAAA-0D7C-427E-A494-8C8A6282BD8F","USER_CODE":"012491","USER_ID":"8ad98d3950c1babf0150c1bcab8b036c","USER_NAME":"毛福华","VALID_":"","count":"","user_photo":"personal/012491.jpg","userid":""},{"LOMO_ID":"a61b9e7e-4913-11e8-95ae-1866daf6a144","THUMB_CREATE_TIME":"2018-04-26 13:58:00","THUMB_ID":"02F3323D-5E26-4A1C-A773-7084772CD841","USER_CODE":"016479","USER_ID":"8ad98d3950c10ef50150c164ebe10bae","USER_NAME":"欧阳浩","VALID_":"","count":"","user_photo":"personal/016479.jpg","userid":""}]
     */

    private int result;
    private List<InfolistBean> infolist;// 评论
    private List<ReplyListBean> replyList;// 回复
    private List<ThumblistBean> thumblist;// 点赞

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<InfolistBean> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<InfolistBean> infolist) {
        this.infolist = infolist;
    }

    public List<ReplyListBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyListBean> replyList) {
        this.replyList = replyList;
    }

    public List<ThumblistBean> getThumblist() {
        return thumblist;
    }

    public void setThumblist(List<ThumblistBean> thumblist) {
        this.thumblist = thumblist;
    }

    public static class InfolistBean {
        /**
         * COMM_CONT : NjY2NjY=
         * COMM_ID : 3798ad57-4e96-11e8-a143-1866daf6a144
         * COMM_TIME : 2018-05-03 13:52:51
         * COMM_USER :
         * INFO_ID : a61b9e7e-4913-11e8-95ae-1866daf6a144
         * USER_CODE : 034488
         * USER_NAME : 谢凌云
         * VALID_ :
         * replyList : []
         * user_photo : personal/034488.jpg
         * userid :
         */

        private String COMM_CONT;
        private String COMM_ID;
        private String COMM_TIME;
        private String COMM_USER;
        private String INFO_ID;
        private String USER_CODE;
        private String USER_NAME;
        private String VALID_;
        private String user_photo;
        private String userid;
        private List<?> replyList;

        public String getCOMM_CONT() {
            return COMM_CONT;
        }

        public void setCOMM_CONT(String COMM_CONT) {
            this.COMM_CONT = COMM_CONT;
        }

        public String getCOMM_ID() {
            return COMM_ID;
        }

        public void setCOMM_ID(String COMM_ID) {
            this.COMM_ID = COMM_ID;
        }

        public String getCOMM_TIME() {
            return COMM_TIME;
        }

        public void setCOMM_TIME(String COMM_TIME) {
            this.COMM_TIME = COMM_TIME;
        }

        public String getCOMM_USER() {
            return COMM_USER;
        }

        public void setCOMM_USER(String COMM_USER) {
            this.COMM_USER = COMM_USER;
        }

        public String getINFO_ID() {
            return INFO_ID;
        }

        public void setINFO_ID(String INFO_ID) {
            this.INFO_ID = INFO_ID;
        }

        public String getUSER_CODE() {
            return USER_CODE;
        }

        public void setUSER_CODE(String USER_CODE) {
            this.USER_CODE = USER_CODE;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getVALID_() {
            return VALID_;
        }

        public void setVALID_(String VALID_) {
            this.VALID_ = VALID_;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public List<?> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<?> replyList) {
            this.replyList = replyList;
        }
    }

    public static class ReplyListBean {
        /**
         * USER_CODE : 034488
         * USER_NAME : 谢凌云
         * comm_id :
         * comm_usre_id :
         * lomo_id : c95a02a0-3a2a-11e8-95ae-1866daf6a144
         * replay_content : Nzc3
         * replay_content_id :
         * replay_time : 2018-05-03 13:53:16
         * user_photo : personal/034488.jpg
         * userid :
         * valid_ :
         */

        private String USER_CODE;
        private String USER_NAME;
        private String comm_id;
        private String comm_usre_id;
        private String lomo_id;
        private String replay_content;
        private String replay_content_id;
        private String replay_time;
        private String user_photo;
        private String userid;
        private String valid_;

        public String getUSER_CODE() {
            return USER_CODE;
        }

        public void setUSER_CODE(String USER_CODE) {
            this.USER_CODE = USER_CODE;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getComm_id() {
            return comm_id;
        }

        public void setComm_id(String comm_id) {
            this.comm_id = comm_id;
        }

        public String getComm_usre_id() {
            return comm_usre_id;
        }

        public void setComm_usre_id(String comm_usre_id) {
            this.comm_usre_id = comm_usre_id;
        }

        public String getLomo_id() {
            return lomo_id;
        }

        public void setLomo_id(String lomo_id) {
            this.lomo_id = lomo_id;
        }

        public String getReplay_content() {
            return replay_content;
        }

        public void setReplay_content(String replay_content) {
            this.replay_content = replay_content;
        }

        public String getReplay_content_id() {
            return replay_content_id;
        }

        public void setReplay_content_id(String replay_content_id) {
            this.replay_content_id = replay_content_id;
        }

        public String getReplay_time() {
            return replay_time;
        }

        public void setReplay_time(String replay_time) {
            this.replay_time = replay_time;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getValid_() {
            return valid_;
        }

        public void setValid_(String valid_) {
            this.valid_ = valid_;
        }
    }

    public static class ThumblistBean {
        /**
         * LOMO_ID : a61b9e7e-4913-11e8-95ae-1866daf6a144
         * THUMB_CREATE_TIME : 2018-05-03 13:52:00
         * THUMB_ID : 3E416010-1B9A-41D4-9AB9-F1DBF7D1D09D
         * USER_CODE : 034488
         * USER_ID : 000000005d124941015d21cc56a07bbf
         * USER_NAME : 谢凌云
         * VALID_ :
         * count :
         * user_photo : personal/034488.jpg
         * userid :
         */

        private String LOMO_ID;
        private String THUMB_CREATE_TIME;
        private String THUMB_ID;
        private String USER_CODE;
        private String USER_ID;
        private String USER_NAME;
        private String VALID_;
        private String count;
        private String user_photo;
        private String userid;

        public String getLOMO_ID() {
            return LOMO_ID;
        }

        public void setLOMO_ID(String LOMO_ID) {
            this.LOMO_ID = LOMO_ID;
        }

        public String getTHUMB_CREATE_TIME() {
            return THUMB_CREATE_TIME;
        }

        public void setTHUMB_CREATE_TIME(String THUMB_CREATE_TIME) {
            this.THUMB_CREATE_TIME = THUMB_CREATE_TIME;
        }

        public String getTHUMB_ID() {
            return THUMB_ID;
        }

        public void setTHUMB_ID(String THUMB_ID) {
            this.THUMB_ID = THUMB_ID;
        }

        public String getUSER_CODE() {
            return USER_CODE;
        }

        public void setUSER_CODE(String USER_CODE) {
            this.USER_CODE = USER_CODE;
        }

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getVALID_() {
            return VALID_;
        }

        public void setVALID_(String VALID_) {
            this.VALID_ = VALID_;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
