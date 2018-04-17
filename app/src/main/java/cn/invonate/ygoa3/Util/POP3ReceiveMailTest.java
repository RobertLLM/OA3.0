package cn.invonate.ygoa3.Util;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import cn.invonate.ygoa3.Entry.Mail;

/**
 * Created by liyangyang on 2017/10/20.
 */

public class POP3ReceiveMailTest {

    private String charset;

    /**
     * 解析邮件
     *
     * @param messages 要解析的邮件列表
     */
    public static ArrayList<Mail> parseMessage(List<MimeMessage> messages) throws Exception {
        ArrayList<Mail> list_mail = new ArrayList<>();
        // 解析所有邮件
        for (int i = 0, count = messages.size(); i < count; i++) {
            MimeMessage msg = messages.get(i);
            Mail mail = new Mail();
            System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
            mail.setSubject(getSubject(msg));
            System.out.println("主题: " + mail.getSubject());

            String[] from = getFrom(msg);
            mail.setFrom(from[1]);
            mail.setPersonal(from[0]);
            System.out.println("发件人: " + mail.getPersonal() + mail.getFrom());

            mail.setReceiver(getReceiveAddress(msg, Message.RecipientType.TO));
            System.out.println("收件人：" + mail.getReceiver());

            mail.setCopy(getReceiveAddress(msg, Message.RecipientType.CC));
            System.out.println("抄送人：" + mail.getReceiver());

            mail.setSend_date(getSentDate(msg));
            System.out.println("发送时间：" + mail.getSend_date());

            mail.setSeen(isSeen(msg));
            System.out.println("是否已读：" + mail.isSeen());

            mail.setSize(msg.getSize());
            System.out.println("邮件大小：" + mail.getSize() * 1024 + "kb");

            mail.setContainerAttachment(isContainAttachment(msg));
            boolean isContainerAttachment = mail.isContainerAttachment();
            System.out.println("是否包含附件：" + isContainerAttachment);


            //System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
            System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
            System.out.println();

            list_mail.add(mail);
        }
        return list_mail;
    }

    /**
     * 解析邮件内容和附件
     *
     * @param msg
     * @param mail
     * @throws Exception
     */
    public static void setContent(Message msg, Mail mail) throws Exception {
        StringBuffer content = new StringBuffer(30);
        ArrayList<String> attachments = new ArrayList<>();
        ArrayList<byte[]> attachmentsInputStreams = new ArrayList<>();
        ArrayList<Integer> size = new ArrayList<>();
        compileMailContent(msg, mail, content, attachments, attachmentsInputStreams, size);
        String str = content.toString().replaceAll("\r\n", "<br>")
                .replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        mail.setContent(str);
        mail.setAttachments(attachments);
        mail.setAttachmentsInputStreams(attachmentsInputStreams);
        mail.setFile_size(size);
    }

    /**
     * 获得邮件主题
     *
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        if (msg.getSubject() == null) {
            return "无主题";
        } else {
            return MimeUtility.decodeText(msg.getSubject());
        }
    }

    /**
     * 获得邮件发件人
     *
     * @param msg 邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String[] getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String[] from = new String[2];
        Address[] froms = msg.getFrom();
        for (int i = 0; i < froms.length; i++) {
            Log.i("address" + i, JSON.toJSONString(froms[i]));
        }
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");
        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from[0] = person;
        from[1] = address.getAddress();
        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     *
     * @param msg  邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static ArrayList<String> getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        ArrayList<String> receiveAddresses = new ArrayList<>();
        Address[] addresss = null;
        addresss = msg.getRecipients(type);

        if (addresss == null || addresss.length < 1) {

        } else {
            for (Address address : addresss) {
                InternetAddress internetAddress = (InternetAddress) address;
                receiveAddresses.add(internetAddress.toUnicodeString());
            }
        }
        return receiveAddresses;
    }

    /**
     * 获得邮件发送时间
     *
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     */
    public static String getSentDate(MimeMessage msg) throws MessagingException {
        StringBuffer pattern = new StringBuffer();
        Date today = new Date();
        Date receivedDate = msg.getSentDate();
        if (today.getYear() != receivedDate.getYear()) {
            pattern.append("yyyy-");
        }
        if (today.getYear() == receivedDate.getYear() && today.getMonth() == receivedDate.getMonth() && today.getDate() - 1 == receivedDate.getDate()) {
            pattern.append("昨天");
        }

        if (today.getYear() == receivedDate.getYear() && today.getDate() - 1 != receivedDate.getDate() && today.getDate() != receivedDate.getDate()) {
            pattern.append("MM-dd ");
        }
        pattern.append("HH:mm");
        return new SimpleDateFormat(pattern.toString()).format(receivedDate);
    }

