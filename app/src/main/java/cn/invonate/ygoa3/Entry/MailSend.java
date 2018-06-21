package cn.invonate.ygoa3.Entry;

import java.io.File;
import java.util.List;

public class MailSend {
    private String account;
    private List<MailAddress> address;
    private List<MailAddress> cc;
    private String subject;
    private String ref;
    private int[] files;
    private String folder;
    private File[] attachment;
    private String isReply;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<MailAddress> getAddress() {
        return address;
    }

    public void setAddress(List<MailAddress> address) {
        this.address = address;
    }

    public List<MailAddress> getCc() {
        return cc;
    }

    public void setCc(List<MailAddress> cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int[] getFiles() {
        return files;
    }

    public void setFiles(int[] files) {
        this.files = files;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public File[] getAttachment() {
        return attachment;
    }

    public void setAttachment(File[] attachment) {
        this.attachment = attachment;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
}
