import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密与解密工具类
 */
public class AESUtils {

    /**
     * AES加密
     * @param plaintext 明文
     * @param seed SecureRandom的种子
     * @param keySize key的长度
     * @return
     */
    public static String encode(String plaintext, String seed, int keySize) {
        if (plaintext == null || plaintext.length() == 0){
            return null;
        }
        try {
            byte[] encodeKey = generateKey(seed, keySize);
            SecretKey secretKey = new SecretKeySpec(encodeKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] plaintextBytes = plaintext.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
            String ciphertext = Base64.encodeBase64String(ciphertextBytes);
            return ciphertext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     * @param ciphertext 密文
     * @param key key
     * @return
     */
    public static String decode(String ciphertext, String key) {
        if (ciphertext == null || ciphertext.length() ==0 ) {
            return null;
        }
        if (ciphertext.trim().length() < 19) {
            return ciphertext;
        }
        byte[] ciphertextBytes = Base64.decodeBase64(ciphertext);
        try {
            byte[] encodeKey = Base64.decodeBase64(key);
            SecretKey secretKey = new SecretKeySpec(encodeKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plaintext = cipher.doFinal(ciphertextBytes);
            return new String(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成key
     * @param seed SecureRandom的种子
     * @param keySize key的长度
     * @param filePath 保存key的路径
     */
    public static void generateKey(String seed, int keySize, String filePath){
        try {
            //实例化一个AES KeyGenerator
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed.getBytes());
            keyGenerator.init(keySize, random);
            //生成key
            SecretKey secretKey = keyGenerator.generateKey();
            //保存到指定路径
            Path path = Paths.get(filePath + "/aes.key");
            Files.write(path, Base64.encodeBase64(secretKey.getEncoded()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成key
     * @param seed SecureRandom的种子
     * @param keySize key的长度
     * @return
     */
    public static byte[] generateKey(String seed, int keySize){
        try {
            //实例化一个AES KeyGenerator
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed.getBytes());
            keyGenerator.init(keySize, random);
            //生成key
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

