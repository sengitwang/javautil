package cn.com.bsfit.frms.rate.util;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sen on 2016/10/18.
 */
public class KeyUtil {
    public  static PrivateKey pkey ;

    public	static PublicKey pubkey ;

    public static Map<String,Object> genKey() throws Exception{

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kep = kpg.generateKeyPair();
        Provider p  = kpg.getProvider();
        System.out.println(p.getName());
        pkey = kep.getPrivate();
        pubkey = kep.getPublic();
        System.out.println("生成的公钥"+new String(Base64Util.encode(pubkey.getEncoded())));
        System.out.println("生成的私钥"+new String(Base64Util.encode(pkey.getEncoded())));

        Map<String,Object> param=new HashMap<String,Object>();
        param.put("PublicKey", new String(Base64Util.encode(pubkey.getEncoded())));
        param.put("PrivateKey", new String(Base64Util.encode(pkey.getEncoded())));

        return param;
    }


    public static void main(String[] args) throws Exception{
        Map<String,Object> param=genKey();
        String pb=(String) param.get("PublicKey");
        String pr=(String) param.get("PrivateKey");
        System.out.println(pb);
        System.out.println(pr);
    }
}
