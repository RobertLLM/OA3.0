package cn.invonate.ygoa3.Entry;

import java.util.List;

public class TaskEntry {
    private String lb;
    private List<Mission.MissionBean> tasks;

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public List<Mission.MissionBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<Mission.MissionBean> tasks) {
        this.tasks = tasks;
    }
}
