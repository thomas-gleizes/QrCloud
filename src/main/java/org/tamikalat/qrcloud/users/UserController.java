package org.tamikalat.qrcloud.users;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final AuthenticationService authenticationService;

  public UserController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginBody request) {
    return ResponseEntity.ok(
        (authenticationService.authenticate(request)));
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterBody request) {
    return ResponseEntity.ok((authenticationService.register(request)));
  }

  @GetMapping("/me")
  public ResponseEntity<User> profil() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok((User) authentication.getPrincipal());
  }

}
