package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by yang on 2018/3/19.
 */

public class MyApplicationList {


    /**
     * attributes : {}
     * checked :
     * children : []
     * function_code : C_GHSP_003
     * function_description :
     * function_image :
     * function_name : 慰问申请
     * function_type :
     * iconCls :
     * id :
     * id_ : 1cd93834-d2d0-1035-9a60-5afad4399f72
     * menutitle :
     * parent_id :
     * phone_url : /ygoa/view/wwsq/phone_wwsq_list.jsp
     * search_name :
     * state :
     * target :
     * target_ :
     * text :
     * url_ : /view/wwsq/doQueryWwsq.action
     */

    private AttributesBean attributes;
    private String checked;
    private String function_code;
    private String function_description;
    private String function_image;
    private String function_name;
    private String function_type;
    private String iconCls;
    private String id;
    private String id_;
    private String menutitle;
    private String parent_id;
    private String phone_url;
    private String search_name;
    private String state;
    private String target;
    private String target_;
    private String text;
    private String url_;
    private List<?> children;
    private int pic_id;

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public AttributesBean getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesBean attributes) {
        this.attributes = attributes;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getFunction_code() {
        return function_code;
    }

    public void setFunction_code(String function_code) {
        this.function_code = function_code;
    }

    public String getFunction_description() {
        return function_description;
    }

    public void setFunction_description(String function_description) {
        this.function_description = function_description;
    }

    public String getFunction_image() {
        return function_image;
    }

    public void setFunction_image(String function_image) {
        this.function_image = function_image;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getFunction_type() {
        return function_type;
    }

    public void setFunction_type(String function_type) {
        this.function_type = function_type;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getMenutitle() {
        return menutitle;
    }

    public void setMenutitle(String menutitle) {
        this.menutitle = menutitle;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPhone_url() {
        return phone_url;
    }

    public void setPhone_url(String phone_url) {
        this.phone_url = phone_url;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget_() {
        return target_;
    }

    public void setTarget_(String target_) {
        this.target_ = target_;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl_() {
        return url_;
    }

    public void setUrl_(String url_) {
        this.url_ = url_;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static class AttributesBean {
    }


    //新建内部类，保存图片和名称的关联，一对一关系
    public static class PicToName {
        String function_name;
        int pic_id;

        public PicToName(String function_name, int pic_id) {
            this.function_name = function_name;
            this.pic_id = pic_id;
        }

        public PicToName() {
        }

        public String getFunction_name() {
            return function_name;
        }

        public void setFunction_name(String function_name) {
            this.function_name = function_name;
        }

        public int getPic_id() {
            return pic_id;
        }

        public void setPic_id(int pic_id) {
            this.pic_id = pic_id;
        }
    }
    //新建内部类保存组和位置映射关系
    public static class Head2Location {
        String function_name;
        int position;

        public Head2Location() {
        }

        public Head2Location(String function_name, int position) {
            this.function_name = function_name;
            this.position = position;
        }

        public String getFunction_name() {
            return function_name;
        }

        public void setFunction_name(String function_name) {
            this.function_name = function_name;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return function_name;
        }
    }


}
