package org.tamikalat.qrcloud.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String SECRET_TOKEN = "15d5c9389f9e849346013657a0c50d3b3f4389b51f90d1773d19966badb8aa32";

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(this.getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public <T> T extractClaims(String token, Function<Claims, T> resolver) {
    Claims claims = extractAllClaims(token);
    return resolver.apply(claims);
  }

  public String extractUsername(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaims(token, Claims::getExpiration);
  }

  public boolean isValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(this.getSignInKey())
        .compact();

  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_TOKEN);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
