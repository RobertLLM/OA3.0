package cn.invonate.ygoa3.Entry;

/**
 * Created by liyangyang on 2018/4/3.
 */

public class Version {

    /**
     * size : 530.00KB
     * version_num : sdfsd
     * id : 73d2b02e-36de-11e8-9e21-1866daf6a144
     * time : 2018-04-03 09:29:28.0
     * version_url : /androidVersion/2018/04/03/3B7E11A4-7357-43C7-84FF-30D29F8ECF0F.docx
     */

    private String size;
    private String version_num;
    private String version_url;
    private int version_code;

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion_num() {
        return version_num;
    }

    public void setVersion_num(String version_num) {
        this.version_num = version_num;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }
}
