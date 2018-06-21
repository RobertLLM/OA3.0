package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;

public class MailNew {

    /**
     * code : 0000
     * message : OK
     * result : {"mails":[{"attachments":[{"fileName":"IMG_0787.jpeg","index":1,"size":560}],"cc":[{"address":"035018@yong-gang.cn","userName":"李鹏"}],"id":7,"isReply":"0","read":"1","receive":[{"address":"022023@yong-gang.cn","userName":"马晓卫"}],"sender":{"address":"<033523@yong-gang.cn>","userName":"李阳洋 "},"sentDate":"2018-06-11  13:58","subject":"几日不见，甚是想念","textContent":"聊表敬意，特发此件"}]}
     */

    private String code;
    private String message;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private ArrayList<MailsBean> mails;

        public ArrayList<MailsBean> getMails() {
            return mails;
        }

        public void setMails(ArrayList<MailsBean> mails) {
            this.mails = mails;
        }

        public static class MailsBean implements Serializable{
            /**
             * attachments : [{"fileName":"IMG_0787.jpeg","index":1,"size":560}]
             * cc : [{"address":"035018@yong-gang.cn","userName":"李鹏"}]
             * id : 7
             * isReply : 0
             * read : 1
             * receive : [{"address":"022023@yong-gang.cn","userName":"马晓卫"}]
             * sender : {"address":"<033523@yong-gang.cn>","userName":"李阳洋 "}
             * sentDate : 2018-06-11  13:58
             * subject : 几日不见，甚是想念
             * textContent : 聊表敬意，特发此件
             */

            private int id;
            private int isReply;
            private int read;
            private SenderBean sender;
            private String sentDate;
            private String subject;
            private String textContent;
            private ArrayList<AttachmentsBean> attachments;
            private ArrayList<CcBean> cc;
            private ArrayList<ReceiveBean> receive;
            private boolean select;

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsReply() {
                return isReply;
            }

            public void setIsReply(int isReply) {
                this.isReply = isReply;
            }

            public int getRead() {
                return read;
            }

            public void setRead(int read) {
                this.read = read;
            }

            public SenderBean getSender() {
                return sender;
            }

            public void setSender(SenderBean sender) {
                this.sender = sender;
            }

            public String getSentDate() {
                return sentDate;
            }

            public void setSentDate(String sentDate) {
                this.sentDate = sentDate;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getTextContent() {
                return textContent;
            }

            public void setTextContent(String textContent) {
                this.textContent = textContent;
            }

            public ArrayList<AttachmentsBean> getAttachments() {
                return attachments;
            }

            public void setAttachments(ArrayList<AttachmentsBean> attachments) {
                this.attachments = attachments;
            }

            public ArrayList<CcBean> getCc() {
                return cc;
            }

            public void setCc(ArrayList<CcBean> cc) {
                this.cc = cc;
            }

            public ArrayList<ReceiveBean> getReceive() {
                return receive;
            }

            public void setReceive(ArrayList<ReceiveBean> receive) {
                this.receive = receive;
            }

            public static class SenderBean implements Serializable{
                /**
                 * address : <033523@yong-gang.cn>
                 * userName : 李阳洋
                 */

                private String address;
                private String userName;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }
            }

            public static class AttachmentsBean implements Serializable{
                /**
                 * fileName : IMG_0787.jpeg
                 * index : 1
                 * size : 560
                 */

                private String fileName;
                private int index;
                private long size;
                private int type;//0:网络返回,1:PhotoPicker,2:FilePicker
                private String path;

                public AttachmentsBean() {

                }

                public AttachmentsBean(String fileName, long size, int type, String path) {
                    this.fileName = fileName;
                    this.size = size;
                    this.type = type;
                    this.path = path;
                }

                public String getFileName() {
                    return fileName;
                }

                public void setFileName(String fileName) {
                    this.fileName = fileName;
                }

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public long getSize() {
                    return size;
                }

                public void setSize(long size) {
                    this.size = size;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }
            }

            public static class CcBean implements Serializable{
                /**
                 * address : 035018@yong-gang.cn
                 * userName : 李鹏
                 */

                private String address;
                private String userName;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }
            }

            public static class ReceiveBean implements Serializable{
                /**
                 * address : 022023@yong-gang.cn
                 * userName : 马晓卫
                 */

                private String address;
                private String userName;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
