package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2018/3/19.
 */

public class PersonGroup {

    /**
     * data : [{"group_desc":"qwe","group_id":"20180319132620","group_name":"qwe","members":[{"department_name":"公司领导","id_":"E773B1A1-450B-491F-A363-ABEA0ADBF0D5","user_code":"033217","user_name":"沈帅祺","user_photo":"personal/033217.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e4adc23ed","user_code":"000731","user_name":"陈华斌","user_photo":"personal/000731.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528ed5783e83","user_code":"002209","user_name":"冯金明","user_photo":"personal/002209.jpg"},{"department_name":"公司领导","id_":"000000005d124941015d21c9e0ca7bad","user_code":"P00083","user_name":"胡俊辉","user_photo":"personal/P00083.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e66e8297e","user_code":"005488","user_name":"李春丰","user_photo":"personal/005488.jpg"},{"department_name":"公司领导","id_":"E773B1A1-450B-491F-A363-ABEA0ADBF0D5","user_code":"033217","user_name":"沈帅祺","user_photo":"personal/033217.jpg"}],"user_code":"033523","user_name":"李阳洋"},{"group_desc":"1234","group_id":"20180319132726","group_name":"qwer","members":[{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e4adc23ed","user_code":"000731","user_name":"陈华斌","user_photo":"personal/000731.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528ed5783e83","user_code":"002209","user_name":"冯金明","user_photo":"personal/002209.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e66e8297e","user_code":"005488","user_name":"李春丰","user_photo":"personal/005488.jpg"},{"department_name":"公司领导","id_":"000000005d124941015d21c9e0ca7bad","user_code":"P00083","user_name":"胡俊辉","user_photo":"personal/P00083.jpg"}],"user_code":"033523","user_name":"李阳洋"}]
     * success : 0
     */

    private int success;
    private List<GroupBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<GroupBean> getData() {
        return data;
    }

    public void setData(List<GroupBean> data) {
        this.data = data;
    }

    public static class GroupBean implements Serializable {
        /**
         * group_desc : qwe
         * group_id : 20180319132620
         * group_name : qwe
         * members : [{"department_name":"公司领导","id_":"E773B1A1-450B-491F-A363-ABEA0ADBF0D5","user_code":"033217","user_name":"沈帅祺","user_photo":"personal/033217.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e4adc23ed","user_code":"000731","user_name":"陈华斌","user_photo":"personal/000731.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528ed5783e83","user_code":"002209","user_name":"冯金明","user_photo":"personal/002209.jpg"},{"department_name":"公司领导","id_":"000000005d124941015d21c9e0ca7bad","user_code":"P00083","user_name":"胡俊辉","user_photo":"personal/P00083.jpg"},{"department_name":"公司领导","id_":"8a8a83db3a5287b2013a528e66e8297e","user_code":"005488","user_name":"李春丰","user_photo":"personal/005488.jpg"},{"department_name":"公司领导","id_":"E773B1A1-450B-491F-A363-ABEA0ADBF0D5","user_code":"033217","user_name":"沈帅祺","user_photo":"personal/033217.jpg"}]
         * user_code : 033523
         * user_name : 李阳洋
         */

        private String group_desc;
        private String group_id;
        private String group_name;
        private String user_code;
        private String user_name;
        private List<MembersBean> members;

        public String getGroup_desc() {
            return group_desc;
        }

        public void setGroup_desc(String group_desc) {
            this.group_desc = group_desc;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getUser_code() {
            return user_code;
        }

        public void setUser_code(String user_code) {
            this.user_code = user_code;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public static class MembersBean implements Serializable {
            /**
             * department_name : 公司领导
             * id_ : E773B1A1-450B-491F-A363-ABEA0ADBF0D5
             * user_code : 033217
             * user_name : 沈帅祺
             * user_photo : personal/033217.jpg
             */

            private String department_name;
            private String id_;
            private String user_code;
            private String user_name;
            private String user_photo;
            private boolean is_select;

            public String getDepartment_name() {
                return department_name;
            }

            public void setDepartment_name(String department_name) {
                this.department_name = department_name;
            }

            public String getId_() {
                return id_;
            }

            public void setId_(String id_) {
                this.id_ = id_;
            }

            public String getUser_code() {
                return user_code;
            }

            public void setUser_code(String user_code) {
                this.user_code = user_code;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_photo() {
                return user_photo;
            }

            public void setUser_photo(String user_photo) {
                this.user_photo = user_photo;
            }

            public boolean isIs_select() {
                return is_select;
            }

            public void setIs_select(boolean is_select) {
                this.is_select = is_select;
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
