package org.tamikalat.qrcloud.users;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public String register(RegisterBody request) {
    User user = new User();

    user.setEmail(request.getEmail());
    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ADMIN);

    user = userRepository.save(user);

    return jwtService.generateToken(user);
  }

  public String authenticate(LoginBody request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
    ));

    User user = userRepository.findByUsername(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

    return jwtService.generateToken(user);
  }
}
