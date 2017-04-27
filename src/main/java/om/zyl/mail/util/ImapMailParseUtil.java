/**
 * chsi
 * Created on 2017-04-26
 */
package om.zyl.mail.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPMessage;

/**
 * @author zhuyl<a href="mailto:zhuyouliangcn@gmail.com">zhu Youliang</a>
 * @version $Id$
 */
public class ImapMailParseUtil {
    private static Logger log = LoggerFactory.getLogger(ImapMailParseUtil.class);

    public static String getSubject(IMAPMessage message) {
        try {
            String subject = message.getSubject();
            return MimeUtility.decodeText(subject);
        } catch ( MessagingException e ) {
            log.error("解析邮件主题失败！", e);
        } catch ( UnsupportedEncodingException e ) {
            log.error("解析邮件主题失败！", e);
        }
        return "";
    }

    public static String getSender(IMAPMessage message) {
        try {
            InternetAddress senderAddress = (InternetAddress) message.getSender();
            return MimeUtility.decodeText(senderAddress.getPersonal()) + "<" + senderAddress.getAddress() + ">";
        } catch ( MessagingException e ) {
            log.error("解析发件人失败！", e);
        } catch ( UnsupportedEncodingException e ) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMailContent(Part part) {
        try {
            if ( part.isMimeType("text/plain") ) {
                return part.getContent().toString();
            } else if ( part.isMimeType("text/html") ) {
                return part.getContent().toString();
            } else if ( part.isMimeType("multipart/*") ) {
                Multipart multipart = (Multipart)part.getContent();
                String content = "";
                for ( int i=0; i<multipart.getCount(); i++ ) {
                    content = getMailContent(multipart.getBodyPart(i));
                }
                return content;
            } else if ( part.isMimeType("message/rfc822") ) {
                return getMailContent((Part) part.getContent());
            }
        } catch ( MessagingException e ) {
            log.error("解析邮件正文失败！", e);
        } catch ( IOException e ) {
            log.error("解析邮件正文失败！", e);
        }
        return "";
    }
}