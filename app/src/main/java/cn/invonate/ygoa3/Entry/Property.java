package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2018/2/26.
 */

public class Property {

    /**
     * data : [{"asset_category_name":"文件柜","asset_code":"6010501012371","code_":"304536000014816","cost_":"0","department_name":"恒创软件综合科","finance_category_name":"办公家具用品","measurement_":"张","name_":"文件柜","principal_code":"033523","principal_name":"李阳洋","principal_tel":"18626359305","qty":"1","quondam_cost":"780","specification_":"组合桌"},{"asset_category_name":"文件柜","asset_code":"6010501007991","code_":"304536000010480","cost_":"0","department_name":"恒创软件综合科","finance_category_name":"办公家具用品","measurement_":"张","name_":"文件柜","principal_code":"033523","principal_name":"李阳洋","principal_tel":"18626359305","qty":"1","quondam_cost":"450","specification_":"无扶手"},{"asset_category_name":"文件柜","asset_code":"6010501004802","code_":"30453600007298","cost_":"0","department_name":"恒创软件综合科","finance_category_name":"办公家具用品","measurement_":"张","name_":"文件柜","principal_code":"033523","principal_name":"李阳洋","principal_tel":"18626359305","qty":"1","quondam_cost":"380"},{"asset_category_name":"便携式计算机","asset_code":"2010105000048","code_":"1040153700006901","cost_":"0","department_name":"恒创软件软件开发科","finance_category_name":"IT设备","measurement_":"台","name_":"便携式计算机","principal_code":"033523","principal_name":"李阳洋","principal_tel":"18626359305","qty":"1","quondam_cost":"5470","specification_":"联想Thinkpad E570 20H5A024CD  "}]
     * success : 0
     */

    private int success;
    private List<PropertyBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<PropertyBean> getData() {
        return data;
    }

    public void setData(List<PropertyBean> data) {
        this.data = data;
    }

    public static class PropertyBean implements Serializable{
        /**
         * asset_category_name : 文件柜
         * asset_code : 6010501012371
         * code_ : 304536000014816
         * cost_ : 0
         * department_name : 恒创软件综合科
         * finance_category_name : 办公家具用品
         * measurement_ : 张
         * name_ : 文件柜
         * principal_code : 033523
         * principal_name : 李阳洋
         * principal_tel : 18626359305
         * qty : 1
         * quondam_cost : 780
         * specification_ : 组合桌
         */

        private String asset_category_name;
        private String asset_code;
        private String code_;
        private String cost_;
        private String department_name;
        private String finance_category_name;
        private String measurement_;
        private String name_;
        private String principal_code;
        private String principal_name;
        private String principal_tel;
        private String qty;
        private String quondam_cost;
        private String specification_;

        public String getAsset_category_name() {
            return asset_category_name;
        }

        public void setAsset_category_name(String asset_category_name) {
            this.asset_category_name = asset_category_name;
        }

        public String getAsset_code() {
            return asset_code;
        }

        public void setAsset_code(String asset_code) {
            this.asset_code = asset_code;
        }

        public String getCode_() {
            return code_;
        }

        public void setCode_(String code_) {
            this.code_ = code_;
        }

        public String getCost_() {
            return cost_;
        }

        public void setCost_(String cost_) {
            this.cost_ = cost_;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String department_name) {
            this.department_name = department_name;
        }

        public String getFinance_category_name() {
            return finance_category_name;
        }

        public void setFinance_category_name(String finance_category_name) {
            this.finance_category_name = finance_category_name;
        }

        public String getMeasurement_() {
            return measurement_;
        }

        public void setMeasurement_(String measurement_) {
            this.measurement_ = measurement_;
        }

        public String getName_() {
            return name_;
        }

        public void setName_(String name_) {
            this.name_ = name_;
        }

        public String getPrincipal_code() {
            return principal_code;
        }

        public void setPrincipal_code(String principal_code) {
            this.principal_code = principal_code;
        }

        public String getPrincipal_name() {
            return principal_name;
        }

        public void setPrincipal_name(String principal_name) {
            this.principal_name = principal_name;
        }

        public String getPrincipal_tel() {
            return principal_tel;
        }

        public void setPrincipal_tel(String principal_tel) {
            this.principal_tel = principal_tel;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getQuondam_cost() {
            return quondam_cost;
        }

        public void setQuondam_cost(String quondam_cost) {
            this.quondam_cost = quondam_cost;
        }

        public String getSpecification_() {
            return specification_;
        }

        public void setSpecification_(String specification_) {
            this.specification_ = specification_;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
