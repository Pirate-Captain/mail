/**
 * chsi
 * Created on 2017-04-26
 */
package com.zyl.mail.config.imap;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.IdleManager;
import com.zyl.mail.config.MailConfig;
import om.zyl.mail.util.ImapMailParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhuyl<a href="mailto:zhuyl@chsi.com.cn">zhu Youliang</a>
 * @version $Id$
 */
public class ReceiveMailImap {
    private static Logger log = LoggerFactory.getLogger(ReceiveMailImap.class);
    private IMAPStore store;
    private IMAPFolder folder;
    private Session session;
    private static IdleManager idleManager;

    public static void main(String[] args) {
        MailConfig mailConfig = new MailConfig();
        mailConfig.setUsername("devtest@chsi.com.cn");
        mailConfig.setPassword("Chsitest1234");
        mailConfig.setMailServer("imap.exmail.qq.com");
        mailConfig.setMailPort("993");
        mailConfig.setMailProtoc("imap");

        ReceiveMailImap receiveMailImap = new ReceiveMailImap();
        if ( !receiveMailImap.connectMailServer(mailConfig) ) {
            log.warn("连接邮件服务器失败！");
            return;
        }
        receiveMailImap.readMessages();
        System.out.println("helloddd");
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(60000);
                    idleManager.stop();
                    System.out.println("IdleManager stop");
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean connectMailServer(MailConfig mailConfig) {
        session = Session.getInstance(mailConfig.getProperties());
        try {
            store = (IMAPStore)session.getStore(mailConfig.getMailProtoc());
            store.connect(mailConfig.getUsername(), mailConfig.getPassword());

            ExecutorService executorService = Executors.newCachedThreadPool();
            idleManager = new IdleManager(session, executorService);
            return true;
        } catch ( NoSuchProviderException e ) {
            e.printStackTrace();
        } catch ( MessagingException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    public void readMessages() {
        try {
            folder = (IMAPFolder) store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            folder.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent e) {
                    System.out.println("有新邮件a");
                    Message[] messages =  e.getMessages();
                    for ( Message tmpMessage : messages ) {
                        System.out.println("新邮件");
                        try {
                            IMAPMessage message = (IMAPMessage)tmpMessage;
                            log.info("发件人：" + ImapMailParseUtil.getSender(message));
                            log.info("主题：" + ImapMailParseUtil.getSubject(message));
//                            log.info("邮件正文：" + ImapMailParseUtil.getMailContent(message));
                        } catch (Exception e1) {
                            log.error("邮件解析失败：" + e1);
                        }
                    }
                    try {
                        idleManager.watch(folder);
                    } catch ( MessagingException e1 ) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent e) {
                    super.messagesRemoved(e);
                }
            });
            idleManager.watch(folder);
        } catch ( MessagingException e ) {
            e.printStackTrace();
        }
    }
}