import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/**
 * RSA加密与解密工具类
 */
public class RSAUtils {

    /**
     * 生成公私钥对
     * @param keySize key的长度
     * @param filePath 保存key的路径
     */
    public static void generateKeyPair(int keySize, String filePath) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            //保存到指定路径
            Path publicPath = Paths.get(filePath + "/public.pem");
            Path privatePath = Paths.get(filePath + "/private.pem");
            Files.write(publicPath, Base64.encodeBase64(publicKey.getEncoded()));
            Files.write(publicPath, Base64.encodeBase64(privateKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成公私钥对
     * @param keySize key的长度
     * @return Map(私钥的key为private,公钥的key为public)
     */
    public static Map<String, String> generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
            PrivateKey privateKey = keyPair.getPrivate();
            String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
            HashMap<String, String> keyPairMap = new HashMap<>();
            keyPairMap.put("public", publicKeyStr);
            keyPairMap.put("private", privateKeyStr);
            return keyPairMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     * @param plaintext 明文
     * @param publicKeyStr 公钥字符串
     * @return
     */
    public static String encode(String plaintext, String publicKeyStr){
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //获取公钥对象
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            //得到Cipher对象来实现对源数据的RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int i=0;
            int offSet = 0;
            int contentLength = plaintext.getBytes().length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] cache;
            //分段加密
            while(contentLength - offSet > 0){
                if (contentLength - offSet > 256) {
                    cache = cipher.doFinal(plaintext.getBytes(), offSet, 256);
                } else {
                    cache = cipher.doFinal(plaintext.getBytes(), offSet, contentLength - offSet);
                }
                out.write(cache);
                i++;
                offSet = i * 256;
            }
            byte[] ciphertextBytes = out.toByteArray();
            return Base64.encodeBase64String(ciphertextBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥解密
     * @param ciphertext 密文
     * @param privateKeyStr 私钥字符串
     * @return
     */
    public static String decode(String ciphertext, String privateKeyStr){
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //获取私钥
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("rsa");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int i=0;
            int offSet = 0;
            int contentLength = Base64.decodeBase64(ciphertext).length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] cache;
            //分段解密
            while(contentLength - offSet > 0){
                if (contentLength - offSet > 256) {
                    cache = cipher.doFinal(Base64.decodeBase64(ciphertext), offSet, 256);
                } else {
                    cache = cipher.doFinal(Base64.decodeBase64(ciphertext), offSet, contentLength - offSet);
                }
                out.write(cache);
                i++;
                offSet = i * 256;
            }
            byte[] plaintext = out.toByteArray();
            return new String(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

