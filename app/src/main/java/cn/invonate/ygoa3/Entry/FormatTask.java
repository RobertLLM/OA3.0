package cn.invonate.ygoa3.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2018/3/20.
 */

public class FormatTask {

    private String title;
    private List<TaskDetail.Button> buttons;

    private List<TaskGroup> tasks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TaskDetail.Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<TaskDetail.Button> buttons) {
        this.buttons = buttons;
    }

    public List<TaskGroup> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskGroup> tasks) {
        this.tasks = tasks;
    }

    public static class TaskGroup {
        private int index;
        private String type;
        private String name;
        private ArrayList<TaskDetail.Input> inputs;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<TaskDetail.Input> getInputs() {
            return inputs;
        }

        public void setInputs(ArrayList<TaskDetail.Input> inputs) {
            this.inputs = inputs;
        }
    }
}
