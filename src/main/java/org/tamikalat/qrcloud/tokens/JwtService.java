package org.tamikalat.qrcloud.tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tamikalat.qrcloud.users.User;

@Service
public class JwtService {

  private final String SECRET_TOKEN = "15d5c9389f9e849346013657a0c50d3b3f4389b51f90d1773d19966badb8aa32";
  private final TokenRepository tokenRepository;

  public JwtService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

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

  public String extractHash(String token) {
    return token.split("\\.")[2];
  }

  public boolean isValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isRegistered(
        token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public boolean isRegistered(String token) {
    Optional<Token> find = tokenRepository.findByHash(extractHash(token));
    return find.isPresent() && find.get().getIsActivate();
  }

  public String generateToken(User user) {
    final String jwt = Jwts.builder()
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(this.getSignInKey())
        .compact();

    Token token = new Token();

    token.setHash(extractHash(jwt));
    token.setUser(user);
    token.setIsActivate(true);

    tokenRepository.save(token);

    return jwt;
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_TOKEN);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public void disabledToken(String jwt) {
    String hash = this.extractHash(jwt);
    Token token = tokenRepository.findByHash(hash).orElseThrow();

    token.setIsActivate(false);

    tokenRepository.save(token);
  }
}
