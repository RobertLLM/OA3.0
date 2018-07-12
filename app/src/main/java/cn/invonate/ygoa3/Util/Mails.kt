package cn.invonate.ygoa3.Util

import android.util.Log
import cn.invonate.ygoa3.Entry.FileEntry
import cn.invonate.ygoa3.YGApplication
import com.alibaba.fastjson.JSON
import java.io.File
import java.util.*
import javax.activation.CommandMap
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.activation.MailcapCommandMap
import javax.mail.*
import javax.mail.internet.*
import javax.mail.util.ByteArrayDataSource

class Mails(private val app: YGApplication) : javax.mail.Authenticator() {
    private val _user: String
    private val _pass: String
    private var _to: List<String>?=null
    private var _cc: List<String>?=null
    private var _bcc: List<String>?=null
    private var _from: String?=null
    private var _personal: String?=null
    private val _port: String
    private val _sport: String
    private val _host: String
    private var _subject: String?=null
    private var _body: String?=null
    private val _auth: Boolean
    private val _debuggable: Boolean
    private val _multipart: Multipart

    init {
        _host = Domain.MAIL_URL // default smtp server
        _port = "25" // default smtp port
        _sport = "25" // default socketfactory port
        _user = app.user.user_code // username
        _pass = app.user.mailPassword // password
        _debuggable = true // debug mode on or off - default off
        _auth = true // smtp authentication - default on
        _multipart = MimeMultipart() // There is something wrong with
    }

    constructor(app: YGApplication, from: String, personal: String, to: List<String>, cc: List<String>,
                bcc: List<String>, subject: String, body: String) : this(app) {
        _from = from
        _personal = personal
        _to = to
        _cc = cc
        _bcc = bcc
        _subject = subject
        _body = body
        val mc = CommandMap
                .getDefaultCommandMap() as MailcapCommandMap
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed")
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822")

        CommandMap.setDefaultCommandMap(mc)
        Log.i("MailUtil", JSON.toJSONString(this))
    }

    /**
     * 发送邮件
     *
     * @param is_draft
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun send(is_draft: Boolean): Boolean {
        val props = _setProperties()
        if (_user != "" && _pass != "" && _to!!.size > 0
                && _from != "") {
            val session = Session.getInstance(props, this)
            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(_from, _personal))
            if (_to != null) {
                for (item in _to!!) {
                    val address = InternetAddress()
                    val name = item.substring(0, item.indexOf('<'))
                    val addr = item.substring(item.indexOf('<') + 1, item.indexOf('>'))
                    address.address = addr
                    address.personal = name
                    msg.addRecipient(Message.RecipientType.TO,
                            address)
                }
            }
            if (_cc != null) {
                for (item in _cc!!) {
                    val address = InternetAddress()
                    val name = item.substring(0, item.indexOf('<'))
                    val addr = item.substring(item.indexOf('<') + 1, item.indexOf('>'))
                    address.address = addr
                    address.personal = name
                    msg.addRecipient(Message.RecipientType.CC,
                            address)
                }
            }
            if (_bcc != null) {
                for (item in _bcc!!) {
                    val address = InternetAddress()
                    val name = item.substring(0, item.indexOf('<'))
                    val addr = item.substring(item.indexOf('<') + 1, item.indexOf('>'))
                    address.address = addr
                    address.personal = name
                    msg.addRecipient(Message.RecipientType.BCC,
                            address)
                }
            }
            msg.subject = _subject
            msg.sentDate = Date() // setup message body
            val messageBodyPart = MimeBodyPart()
            messageBodyPart.setText(_body)
            _multipart.addBodyPart(messageBodyPart) // Put parts in message
            msg.setContent(_multipart) // send email
            if (is_draft) {
                save_to_draft(msg)
                msg.setFlag(Flags.Flag.DRAFT, true)
                return true
            }
            Transport.send(msg)
            save_to_sent(msg)
            return true
        } else {
            return false
        }
    }

    /**
     * 添加附件
     *
     * @param file
     * @throws Exception
     */
    @Throws(Exception::class)
    fun addAttachment(file: File) {
        val messageBodyPart = MimeBodyPart()
        val source = FileDataSource(file)
        messageBodyPart.dataHandler = DataHandler(source)
        messageBodyPart.fileName = file.name
        _multipart.addBodyPart(messageBodyPart)
    }

    /**
     * 添加附件
     *
     * @param file
     * @throws Exception
     */
    @Throws(Exception::class)
    fun addAttachment(file: FileEntry) {
        Log.i("FileEntry", file.type)
        val messageBodyPart = MimeBodyPart()
        val source = ByteArrayDataSource(file.getIs(), "application/" + file.type)
        messageBodyPart.dataHandler = DataHandler(source)
        messageBodyPart.fileName = MimeUtility.encodeText(file.name)
        _multipart.addBodyPart(messageBodyPart)
    }

    public override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(_user, _pass)
    }

    private fun _setProperties(): Properties {
        val props = Properties()
        props["mail.smtp.host"] = _host
        if (_debuggable) {
            props["mail.debug"] = "true"
        }
        if (_auth) {
            props["mail.smtp.auth"] = "true"
        }
        props["mail.smtp.port"] = _port
        // 没有SSL握手协议可不填
        //props.put("mail.smtp.socketFactory.port", _sport);
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");
        return props
    } // the getters and setters


    @Throws(MessagingException::class)
    private fun save_to_sent(msg: Message) {
        // 准备连接服务器的会话信息
        val props = Properties()
        props.setProperty("mail.store.protocol", "imap")
        props.setProperty("mail.imap.host", Domain.MAIL_URL)
        props.setProperty("mail.imap.port", Domain.MAIL_PORT)

        // 创建Session实例对象
        val session = Session.getInstance(props)

        // 创建IMAP协议的Store对象
        val store = session.getStore("imap")
        store.connect(app.user.user_code, app.user.mailPassword)
        val folder = store.getFolder("Sent")

        folder.open(Folder.READ_WRITE) //打开收件箱

        val msgs = arrayOf(msg)

        folder.appendMessages(msgs)

        msg.setFlag(Flags.Flag.RECENT, true)

        println("邮件保存到已发送收件夹")
    }

    /**
     * 邮件添加到垃圾箱
     *
     * @param msg
     * @throws MessagingException
     */
    @Throws(MessagingException::class)
    private fun save_to_draft(msg: Message) {
        // 准备连接服务器的会话信息
        val props = Properties()
        props.setProperty("mail.store.protocol", "imap")
        props.setProperty("mail.imap.host", Domain.MAIL_URL)
        props.setProperty("mail.imap.port", Domain.MAIL_PORT)

        // 创建Session实例对象
        val session = Session.getInstance(props)

        // 创建IMAP协议的Store对象
        val store = session.getStore("imap")
        store.connect(app.user.user_code, app.user.mailPassword)
        val folder = store.getFolder("Drafts")

        folder.open(Folder.READ_WRITE) //打开草稿箱

        val msgs = arrayOf(msg)

        folder.appendMessages(msgs)

        msg.setFlag(Flags.Flag.DRAFT, true)

        folder.close(true)
    }
}
