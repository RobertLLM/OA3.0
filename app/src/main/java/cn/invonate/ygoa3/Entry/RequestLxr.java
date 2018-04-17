package cn.invonate.ygoa3.Entry;

/**
 * Created by liyangyang on 2018/4/2.
 */

public class RequestLxr {
    private String syz_id;
    private String lxr_id;

    public RequestLxr(String syz_id, String lxr_id) {
        this.syz_id = syz_id;
        this.lxr_id = lxr_id;
    }

    public String getSyz_id() {
        return syz_id;
    }

    public void setSyz_id(String syz_id) {
        this.syz_id = syz_id;
    }

    public String getLxr_id() {
        return lxr_id;
    }

    public void setLxr_id(String lxr_id) {
        this.lxr_id = lxr_id;
    }


}
