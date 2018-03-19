package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liyangyang on 2018/2/26.
 */

public class Salary {

    /**
     * data : [{"sail":"2018-01","sailName":"月份"},{"sail":"6798.00","sailName":"应发工资"},{"sail":"280.63","sailName":"保险金"},{"sail":"263.00","sailName":"公积金"},{"sail":"0.00","sailName":"罚款"},{"sail":"0.00","sailName":"其他收入"},{"sail":"6254.37","sailName":"应税工资"},{"sail":"170.44","sailName":"所得税"},{"sail":"10.00","sailName":"工会会员会费1"},{"sail":"0.00","sailName":"工会会员会费2"},{"sail":"0.00","sailName":"为民基金捐款"},{"sail":"0.00","sailName":"培训费"},{"sail":"0.00","sailName":"房租"},{"sail":"0.00","sailName":"借款"},{"sail":"0.00","sailName":"其他"},{"sail":"0.00","sailName":"已发收入"},{"sail":"724.07","sailName":"扣款小计"},{"sail":"0.00","sailName":"红旗班组"},{"sail":"0.00","sailName":"节日补助"},{"sail":"0.00","sailName":"高温费"},{"sail":"6073.93","sailName":"实发工资"}]
     * gjjData : [[{"sail":"01","sailName":"月份"},{"sail":"2625.00","sailName":"保险缴费基数"},{"sail":"1076.00","sailName":"当月应缴合计|合计"},{"sail":"795.37","sailName":"当月应缴合计|集体"},{"sail":"280.63","sailName":"当月应缴合计|个人"}],[{"sail":"708.75","sailName":"养老保险|合计"},{"sail":"498.75","sailName":"养老保险|集体"},{"sail":"498.75","sailName":"养老保险|个人"}],[{"sail":"288.75","sailName":"医疗保险|合计"},{"sail":"236.25","sailName":"医疗保险|集体"},{"sail":"52.50","sailName":"医疗保险|个人"}],[{"sail":"26.25","sailName":"工伤保险|合计"},{"sail":"26.25","sailName":"工伤保险|集体"},{"sail":"0.00","sailName":"工伤保险|个人"}],[{"sail":"26.25","sailName":"失业保险|合计"},{"sail":"13.13","sailName":"失业保险|集体"},{"sail":"13.13","sailName":"失业保险|个人"}],[{"sail":"21.00","sailName":"生育保险|合计"},{"sail":"21.00","sailName":"生育保险|集体"},{"sail":"0.00","sailName":"生育保险|个人"}],[{"sail":"5.00","sailName":"大病风险个人"},{"sail":"280.63","sailName":"本月应缴金额"},{"sail":"0.00","sailName":"累计至上月欠款额"},{"sail":"280.63","sailName":"合计本月应扣金额"},{"sail":"280.63","sailName":"实际已扣"},{"sail":"0.00","sailName":"实际未扣"}],[{"sail":"2387","sailName":"公积金缴费基数"},{"sail":"526.00","sailName":"公积金|合计"},{"sail":"263.00","sailName":"公积金|集体"},{"sail":"263.00","sailName":"公积金|个人"},{"sail":"0.00","sailName":"累计至上月欠款额"},{"sail":"263.00","sailName":"合计本月应扣金额"},{"sail":"263.00","sailName":"实际已扣"}]]
     * success : 0
     */

    private int success;
    private ArrayList<DataBean> data;
    private ArrayList<ArrayList<GjjDataBean>> gjjData;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public ArrayList<ArrayList<GjjDataBean>> getGjjData() {
        return gjjData;
    }

    public void setGjjData(ArrayList<ArrayList<GjjDataBean>> gjjData) {
        this.gjjData = gjjData;
    }

    public static class DataBean implements Serializable {
        /**
         * sail : 2018-01
         * sailName : 月份
         */

        private String sail;
        private String sailName;

        public String getSail() {
            return sail;
        }

        public void setSail(String sail) {
            this.sail = sail;
        }

        public String getSailName() {
            return sailName;
        }

        public void setSailName(String sailName) {
            this.sailName = sailName;
        }
    }

    public static class GjjDataBean implements Serializable {
        /**
         * sail : 01
         * sailName : 月份
         */

        private String sail;
        private String sailName;

        public String getSail() {
            return sail;
        }

        public void setSail(String sail) {
            this.sail = sail;
        }

        public String getSailName() {
            return sailName;
        }

        public void setSailName(String sailName) {
            this.sailName = sailName;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
