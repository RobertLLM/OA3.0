package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Comment {

    /**
     * commList : [{"COMM_CONT":"55m96Imy5p6r5Y+2","COMM_ID":"00892e0d-3d4d-11e8-95ae-1866daf6a144","COMM_TIME":"2018-04-11 13:55:56","COMM_USER":"54817F3814E7F012E0530A0A0525F012","INFO_ID":"","USER_CODE":"034731","USER_NAME":"韦龙华","VALID_":"","replyList":[{"USER_CODE":"033523","USER_NAME":"李阳洋","comm_id":"00892e0d-3d4d-11e8-95ae-1866daf6a144","comm_usre_id":"","lomo_id":"","replay_content":"57qi6Imy55qE5aW955yL","replay_content_id":"5e72de3f-4693-11e8-95ae-1866daf6a144","replay_time":"2018-04-23 09:12:19","user_photo":"personal/033523.jpg","userid":"","valid_":""}],"user_photo":"personal/034731.jpg","userid":""}]
     * result : 0
     */

    private int result;
    private List<CommList> commList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<CommList> getCommList() {
        return commList;
    }

    public void setCommList(List<CommList> commList) {
        this.commList = commList;
    }

    public static class CommList {
        /**
         * COMM_CONT : 55m96Imy5p6r5Y+2
         * COMM_ID : 00892e0d-3d4d-11e8-95ae-1866daf6a144
         * COMM_TIME : 2018-04-11 13:55:56
         * COMM_USER : 54817F3814E7F012E0530A0A0525F012
         * INFO_ID :
         * USER_CODE : 034731
         * USER_NAME : 韦龙华
         * VALID_ :
         * replyList : [{"USER_CODE":"033523","USER_NAME":"李阳洋","comm_id":"00892e0d-3d4d-11e8-95ae-1866daf6a144","comm_usre_id":"","lomo_id":"","replay_content":"57qi6Imy55qE5aW955yL","replay_content_id":"5e72de3f-4693-11e8-95ae-1866daf6a144","replay_time":"2018-04-23 09:12:19","user_photo":"personal/033523.jpg","userid":"","valid_":""}]
         * user_photo : personal/034731.jpg
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
        private List<Reply> replyList;

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

        public List<Reply> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<Reply> replyList) {
            this.replyList = replyList;
        }

        public static class Reply {
            /**
             * USER_CODE : 033523
             * USER_NAME : 李阳洋
             * comm_id : 00892e0d-3d4d-11e8-95ae-1866daf6a144
             * comm_usre_id :
             * lomo_id :
             * replay_content : 57qi6Imy55qE5aW955yL
             * replay_content_id : 5e72de3f-4693-11e8-95ae-1866daf6a144
             * replay_time : 2018-04-23 09:12:19
             * user_photo : personal/033523.jpg
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
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
