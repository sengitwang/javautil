package com.senit.javautil.util;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sen on 2016/8/9.
 * 邮件发送
 */
public class MailUtil {

    private final static String DEFAULT_CHARSET = "UTF-8" ;
    private static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    /***
     * 发送邮件
     *
     * @param title   标题
     * @param context 内容
     * @param fromTo  收件人email
     */
    public static boolean sendMail(String title, String context, List<String> fromTo) {
        return sendMail(title, context, fromTo, null, null);
    }

    /***
     * 发送邮件
     *
     * @param title   标题
     * @param context 内容
     * @param fromTo  收件人email
     * @param ccAddress 抄送
     */
    public static boolean sendMail(String title, String context, List<String> fromTo,List<String> ccAddress) {
        return sendMail(title, context, fromTo, ccAddress, null);
    }

    /***
     * 发送邮件
     *
     * @param title          标题
     * @param context        内容
     * @param fromTo         收件人email
     * @param ccAddress 抄送
     * @param attachmentPath 文件路径
     */
    public static boolean sendMail(String title, String context, List<String> fromTo, List<String> ccAddress, String[] attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            mailSender.setHost( "此处配置邮箱服务器" );  		//邮箱服务器
            mailSender.setUsername( "用户名" );  //用户名称
            mailSender.setPassword( "密码" );	 //用户密码
            mailSender.setDefaultEncoding(DEFAULT_CHARSET);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, DEFAULT_CHARSET);
            String[] ft = new String[fromTo.size()];
            helper.setTo(fromTo.toArray(ft));        //收件人
            if (ccAddress != null && !ccAddress.isEmpty() && ccAddress.size() > 0) {
                String[] cc = new String[ccAddress.size()];
                helper.setCc(ccAddress.toArray(cc));    //抄送人列表
            }
            helper.setFrom("发送人" );  //发送人
            helper.setSubject(title);       //标题
            helper.setText(context, true);  //内容
            if (attachmentPath != null) {
                for (int i = 0; i < attachmentPath.length; i++) {
                    File file = new File(attachmentPath[i]);    //附件路径
                    helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);    //添加附件
                }
            }
            //发送邮件
            mailSender.send(message);
            //Log.info("发送邮件成功: 收件人[" + fromTo + "] 附件数[" + attachmentPath != null ? attachmentPath.length : 0 + "]");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        List<String> listf = new ArrayList<String>();
        listf.add("1234567@qq.com");
        List<String> listc = new ArrayList<String>();
        listc.add("1234567@qq.com");
        String[] str = new String[1];
        str[0] = "E:\\test1.xls";
        if (sendMail("测试邮件", "测试邮件", listf,listc ,str)) {
            //logger.info("邮件发送成功！");
        } else {
          //  logger.info("邮件发送失败！");
        }
    }
}
