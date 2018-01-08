package cn.invonate.ygoa.Chat.utils;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * Created by liyangyang on 2017/12/20.
 */

public class TextModule implements ExtensionElement {
    private String user_id;
    private String head_img;
    private String content;

    public TextModule(String user_id, String head_img, String content) {
        this.user_id = user_id;
        this.head_img = head_img;
        this.content = content;
    }

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public String getElementName() {
        return null;
    }

    @Override
    public CharSequence toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<body>");
        sb.append("<mm ");
        sb.append("type=\"text\"");
        sb.append(">");

        sb.append("<content>");
        sb.append(content);
        sb.append("</content>");
        sb.append("</mm>");
        sb.append("</body>");

//        sb.append("<userInfo>");
//
//        sb.append("<username>");
//        sb.append(user_id);
//        sb.append("</username>");
//
//        sb.append("<headImage>");
//        sb.append(head_img);
//        sb.append("</headImage>");
//
//        sb.append("</userInfo>");

        return sb;
    }
}
