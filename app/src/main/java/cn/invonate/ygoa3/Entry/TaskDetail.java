package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liyangyang on 2018/1/18.
 */

public class TaskDetail {
    private String title;
    private int success;
    private ArrayList<Input> inputs;
    private List<Button> buttons;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<Input> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Input> inputs) {
        this.inputs = inputs;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static class Input implements Serializable {
        private String name;
        private String value;//
        private String label;//
        private String type; //
        private boolean required;
        private String readonly;
        private String areaCode;
        private List<Option> options;
        private String textFormat;
        private int index;
        private String f_type;
        private String f_name;
        private List<String> color;
        private String size;
        private String url;

        public Input() {

        }

        public Input(String name, String value, String label, String type, boolean required) {
            this.name = name;
            this.value = value;
            this.label = label;
            this.type = type;
            this.required = required;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public String isReadonly() {
            return readonly;
        }

        public void setReadonly(String readonly) {
            this.readonly = readonly;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

        public String getTextFormat() {
            return textFormat;
        }

        public void setTextFormat(String textFormat) {
            this.textFormat = textFormat;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }

        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Button {
        private String name;
        private String label;
        private String url;
        private String value;

        public Button() {

        }

        public Button(String name, String label, String url, String value) {
            this.name = name;
            this.label = label;
            this.url = url;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Option {
        private boolean selected;
        private String value;
        private String label;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class Accordion {
        private String header;
        private Map<String, String> content;
        private List<MapEntry> maps;

        private String deltext;
        private String delurl;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public Map<String, String> getContent() {
            return content;
        }

        public void setContent(Map<String, String> content) {
            this.content = content;
        }

        public List<MapEntry> getMaps() {
            return maps;
        }

        public void setMaps(List<MapEntry> maps) {
            this.maps = maps;
        }

        public void initMaps() {
            maps = new ArrayList<>();
            for (String key : content.keySet()) {
                maps.add(new MapEntry(key, content.get(key)));
            }
        }

        public String getDeltext() {
            return deltext;
        }

        public void setDeltext(String deltext) {
            this.deltext = deltext;
        }

        public String getDelurl() {
            return delurl;
        }

        public void setDelurl(String delurl) {
            this.delurl = delurl;
        }

        public class MapEntry {
            private String key;
            private String value;

            public MapEntry(String key, String value) {
                this.key = key;
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

    }


}
