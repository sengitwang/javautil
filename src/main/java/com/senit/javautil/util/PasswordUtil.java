package com.senit.javautil.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Random;

/**
 * Created by sen on 2016/8/15.
 */
public class PasswordUtil {
    public static final String toMD5(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();

        byte[] bs = null;
        try {
            bs = MessageDigest.getInstance("md5").digest(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        String tmp = null;
        for (int i = 0; i < bs.length; i++) {
            tmp = (Integer.toHexString(bs[i] & 0xFF));
            if (tmp.length() == 1) {
                result.append("0");
            }
            result.append(tmp);
        }

        return result.toString();
    }

    public static final String toSHA1(String strSrc) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return strDes;
    }

    private static final String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(toMD5("c111111"));
        System.out.println(toSHA1("123"));

    }

    /**
     * 字符串解密
     *
     * @param passWord
     *            密码
     * @param algorithm
     *            生成类型
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptPassword(String passWord, String algorithm,
                                         String key) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        KeySpec keySpec = new PBEKeySpec(key.toCharArray());
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] { 1,
                2, 3, 4, 5, 6, 7, 8 }, 1000);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        byte[] passDec = cipher.doFinal(hexStringToBytes(passWord));

        return new String(passDec);
    }

    /**
     * 字符串转换成字节
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 字符串转字节
     *
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String getRandString() {

        Random random = new Random();
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }

        return sRand;
    }
}
