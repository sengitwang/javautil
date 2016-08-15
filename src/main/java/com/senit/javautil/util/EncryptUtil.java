package com.senit.javautil.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sen on 2016/8/15.
 */
public class EncryptUtil {

    private static final String DES = "DES";
    public static final String KEY_ALGORTHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    public static byte[] encryptByDES(byte[] src, byte[] key)
            throws RuntimeException {
        try {
            SecureRandom sr = new SecureRandom();

            DESKeySpec dks = new DESKeySpec(addZerosForDes(key));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance(DES);

            cipher.init(1, securekey, sr);

            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptByDES(byte[] src, byte[] key)
            throws RuntimeException {
        try {
            SecureRandom sr = new SecureRandom();

            DESKeySpec dks = new DESKeySpec(addZerosForDes(key));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance(DES);

            cipher.init(2, securekey, sr);

            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final String decryptByDES(String src, String key) {
        return new String(
                decryptByDES(hex2byte(src.getBytes()), key.getBytes()));
    }

    public static final String encryptByDES(String src, String key) {
        if (src != null)
            try {
                return byte2hex(encryptByDES(src.getBytes(), key.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();

        for (int n = 0; (b != null) && (n < b.length); n++) {
            String stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));
        }
        return b2;
    }

    public static byte[] addZerosForDes(byte[] source) {
        byte[] ret = (byte[]) null;
        if ((source == null) || (source.length % 8 == 0)) {
            ret = source;
        } else {
            int i = source.length + 8 - source.length % 8;
            ret = new byte[i];
            System.arraycopy(source, 0, ret, 0, source.length);
        }
        return ret;
    }

    public static String encryptByPrivateKeyRSA(String content,
                                                String privateKey, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, prikey);

        InputStream ins = new ByteArrayInputStream(
                content.getBytes(input_charset));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        byte[] buf = new byte[117];
        int bufl = 0;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = (byte[]) null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(Base64.encode(writer.toByteArray()));
    }

    public static String decryptByPrivateKeyRSA(String content,
                                                String privateKey, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        byte[] buf = new byte[''];
        int bufl = 0;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = (byte[]) null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

    public static String encryptByPublicKeyRSA(String content,
                                               String publicKey, String input_charset) throws Exception {
        PublicKey pubkey = getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, pubkey);

        InputStream ins = new ByteArrayInputStream(
                content.getBytes(input_charset));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        byte[] buf = new byte[117];
        int bufl = 0;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = (byte[]) null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(Base64.encode(writer.toByteArray()));
    }

    public static String decryptByPublicKeyRSA(String content,
                                               String publicKey, String input_charset) throws Exception {
        PublicKey pubkey = getPublicKey(publicKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, pubkey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        byte[] buf = new byte[''];
        int bufl = 0;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = (byte[]) null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(
                encodedKey));
        return pubKey;
    }

    public static void main(String[] args) throws Exception {
        String str = "adbfe465879adadebc158ade2212aefd";
        String key = "SysTem#Config1qw";
        String encrypt = encryptByDES(str, key);
        System.out.println(encrypt);

        String decrypt = decryptByDES(encrypt, key);
        System.out.println(decrypt);
    }
}
