package cn.invonate.ygoa.Util;


import java.util.List;

import javax.mail.Folder;
import javax.mail.internet.MimeMessage;

import cn.invonate.ygoa.Entry.Mail;

/**
 * Created by liyangyang on 2017/10/26.
 */

public class MailHolder {
    public static List<MimeMessage> mails;
    public static List<Mail> mail_model;

    public static Folder folder;

}
