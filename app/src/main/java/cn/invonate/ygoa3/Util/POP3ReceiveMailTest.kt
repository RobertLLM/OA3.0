package cn.invonate.ygoa3.Util

import android.util.Log
import cn.invonate.ygoa3.Entry.Mail
import com.alibaba.fastjson.JSON
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeUtility
import javax.mail.util.ByteArrayDataSource

/**
 * Created by liyangyang on 2017/10/20.
 */

class POP3ReceiveMailTest {

    private val charset: String? = null

    /**
     * 解析字符集编码
     *
     * @param contentType
     * @return
     */
    private fun parseCharset(contentType: String): String? {
        if (!contentType.contains("charset")) {
            return null
        }
        if (contentType.contains("gbk")) {
            return "GBK"
        } else if (contentType.contains("GB2312") || contentType.contains("gb18030")) {
            return "gb2312"
        } else {
            val sub = contentType.substring(contentType.indexOf("charset") + 8).replace("\"", "")
            return if (sub.contains(";")) {
                sub.substring(0, sub.indexOf(";"))
            } else {
                sub
            }
        }
    }

    companion object {

        /**
         * 解析邮件
         *
         * @param messages 要解析的邮件列表
         */
        @Throws(Exception::class)
        fun parseMessage(messages: List<MimeMessage>): ArrayList<Mail> {
            val list_mail = ArrayList<Mail>()
            // 解析所有邮件
            var i = 0
            val count = messages.size
            while (i < count) {
                val msg = messages[i]
                val mail = Mail()
                println("------------------解析第" + msg.messageNumber + "封邮件-------------------- ")
                mail.subject = getSubject(msg)
                println("主题: " + mail.subject)

                val from = getFrom(msg)
                mail.from = from[1]
                mail.personal = from[0]
                println("发件人: " + mail.personal + mail.from)

                mail.receiver = getReceiveAddress(msg, Message.RecipientType.TO)
                println("收件人：" + mail.receiver)

                mail.copy = getReceiveAddress(msg, Message.RecipientType.CC)
                println("抄送人：" + mail.receiver)

                mail.send_date = getSentDate(msg)
                println("发送时间：" + mail.send_date)

                mail.isSeen = isSeen(msg)
                println("是否已读：" + mail.isSeen)

                mail.size = msg.size
                println("邮件大小：" + mail.size * 1024 + "kb")

                mail.isContainerAttachment = isContainAttachment(msg)
                val isContainerAttachment = mail.isContainerAttachment
                println("是否包含附件：$isContainerAttachment")


                //System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
                println("------------------第" + msg.messageNumber + "封邮件解析结束-------------------- ")
                println()

                list_mail.add(mail)
                i++
            }
            return list_mail
        }

        /**
         * 解析邮件内容和附件
         *
         * @param msg
         * @param mail
         * @throws Exception
         */
        @Throws(Exception::class)
        fun setContent(msg: Message, mail: Mail) {
            val content = StringBuffer(30)
            val attachments = ArrayList<String>()
            val attachmentsInputStreams = ArrayList<ByteArray>()
            val size = ArrayList<Int>()
            compileMailContent(msg, mail, content, attachments, attachmentsInputStreams, size)
            val str = content.toString().replace("\r\n".toRegex(), "<br>")
                    .replace("\t".toRegex(), "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
            mail.content = str
            mail.attachments = attachments
            mail.attachmentsInputStreams = attachmentsInputStreams
            mail.file_size = size
        }

        /**
         * 获得邮件主题
         *
         * @param msg 邮件内容
         * @return 解码后的邮件主题
         */
        @Throws(UnsupportedEncodingException::class, MessagingException::class)
        fun getSubject(msg: MimeMessage): String {
            return if (msg.subject == null) {
                "无主题"
            } else {
                MimeUtility.decodeText(msg.subject)
            }
        }

        /**
         * 获得邮件发件人
         *
         * @param msg 邮件内容
         * @return 姓名 <Email地址>
         * @throws MessagingException
         * @throws UnsupportedEncodingException
        </Email地址> */
        @Throws(MessagingException::class, UnsupportedEncodingException::class)
        fun getFrom(msg: MimeMessage): Array<String?> {
            val from = arrayOfNulls<String>(2)
            val froms = msg.from
            for (i in froms.indices) {
                Log.i("address$i", JSON.toJSONString(froms[i]))
            }
            if (froms.size < 1)
                throw MessagingException("没有发件人!")
            val address = froms[0] as InternetAddress
            var person: String? = address.personal
            if (person != null) {
                person = MimeUtility.decodeText(person) + " "
            } else {
                person = ""
            }
            from[0] = person
            from[1] = address.address
            return from
        }

        /**
         * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
         *
         * Message.RecipientType.TO  收件人
         *
         * Message.RecipientType.CC  抄送
         *
         * Message.RecipientType.BCC 密送
         *
         * @param msg  邮件内容
         * @param type 收件人类型
         * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
         * @throws MessagingException
        </邮件地址2></邮件地址1> */
        @Throws(MessagingException::class)
        fun getReceiveAddress(msg: MimeMessage, type: Message.RecipientType): ArrayList<Mail.Address> {
            val receiveAddresses = ArrayList<Mail.Address>()
            var addresss: Array<Address>? = null
            addresss = msg.getRecipients(type)
            if (addresss == null || addresss.size < 1) {

            } else {
                for (address in addresss) {
                    val ad = Mail.Address()
                    val internetAddress = address as InternetAddress
                    ad.personal = internetAddress.personal
                    ad.address = internetAddress.toUnicodeString()
                    receiveAddresses.add(ad)
                }
            }
            return receiveAddresses
        }

        /**
         * 获得邮件发送时间
         *
         * @param msg 邮件内容
         * @return yyyy年mm月dd日 星期X HH:mm
         * @throws MessagingException
         */
        @Throws(MessagingException::class)
        fun getSentDate(msg: MimeMessage): String {
            val pattern = StringBuffer()
            val today = Date()
            val receivedDate = msg.sentDate
            if (today.year != receivedDate.year) {
                pattern.append("yyyy-")
            }
            if (today.year == receivedDate.year && today.month == receivedDate.month && today.date - 1 == receivedDate.date) {
                pattern.append("昨天")
            }

            if (today.year == receivedDate.year && today.date - 1 != receivedDate.date && today.date != receivedDate.date) {
                pattern.append("MM-dd ")
            }
            pattern.append("HH:mm")
            return SimpleDateFormat(pattern.toString()).format(receivedDate)
        }

        /**
         * 判断邮件中是否包含附件
         *
         * @return 邮件中存在附件返回true，不存在返回false
         * @throws MessagingException
         * @throws IOException
         */
        @Throws(MessagingException::class, IOException::class)
        fun isContainAttachment(part: Part): Boolean {
            var flag = false
            if (part.isMimeType("multipart/*")) {
                val multipart = part.content as MimeMultipart
                val partCount = multipart.count
                for (i in 0 until partCount) {
                    val bodyPart = multipart.getBodyPart(i)
                    val disp = bodyPart.disposition
                    if (disp != null && (disp.equals(Part.ATTACHMENT, ignoreCase = true) || disp.equals(Part.INLINE, ignoreCase = true))) {
                        flag = true
                    } else if (bodyPart.isMimeType("multipart/*")) {
                        flag = isContainAttachment(bodyPart)
                    } else {
                        val contentType = bodyPart.contentType
                        if (contentType.indexOf("application") != -1) {
                            flag = true
                        }
                        if (contentType.indexOf("name") != -1) {
                            flag = true
                        }
                    }

                    if (flag) break
                }
            } else if (part.isMimeType("message/rfc822")) {
                flag = isContainAttachment(part.content as Part)
            }
            return flag
        }

        /**
         * 判断邮件是否已读
         *
         * @param msg 邮件内容
         * @return 如果邮件已读返回true, 否则返回false
         * @throws MessagingException
         */
        @Throws(MessagingException::class)
        fun isSeen(msg: MimeMessage): Boolean {
            return msg.flags.contains(Flags.Flag.SEEN)
        }

        /**
         * 判断邮件是否需要阅读回执
         *
         * @param msg 邮件内容
         * @return 需要回执返回true, 否则返回false
         * @throws MessagingException
         */
        @Throws(MessagingException::class)
        fun isReplySign(msg: MimeMessage): Boolean {
            var replySign = false
            val headers = msg.getHeader("Disposition-Notification-To")
            if (headers != null)
                replySign = true
            return replySign
        }

        /**
         * 获得邮件的优先级
         *
         * @param msg 邮件内容
         * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
         * @throws MessagingException
         */
        @Throws(MessagingException::class)
        fun getPriority(msg: MimeMessage): String {
            var priority = "普通"
            val headers = msg.getHeader("X-Priority")
            if (headers != null) {
                val headerPriority = headers[0]
                if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                    priority = "紧急"
                else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                    priority = "低"
                else
                    priority = "普通"
            }
            return priority
        }

        /**
         * 获得邮件文本内容
         *
         * @param part    邮件体
         * @param content 存储邮件文本内容的字符串
         * @throws MessagingException
         * @throws IOException
         */
        @Throws(MessagingException::class, IOException::class)
        fun getMailTextContent(part: Part, content: StringBuffer) {
            //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
            val isContainTextAttach = part.contentType.indexOf("name") > 0
            if (part.isMimeType("text/*") && !isContainTextAttach) {
                content.append(part.content.toString())
            } else if (part.isMimeType("message/rfc822")) {
                getMailTextContent(part.content as Part, content)
            } else if (part.isMimeType("multipart/*")) {
                val multipart = part.content as Multipart
                val partCount = multipart.count
                for (i in 0 until partCount) {
                    val bodyPart = multipart.getBodyPart(i)
                    getMailTextContent(bodyPart, content)
                }
            }
        }

        /**
         * 文本解码
         *
         * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
         * @return 解码后的文本
         * @throws UnsupportedEncodingException
         */
        @Throws(UnsupportedEncodingException::class)
        fun decodeText(encodeText: String?): String {
            return if (encodeText == null || "" == encodeText) {
                ""
            } else {
                MimeUtility.decodeText(encodeText)
            }
        }

        /**
         * 解析邮件内容
         *
         * @param part
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun compileMailContent(part: Part, mail: Mail, mailContent: StringBuffer, attachments: ArrayList<String>, attachmentsInputStreams: ArrayList<ByteArray>, size: ArrayList<Int>) {
            val contentType = part.contentType
            var connName = false
            if (contentType.indexOf("name") != -1) {
                connName = true
            }
            if (part.isMimeType("text/plain") && !connName) {
                val content = part.content.toString()
                mailContent.append(content)
            } else if (part.isMimeType("text/html") && !connName) {
                mail.isHtml = true
                val content = part.content.toString()
                mailContent.append(content)
            } else if (part.isMimeType("multipart/*") || part.isMimeType("message/rfc822")) {
                if (part.content is Multipart) {
                    val multipart = part.content as Multipart
                    val counts = multipart.count
                    for (i in 0 until counts) {
                        compileMailContent(multipart.getBodyPart(i), mail, mailContent, attachments, attachmentsInputStreams, size)
                    }
                } else {
                    val multipart = MimeMultipart(ByteArrayDataSource(part.inputStream, "multipart/*"))
                    val counts = multipart.count
                    for (i in 0 until counts) {
                        compileMailContent(multipart.getBodyPart(i), mail, mailContent, attachments, attachmentsInputStreams, size)
                    }
                }
            } else if (part.disposition != null && part.disposition == Part.ATTACHMENT) {
                // 获取附件
                var filename: String? = part.fileName
                if (filename != null) {
                    if (filename.indexOf("=?gb18030?") != -1) {
                        filename = filename.replace("gb18030", "utf-8")
                    }
                    filename = MimeUtility.decodeText(filename)
                    attachments.add(filename)
                    attachmentsInputStreams.add(toByteArray(part.inputStream))
                    size.add(part.size)
                }
                Log.e("content", "附件：" + filename!!)
            }
        }

        @Throws(IOException::class)
        fun toByteArray(input: InputStream): ByteArray {
            val output = ByteArrayOutputStream()
            val buffer = ByteArray(4096)
            var n = 0
            while (true) {
                n = input.read(buffer)
                if (n != -1) {
                    output.write(buffer, 0, n)
                } else {
                    break
                }
            }
            return output.toByteArray()
        }
    }
}
