/**
 * chsi
 * Created on 2017-04-26
 */
package com.zyl.mail.config;

import org.apache.commons.lang.StringUtils;

import java.security.Security;
import java.util.Properties;

/**
 * @author zhuyl<a href="mailto:zhuyouliangcn@gmail.com">zhu Youliang</a>
 * @version $Id$
 */
public class MailConfig {
    private String username;
    private String password;
    private String mailProtoc;
    private String mailServer;
    private String mailPort;

    public Properties getProperties(){
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = new Properties();
        if( StringUtils.equals(mailProtoc, "pop3")){
            props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.store.protocol", mailProtoc); // 协议
            props.setProperty("mail.pop3.port", mailPort); // 端口
            props.setProperty("mail.pop3.host", mailServer); // pop3服务器
            props.setProperty("mail.pop3.connectiontimeout", "30000");//默认连接pop3服务器为30s
            props.setProperty("mail.pop3.timeout", "300000");//数据读取默认为5分钟
        }else if( StringUtils.equals(mailProtoc, "imap")){
            props.setProperty("mail.store.protocol", mailProtoc); // 协议
            props.setProperty("mail.imap.port", mailPort); // 端口
            props.setProperty("mail.imap.host", mailServer); // imap服务器
            props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.imap.connectiontimeout", "30000");//默认超时连接为30s
            props.setProperty("mail.imap.timeout", "300000");//默认数据读取超时时间为5分钟
            props.put("mail.event.scope", "session");
        }else if(StringUtils.equals(mailProtoc, "smtp")){//smtp
            props.setProperty("mail.smtp.host", mailServer);
            props.setProperty("mail.smtp.port", mailPort);
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.store.propocol", mailProtoc);
        }
        return props;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailProtoc() {
        return mailProtoc;
    }

    public void setMailProtoc(String mailProtoc) {
        this.mailProtoc = mailProtoc;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailPort() {
        return mailPort;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }
}