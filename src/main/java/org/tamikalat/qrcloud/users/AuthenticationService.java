package org.tamikalat.qrcloud.users;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

  public AuthenticationResponse register(RegisterBody request) {
    User user = new User();

    user.setEmail(request.getEmail());
    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);

    user = userRepository.save(user);

    return new AuthenticationResponse(jwtService.generateToken(user), user);
  }

  public AuthenticationResponse authenticate(LoginBody request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
    ));

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

    return new AuthenticationResponse(jwtService.generateToken(user), user);
  }
}
