package cn.invonate.ygoa3.Util


import cn.invonate.ygoa3.Entry.Mail
import cn.invonate.ygoa3.Entry.MailNew
import java.util.*
import javax.mail.Folder
import javax.mail.internet.MimeMessage

/**
 * Created by liyangyang on 2017/10/26.
 */

object MailHolder {
    var mails: List<MimeMessage>? = null
    var mail_model: List<Mail>? = null

    var folder: Folder? = null

    var mailsBeans: ArrayList<MailNew.ResultBean.MailsBean>? = null

}
