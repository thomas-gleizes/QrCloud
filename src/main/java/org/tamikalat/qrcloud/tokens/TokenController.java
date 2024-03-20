package org.tamikalat.qrcloud.tokens;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tamikalat.qrcloud.users.AuthenticationService;

@RestController
@RequestMapping("/users/tokens")
@AllArgsConstructor
public class TokenController {

  private final TokenRepository tokenRepository;
  private final AuthenticationService authenticationService;

  @GetMapping("")
  public ResponseEntity<List<Token>> getTokens() {
    List<Token> tokens = tokenRepository.findAllByUser(authenticationService.getCurrent());
    return ResponseEntity.ok(tokens);
  }

}
