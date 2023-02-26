package cn.czyx007.reggie.utils;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 15:43
 */
@Component
public class SendEmailUtils {
    private static String userName;
    private static String password;

    @Value("${email.userName}")
    public void setUserName(String userName) {
        SendEmailUtils.userName = userName;
    }

    @Value("${email.password}")
    public void setPassword(String password) {
        SendEmailUtils.password = password;
    }


    /**
     * 发送验证码
     * @param email  接收邮箱
     * @param code   验证码
     */
    public static void sendAuthCodeEmail(String email,String code) {
        try {
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName("smtp.qq.com");
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码 username为你开启发送验证码功能的邮箱号 password为你在qq邮箱获取到的一串字符串*/
            mail.setAuthentication(userName, password);
            /*发送邮件的邮箱和发件人*/
            mail.setFrom(userName, "瑞吉外卖");
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo(email);
            /*设置邮件的主题*/
            mail.setSubject("登录验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户:你好! 登录验证码为:" + code + "(有效期为一分钟)");
            mail.send();//发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
