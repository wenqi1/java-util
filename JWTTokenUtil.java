import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTokenUtil {

    /**
     * 采用salt加密
     * @param claims
     * @param subject
     * @param expirationSeconds
     * @param salt
     * @return
     */
    public static String generateToken(Map claims, String subject, int expirationSeconds, String salt) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds))
                .signWith(SignatureAlgorithm.HS256, salt)
                .compact();
    }

    /**
     * 采用公钥加密
     * @param claims
     * @param subject
     * @param expirationSeconds
     * @param publicKey
     * @return
     */
    public static String generateToken(Map claims, String subject, int expirationSeconds, PublicKey publicKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds))
                .signWith(SignatureAlgorithm.HS256, publicKey)
                .compact();
    }

    /**
     * 解析用salt加密的Token
     * @param token
     * @param salt
     * @return
     */
    public static Claims parseToken(String token, String salt){
        Claims claims = Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    /**
     * 解析用公钥加密的Token
     * @param token
     * @param privateKey
     * @return
     */
    public static Claims parseToken(String token, PrivateKey privateKey) {
        HashMap<String, Object> map = new HashMap<>();

        Claims claims = Jwts.parser()
                .setSigningKey(privateKey)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

}
