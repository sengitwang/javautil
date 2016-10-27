package com.senit.javautil.util;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * RSA加密原理概述
 * RSA的安全性依赖于大数的分解，公钥和私钥都是两个大素数（大于100的十进制位）的函数。
 * 据猜测，从一个密钥和密文推断出明文的难度等同于分解两个大素数的积
 * ===================================================================
 * （该算法的安全性未得到理论的证明）
 * ===================================================================
 * 密钥的产生：
 * 1.选择两个大素数 p,q ,计算 n=p*q;
 * 2.随机选择加密密钥 e ,要求 e 和 (p-1)*(q-1)互质
 * 3.利用 Euclid 算法计算解密密钥 d , 使其满足 e*d = 1(mod(p-1)*(q-1)) (其中 n,d 也要互质)
 * 4:至此得出公钥为 (n,e) 私钥为 (n,d)
 * ===================================================================
 * 加解密方法：
 * 1.首先将要加密的信息 m(二进制表示) 分成等长的数据块 m1,m2,...,mi 块长 s(尽可能大) ,其中 2^s<n
 * 2:对应的密文是： ci = mi^e(mod n)
 * 3:解密时作如下计算： mi = ci^d(mod n)
 * ===================================================================
 * RSA速度
 * 由于进行的都是大数计算，使得RSA最快的情况也比DES慢上100倍，无论 是软件还是硬件实现。
 * 速度一直是RSA的缺陷。一般来说只用于少量数据 加密。
 * Created by sen on 2016/10/17.
 */
public class RsaUtil {
    public static String ALGORITHM = "RSA";

    public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算饭

    /**
     * 数据签名
     *
     * @param content
     *            签名内容
     * @param privateKey
     *            私钥
     * @return 返回签名数据
     */
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64Util.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));

            byte[] signed = signature.sign();

            return Base64Util.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名验证
     *
     * @param content
     * @param sign
     * @param lakala_public_key
     * @return
     */
    public static boolean verify(String content, String sign,
                                 String lakala_public_key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64Util.decode(lakala_public_key);
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("UTF-8"));

            boolean bverify = signature.verify(Base64Util.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 通过公钥解密
     *
     * @param content待解密数据
     * @param pk公钥
     * @return 返回 解密后的数据
     */
    protected static byte[] decryptByPublicKey(String content, PublicKey pk) {

        try {
            Cipher ch = Cipher.getInstance(ALGORITHM);
            ch.init(Cipher.DECRYPT_MODE, pk);
            InputStream ins = new ByteArrayInputStream(Base64Util.decode(content));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }

                writer.write(ch.doFinal(block));
            }

            return writer.toByteArray();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 通过私钥加密
     *
     * @param content
     * @param pk
     * @return,加密数据，未进行base64进行加密
     */
    protected static byte[] encryptByPrivateKey(String content, PrivateKey pk) {

        try {
            Cipher ch = Cipher.getInstance(ALGORITHM);
            ch.init(Cipher.ENCRYPT_MODE, pk);
            return ch.doFinal(content.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("通过私钥加密出错");
        }
        return null;

    }

    /**
     * 解密数据，接收端接收到数据直接解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) {
        if (null == key || "".equals(key)) {
            return null;
        }
        PublicKey pk = getPublicKey(key);
        byte[] data = decryptByPublicKey(content, pk);
        String res = null;
        try {
            res = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 对内容进行加密
     *
     * @param content
     * @param key私钥
     * @return
     */
    public static String encrypt(String content, String key) {
        PrivateKey pk = getPrivateKey(key);
        byte[] data = encryptByPrivateKey(content, pk);
        String res = null;
        try {
            res = Base64Util.encode(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    /*
     * 得到私钥对象
    * @param key 密钥字符串（经过base64编码的秘钥字节）
    * @throws Exception
    */
    public static PrivateKey getPrivateKey(String privateKey)  {
        try {
            byte[] keyBytes;

            keyBytes = Base64Util.decode(privateKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey privatekey = keyFactory.generatePrivate(keySpec);

            return privatekey;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取公钥对象
     * @param key 密钥字符串（经过base64编码秘钥字节）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) {

        try {

            byte[] keyBytes;

            keyBytes = Base64Util.decode(publicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publickey = keyFactory.generatePublic(keySpec);

            return publickey;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        String str = Base64Util.encode("test".getBytes());
        System.out.println("Base64加密-->" + str);
        String aaa = sign(
                str,
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKWmOMF5zQBqc8xA41zq9BKHI8GUdZdJR8qP57mv8y2N7T/TD1YeNEx3UH7cSWpsZpdzV5BWKAhH7sTOWfhIU+EhrXrWbQWAndWcT2+ZDYlI0ctJ5Bo1gM0MEU01+g3mhOf70I9DJrYGvoVYV815m+F46bjq8qVEL06zZZLEKCTPAgMBAAECgYEAjZl3rrvFp/NXpWRadtVJaoUm5ZVYp8g2nEtDVJG5mFlYU1TCKWWMY0kjAC6ie1zKnfA1C+b6NYn36zhR5FE/kTSwUYT1P6INT4rD7JUEiwE8hi4MTvWIDCyqeUmb2H+abHpBo9VZymmh5wmwRTi1PgPQGTDq5uP519lgD00DzEECQQDZzHPSvgy0GztmD6Uip1mHDI9j7syIEVCEPtuIsNzH63GQdR5jLH7hZn0WRXEgrZa+f3gKZnFIew+lj6Ip1lPRAkEAwrQrwe6dcioJHdc0PsW/In5TOBTY+ppVKhrkJ6x9UOzZT/BYuXUFVJL8kiKGIK0wihOzMhHK57HjoN5fT5w2nwJBAJ5D4npmXgbWrxAYGFCZOQZYyy28DmZl5pNitdabZqPj5A8r/Bvm7oBOIGF5rp4nZh4htJIiJPmdax5MxHMQarECQH8yMPvqpJT2fSovcwQnL2ybVkZm6DEfLc/p7W81slBxyq38eBoAJtFPjQzy3Ojv+6vYntJw6Ttf7TMk0uMxTEUCQDw1ZXU5jiKoktSYjHjFL2EfqtfT9F8xTHVyaruKL+R67Z0OvxMNlM0j77ADUS/BAFlwF2bCsSkMT3PLvcNtpPk=");
        System.out.println("加签-->" + aaa);
        System.out
                .println("验签-->"
                        + verify(
                        str,
                        aaa,
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClpjjBec0AanPMQONc6vQShyPBlHWXSUfKj+e5r/Mtje0/0w9WHjRMd1B+3ElqbGaXc1eQVigIR+7Ezln4SFPhIa161m0FgJ3VnE9vmQ2JSNHLSeQaNYDNDBFNNfoN5oTn+9CPQya2Br6FWFfNeZvheOm46vKlRC9Os2WSxCgkzwIDAQAB"));
        System.out.println("原文-->" + new String(Base64Util.decode(str)));
    }

}
