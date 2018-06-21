package cn.invonate.ygoa3.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class MessageContent {

    /**
     * code : 0000
     * message : OK
     * result : {"mail":{"attachments":[{"fileName":"IMG_0787.jpeg","index":1,"size":560}],"cc":[{"address":"035018@yong-gang.cn","userName":"李鹏"}],"id":5,"isReply":"0","read":"1","receive":[{"address":"022023@yong-gang.cn","userName":"马晓卫"}],"sender":{"address":"<033523@yong-gang.cn>","userName":"李阳洋 "},"sentDate":"2018-06-11  13:58","subject":"几日不见，甚是想念","textContent":"聊表敬意，特发此件"}}
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
        /**
         * mail : {"attachments":[{"fileName":"IMG_0787.jpeg","index":1,"size":560}],"cc":[{"address":"035018@yong-gang.cn","userName":"李鹏"}],"id":5,"isReply":"0","read":"1","receive":[{"address":"022023@yong-gang.cn","userName":"马晓卫"}],"sender":{"address":"<033523@yong-gang.cn>","userName":"李阳洋 "},"sentDate":"2018-06-11  13:58","subject":"几日不见，甚是想念","textContent":"聊表敬意，特发此件"}
         */

        private MailBean mail;

        public MailBean getMail() {
            return mail;
        }

        public void setMail(MailBean mail) {
            this.mail = mail;
        }

        public static class MailBean {
            /**
             * attachments : [{"fileName":"IMG_0787.jpeg","index":1,"size":560}]
             * cc : [{"address":"035018@yong-gang.cn","userName":"李鹏"}]
             * id : 5
             * isReply : 0
             * read : 1
             * receive : [{"address":"022023@yong-gang.cn","userName":"马晓卫"}]
             * sender : {"address":"<033523@yong-gang.cn>","userName":"李阳洋 "}
             * sentDate : 2018-06-11  13:58
             * subject : 几日不见，甚是想念
             * textContent : 聊表敬意，特发此件
             */

            private int id;
            private String isReply;
            private String read;
            private SenderBean sender;
            private String sentDate;
            private String subject;
            private String textContent;
            private List<AttachmentsBean> attachments;
            private List<CcBean> cc;
            private List<ReceiveBean> receive;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIsReply() {
                return isReply;
            }

            public void setIsReply(String isReply) {
                this.isReply = isReply;
            }

            public String getRead() {
                return read;
            }

            public void setRead(String read) {
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

            public List<AttachmentsBean> getAttachments() {
                return attachments;
            }

            public void setAttachments(List<AttachmentsBean> attachments) {
                this.attachments = attachments;
            }

            public List<CcBean> getCc() {
                return cc;
            }

            public void setCc(List<CcBean> cc) {
                this.cc = cc;
            }

            public List<ReceiveBean> getReceive() {
                return receive;
            }

            public void setReceive(List<ReceiveBean> receive) {
                this.receive = receive;
            }

            public static class SenderBean {
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

            public static class AttachmentsBean {
                /**
                 * fileName : IMG_0787.jpeg
                 * index : 1
                 * size : 560
                 */

                private String fileName;
                private int index;
                private int size;

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

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }
            }

            public static class CcBean {
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

            public static class ReceiveBean {
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
