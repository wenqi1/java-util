package parametersUtil;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESUtil {
	
	/**
	 * aes解密
	 */
	public static String aesDecode(String encodeRules,String content)throws Exception{
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        keygen.init(128, new SecureRandom(encodeRules.getBytes()));
        SecretKey original_key=keygen.generateKey();
        byte [] raw=original_key.getEncoded();
        SecretKey key=new SecretKeySpec(raw, "AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte [] byte_content= Base64.decodeBase64(content);
        byte [] byte_decode=cipher.doFinal(byte_content);
        String aes_decode = new String(byte_decode,"utf-8");
        
        return aes_decode;

	}
	
	public static String aesDecode(String key,String iv ,String content)throws Exception{
		byte[] byte1 = Base64.decodeBase64(content);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);

        byte[] ret = cipher.doFinal(byte1);

    return new String(ret, "utf-8");
	}
	
	/**
	 * aes加密
	 */
	public static String aesEncode(String encodeRules,String content)throws Exception{
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        keygen.init(128, new SecureRandom(encodeRules.getBytes()));
        SecretKey original_key=keygen.generateKey();
        byte [] raw=original_key.getEncoded();
        SecretKey key=new SecretKeySpec(raw, "AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] byte_encode=content.getBytes("utf-8");
        byte [] byte_AES=cipher.doFinal(byte_encode);
        String AES_encode=Base64.encodeBase64String(byte_AES);
        
        return AES_encode;
	}
	
	public static String aesEncode(String key,String iv,String content)throws Exception{
		byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv1 = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv1);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        String aes_decode = Base64.encodeBase64String(encrypted);
        
        return aes_decode;
	}
	
}
