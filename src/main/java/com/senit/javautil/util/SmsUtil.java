package com.senit.javautil.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sen on 2016/8/15.
 * 短信操作工具类
 */
public class SmsUtil {
  //  private static final Logger LOG = LoggerFactory.getLogger(SmsUtil.class);

    /**
     * webservice服务器定义
     */
    private String serviceURL;
    /**
     * 序列号
     */
    private String sn = "";
    /**
     * 密码
     */
    private String password = "";
    /**
     * 加密后密码
     */
    private String pwd = "";

    /**
     * 构造函数，初始化信息
     *
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public SmsUtil() throws UnsupportedEncodingException, Exception {
        Properties prop = getProperties("短信配置");

        if (prop != null) {
            this.serviceURL = prop.getProperty("sms.serviceURL");
            this.sn = prop.getProperty("sms.sn");
            this.password = prop.getProperty("sms.password");
            // 密码为md5(sn+password)
            this.pwd = this.getMD5(sn + password);
        }
    }

    /**
     * 字符串MD5加密
     *
     * @param sourceStr
     *            待转换字符串
     * @return 加密之后字符串
     * @throws UnsupportedEncodingException
     */
    public String getMD5(String sourceStr) throws UnsupportedEncodingException {
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            // resultStr = new String(md5.digest());
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 获取信息sn,pwd(软件序列号，加密密码md5(sn+password))
     *
     * @return
     */
    public String mdgetSninfo() {
        String result = "";

        URL url;
        try {
            url = new URL(serviceURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write("报文".getBytes());//报文
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconn.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconn.setRequestProperty("SOAPAction", "action");//请求action
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);

            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr = new InputStreamReader(
                    httpconn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (null != (inputLine = in.readLine())) {
                Pattern pattern = Pattern
                        .compile("<mdgetSninfoResult>(.*)</mdgetSninfoResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    result = matcher.group(1);
                }
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 发送个性短信
     *
     * @param mobile
     *            手机号
     * @param content
     *            内容
     * @param ext
     *            扩展码
     * @param stime
     *            定时时间
     * @param rrid
     *            唯一标识
     * @param msgfmt
     *            内容编码
     * @return 唯一标识，如果不填写rrid将返回系统生成的
     */
    public String mdgxsend(String mobile, String content, String ext,
                           String stime, String rrid, String msgfmt) {
        String result = "";
        URL url;
        try {
            url = new URL(serviceURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write("报文".getBytes());//请求报文
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconn.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconn.setRequestProperty("SOAPAction", "请求action");//请求action
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);

            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr = new InputStreamReader(
                    httpconn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (null != (inputLine = in.readLine())) {
                Pattern pattern = Pattern
                        .compile("<mdgxsendResult>(.*)</mdgxsendResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    result = matcher.group(1);
                }
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 发送短信
     *
     * @param mobile
     *            手机号
     * @param content
     *            内容
     * @param ext
     *            扩展码
     * @param stime
     *            定时时间
     * @param rrid
     *            唯一标识
     * @param msgfmt
     *            内容编码
     * @return 唯一标识，如果不填写rrid将返回系统生成的
     */
    public String mdsmssend(String mobile, String content, String ext,
                            String stime, String rrid, String msgfmt) {
        String result = "";

        URL url;
        try {
            url = new URL(serviceURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write("短信报文内容".getBytes());//短信报文内容
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpconn.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconn.setRequestProperty("SOAPAction", "action地址");//action地址
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);

            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr = new InputStreamReader(
                    httpconn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (null != (inputLine = in.readLine())) {
                Pattern pattern = Pattern
                        .compile("<mdsmssendResult>(.*)</mdsmssendResult>");
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    result = matcher.group(1);
                }
            }
            return result;
        } catch (Exception e) {
//            LOG.warn("发送短信失败，mobile"+mobile
//                    +":,content："+content,e);
            return "";
        }
    }

    /**
     * 获取属性文件信息信息
     *
     * @return 返回数据库连接信息
     */
    private Properties getProperties(String fileName) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(filePath(fileName));
            prop.load(fis);
        } catch (IOException e) {
            return null;
        }
        return prop;
    }

    /**
     * 返回WEB-INF文件夹下的文件
     *
     * @param uri
     *            文件uri
     * @return 文件路径
     */
    private String filePath(String uri) {
        String path = SmsUtil.class.getResource("").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        int index = path.indexOf("WEB-INF");
        if (index == -1) {
            index = path.indexOf("classes");
        }
        if (index == -1) {
            index = path.indexOf("bin");
        }
        path = path.substring(0, index);
        StringBuffer sb = new StringBuffer();
        sb.append(path).append(uri);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        SmsUtil u=new SmsUtil();
        u.mdsmssend("85269527134", "你好，的的的单点的", "", "", "", "");
    }
}
