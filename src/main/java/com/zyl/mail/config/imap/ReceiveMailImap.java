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
    private IMAPStore store;
    private IMAPFolder folder;
    private Session session;
    private IdleManager idleManager;

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
            folder.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent e) {
                    IMAPMessage[] messages = (IMAPMessage[]) e.getMessages();
                    for ( IMAPMessage message : messages ) {

                    }
                }

                @Override
                public void messagesRemoved(MessageCountEvent e) {
                    super.messagesRemoved(e);
                }
            });
        } catch ( MessagingException e ) {
            e.printStackTrace();
        }
    }
}