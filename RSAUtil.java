package parametersUtil;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSAUtil {
	
	/**
	 * 私钥解密
	 */
	public static String rsaDecode(String keyStr,String content) throws Exception{
		byte[] keyBytes = Base64.decodeBase64(keyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //获取私钥
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		Cipher cipher = Cipher.getInstance("rsa");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int i=0;
		int offSet = 0;
		int contentLength = Base64.decodeBase64(content).length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] cache;
		//解密
		while(contentLength - offSet > 0){
			if (contentLength - offSet > 256) {
                cache = cipher.doFinal(Base64.decodeBase64(content), offSet, 256);
            } else {
                cache = cipher.doFinal(Base64.decodeBase64(content), offSet, contentLength - offSet);
            }
            out.write(cache);
            i++;
            offSet = i * 256;
        }
		byte[] rsa_decode = out.toByteArray();
		
		return new String(rsa_decode);
	}
	
	/**
	 * 公钥加密
	 */
	public static String rsaEncode(String keyStr,String content) throws Exception{
		byte[] keyBytes = Base64.decodeBase64(keyStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //获取公钥对象
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
		//得到Cipher对象来实现对源数据的RSA加密
		Cipher cipher = Cipher.getInstance("rsa");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int i=0;
		int offSet = 0;
		int contentLength = content.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] cache;
		//加密
		while(contentLength - offSet > 0){
			if (contentLength - offSet > 256) {
                cache = cipher.doFinal(content.getBytes(), offSet, 256);
            } else {
                cache = cipher.doFinal(content.getBytes(), offSet, contentLength - offSet);
            }
            out.write(cache);
            i++;
            offSet = i * 256;
        }
		byte[] rsa_encode = out.toByteArray();

		return Base64.encodeBase64String(rsa_encode);
	}
	
	/**
	 * 生成公私钥
	 */
	public static Map<String,String> generatorPubPriKey() throws Exception{
		
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("rsa");
		//利用上面的随机数据源初始化这个KeyPairGenerator对象 
		// keyPairGenerator.initialize(KEYSIZE, secureRandom);
		keyPairGenerator.initialize(2048,new SecureRandom());
		//生成密匙对 
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		//公钥 
		Key publicKey = keyPair.getPublic();
		String PUBLIC_KEY = Base64.encodeBase64String(publicKey.getEncoded());
		//私钥 
		Key privateKey = keyPair.getPrivate();
		String PRIVATE_KEY = Base64.encodeBase64String(privateKey.getEncoded());

		Map<String, String> keyMap = new HashMap<String,String>();
		keyMap.put("PUBLIC_KEY",PUBLIC_KEY);
		keyMap.put("PRIVATE_KEY",PRIVATE_KEY);
		return keyMap;
	}
}