    /**
     * 判断邮件中是否包含附件
     *
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }
                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     *
     * @param msg 邮件内容
     * @return 如果邮件已读返回true, 否则返回false
     * @throws MessagingException
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     *
     * @param msg 邮件内容
     * @return 需要回执返回true, 否则返回false
     * @throws MessagingException
     */
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }

    /**
     * 获得邮件的优先级
     *
     * @param msg 邮件内容
     * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
     * @throws MessagingException
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "紧急";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "低";
            else
                priority = "普通";
        }
        return priority;
    }

    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 保存附件
     *
     * @param part    邮件中多个组合体中的其中一个组合体
     * @param destDir 附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     *
     * @param is       输入流
     * @param fileName 文件名
     * @param destDir  文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    /**
     * 解析字符集编码
     *
     * @param contentType
     * @return
     */
    private String parseCharset(String contentType) {
        if (!contentType.contains("charset")) {
            return null;
        }
        if (contentType.contains("gbk")) {
            return "GBK";
        } else if (contentType.contains("GB2312") || contentType.contains("gb18030")) {
            return "gb2312";
        } else {
            String sub = contentType.substring(contentType.indexOf("charset") + 8).replace("\"", "");
            if (sub.contains(";")) {
                return sub.substring(0, sub.indexOf(";"));
            } else {
                return sub;
            }
        }
    }

    /**
     * 解析流格式
     *
     * @param is
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private static String parseInputStream(InputStream is) throws IOException, MessagingException {
        StringBuffer str = new StringBuffer();
        byte[] readByte = new byte[1024];
        int count;
        try {
            while ((count = is.read(readByte)) != -1) {
                str.append(new String(readByte, 0, count, "GBK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * 解析邮件内容
     *
     * @param part
     * @throws Exception
     */
    private static void compileMailContent(Part part, Mail mail, StringBuffer mailContent, ArrayList<String> attachments, ArrayList<byte[]> attachmentsInputStreams, ArrayList<Integer> size) throws Exception {
        String contentType = part.getContentType();
        boolean connName = false;
        if (contentType.indexOf("name") != -1) {
            connName = true;
        }
        if (part.isMimeType("text/plain") && !connName) {
            String content = part.getContent().toString();
            mailContent.append(content);
        } else if (part.isMimeType("text/html") && !connName) {
            mail.setHtml(true);
            String content = part.getContent().toString();
            mailContent.append(content);
        } else if (part.isMimeType("multipart/*") || part.isMimeType("message/rfc822")) {
            if (part.getContent() instanceof Multipart) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i), mail, mailContent, attachments, attachmentsInputStreams, size);
                }
            } else {
                Multipart multipart = new MimeMultipart(new ByteArrayDataSource(part.getInputStream(), "multipart/*"));
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i), mail, mailContent, attachments, attachmentsInputStreams, size);
                }
            }
        } else if (part.getDisposition() != null && part.getDisposition().equals(Part.ATTACHMENT)) {
            // 获取附件
            String filename = part.getFileName();
            if (filename != null) {
                if (filename.indexOf("=?gb18030?") != -1) {
                    filename = filename.replace("gb18030", "utf-8");
                }
                filename = MimeUtility.decodeText(filename);
                attachments.add(filename);
                attachmentsInputStreams.add(toByteArray(part.getInputStream()));
                size.add(part.getSize());
            }
            Log.e("content", "附件：" + filename);
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
