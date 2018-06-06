package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by liyangyang on 2018/1/5.
 */

public class Contacts implements Serializable {
    private String id_;
    private String user_name;
    private String company_name;
    private String office_phone;
    private String user_phone;
    private String user_code;
    private String department_name;
    private String user_photo;
    private String sex_;
    private String name_all_spell;
    private String name_first_spell;
    private String rsbm_pk;
    private String sortLetters;  //显示数据拼音的首字母

    public Contacts() {

    }

    public Contacts(String user_name, String user_code) {
        this.user_name = user_name;
        this.user_code = user_code;
    }

    private boolean is_select;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOffice_phone() {
        return office_phone;
    }

    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getSex_() {
        return sex_;
    }

    public void setSex_(String sex_) {
        this.sex_ = sex_;
    }

    public String getName_all_spell() {
        return name_all_spell;
    }

    public void setName_all_spell(String name_all_spell) {
        this.name_all_spell = name_all_spell;
    }

    public String getName_first_spell() {
        return name_first_spell;
    }

    public void setName_first_spell(String name_first_spell) {
        this.name_first_spell = name_first_spell;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getRsbm_pk() {
        return rsbm_pk;
    }

    public void setRsbm_pk(String rsbm_pk) {
        this.rsbm_pk = rsbm_pk;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
