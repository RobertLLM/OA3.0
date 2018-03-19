package cn.invonate.ygoa3.Util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sun.mail.imap.protocol.FLAGS;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import cn.invonate.ygoa3.Entry.FileEntry;
import cn.invonate.ygoa3.YGApplication;

public class Mails extends javax.mail.Authenticator {
    private String _user;
    private String _pass;
    private List<String> _to;
    private List<String> _cc;
    private List<String> _bcc;
    private String _from;
    private String _personal;
    private String _port;
    private String _sport;
    private String _host;
    private String _subject;
    private String _body;
    private boolean _auth;
    private boolean _debuggable;
    private Multipart _multipart;

    private YGApplication app;

    public Mails(YGApplication app) {
        this.app = app;
        _host = Domain.MAIL_URL; // default smtp server
        _port = "25"; // default smtp port
        _sport = "25"; // default socketfactory port
        _user = app.getUser().getUser_code(); // username
        _pass = app.getUser().getMailPassword(); // password
        _debuggable = true; // debug mode on or off - default off
        _auth = true; // smtp authentication - default on
        _multipart = new MimeMultipart(); // There is something wrong with
    }

    public Mails(YGApplication app, String from, String personal, List<String> to, List<String> cc,
                 List<String> bcc, String subject, String body) {
        this(app);
        _from = from;
        _personal = personal;
        _to = to;
        _cc = cc;
        _bcc = bcc;
        _subject = subject;
        _body = body;
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap
                .getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");

        CommandMap.setDefaultCommandMap(mc);
        Log.i("MailUtil", JSON.toJSONString(this));
    }

    /**
     * 发送邮件
     *
     * @param is_draft
     * @return
     * @throws Exception
     */
    public boolean send(boolean is_draft) throws Exception {
        Properties props = _setProperties();
        if (!_user.equals("") && !_pass.equals("") && _to.size() > 0
                && !_from.equals("") && !_subject.equals("")
                && !_body.equals("")) {
            Session session = Session.getInstance(props, this);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(_from, _personal));
            if (_to != null) {
                for (String item : _to) {
                    msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(item));
                }
            }
            if (_cc != null) {
                for (String item : _cc) {
                    msg.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(item));
                }
            }
            if (_bcc != null) {
                for (String item : _bcc) {
                    msg.addRecipient(Message.RecipientType.BCC,
                            new InternetAddress(item));
                }
            }

            msg.setSubject(_subject);
            msg.setSentDate(new Date()); // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(_body);
            _multipart.addBodyPart(messageBodyPart); // Put parts in message
            msg.setContent(_multipart); // send email
            if (is_draft) {
                save_to_draft(msg);
                msg.setFlag(Flags.Flag.DRAFT, true);
                return true;
            }
            Transport.send(msg);
            save_to_sent(msg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加附件
     *
     * @param file
     * @throws Exception
     */
    public void addAttachment(File file) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        _multipart.addBodyPart(messageBodyPart);
    }

    /**
     * 添加附件
     *
     * @param file
     * @throws Exception
     */
    public void addAttachment(FileEntry file) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(file.getIs(), file.getType());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        _multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_user, _pass);
    }

    private Properties _setProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", _host);
        if (_debuggable) {
            props.put("mail.debug", "true");
        }
        if (_auth) {
            props.put("mail.smtp.auth", "true");
        }
        props.put("mail.smtp.port", _port);
        // 没有SSL握手协议可不填
        //props.put("mail.smtp.socketFactory.port", _sport);
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    } // the getters and setters


    private void save_to_sent(Message msg) throws MessagingException {
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", Domain.MAIL_URL);
        props.setProperty("mail.imap.port", Domain.MAIL_PORT);

        // 创建Session实例对象
        Session session = Session.getInstance(props);

        // 创建IMAP协议的Store对象
        Store store = session.getStore("imap");
        store.connect(app.getUser().getUser_code(), app.getUser().getMailPassword());
        Folder folder = store.getFolder("Sent");

        folder.open(Folder.READ_WRITE); //打开收件箱

        Message[] msgs = {msg};

        folder.appendMessages(msgs);

        msg.setFlag(FLAGS.Flag.RECENT, true);

        System.out.println("邮件保存到已发送收件夹");
    }

    /**
     * 邮件添加到垃圾箱
     *
     * @param msg
     * @throws MessagingException
     */
    private void save_to_draft(Message msg) throws MessagingException {
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", Domain.MAIL_URL);
        props.setProperty("mail.imap.port", Domain.MAIL_PORT);

        // 创建Session实例对象
        Session session = Session.getInstance(props);

        // 创建IMAP协议的Store对象
        Store store = session.getStore("imap");
        store.connect(app.getUser().getUser_code(), app.getUser().getMailPassword());
        Folder folder = store.getFolder("Drafts");

        folder.open(Folder.READ_WRITE); //打开草稿箱

        Message[] msgs = {msg};

        folder.appendMessages(msgs);

        msg.setFlag(FLAGS.Flag.DRAFT, true);

        folder.close(true);
    }
}
