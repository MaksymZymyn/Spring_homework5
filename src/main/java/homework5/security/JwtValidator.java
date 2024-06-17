package homework5.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class JwtValidator {
    public static void main(String[] args) {
        String jwtString = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSmFuZSBEb2UiLCJlbWFpbCI6ImphbmVAZXhhbXBsZS5jb20iLCJzdWIiOiJqYW5lIiwianRpIjoiYWFkMzY5OTItNWMzZS00OWRhLTg4ODQtYzE0ZjI0M2QxZmM5IiwiaWF0IjoxNzAxODgzNDU5LCJleHAiOjE3MDE4ODM3NTl9.5qjUBL6aCwrBdJGksKylCIdzE0HnAQR3IN7oVMeTsaI";

        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        System.out.println(jwt);
    }
}
