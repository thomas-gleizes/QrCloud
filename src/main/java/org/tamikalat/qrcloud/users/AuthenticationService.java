package org.tamikalat.qrcloud.users;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tamikalat.qrcloud.tokens.JwtService;

@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      AuthenticationManager authenticationManager
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthenticationResponse register(RegisterBody body) {
    User user = new User();

    user.setEmail(body.getEmail());
    user.setFirstname(body.getFirstname());
    user.setLastname(body.getLastname());
    user.setPassword(passwordEncoder.encode(body.getPassword()));
    user.setRole(Role.USER);

    user = userRepository.save(user);

    return new AuthenticationResponse(jwtService.generateToken(user), user);
  }

  public AuthenticationResponse authenticate(LoginBody body) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        body.getEmail(),
        body.getPassword()
    ));

    User user = userRepository.findByEmail(body.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

    return new AuthenticationResponse(jwtService.generateToken(user), user);
  }

  public User getCurrent() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }

  public void logout(String token) {
    jwtService.disabledToken(token);
  }
}
