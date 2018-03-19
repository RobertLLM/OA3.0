package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/3/1.
 */

public class CyContacts {
    private int total;
    private List<CyContactsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CyContactsBean> getRows() {
        return rows;
    }

    public void setRows(List<CyContactsBean> rows) {
        this.rows = rows;
    }

    public static class CyContactsBean {
        private String c_id;
        private String department_name;
        private String id_;
        private int type;
        private String user_code;
        private String user_name;
        private String user_phone;
        private String user_photo;
        private String sortLetters;  //显示数据拼音的首字母

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
