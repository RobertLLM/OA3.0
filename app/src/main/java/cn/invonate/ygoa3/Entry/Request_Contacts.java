package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2018/3/2.
 */

public class Request_Contacts {
    private List<LocalContacts> users;
    private String user_id;

    public Request_Contacts(List<LocalContacts> users, String user_id) {
        this.users = users;
        this.user_id = user_id;
    }

    public List<LocalContacts> getUsers() {
        return users;
    }

    public void setUsers(List<LocalContacts> users) {
        this.users = users;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
