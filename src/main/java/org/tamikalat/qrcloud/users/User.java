package org.tamikalat.qrcloud.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tamikalat.qrcloud.tokens.Token;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstname;

  @Column(name = "last_name", nullable = false)
  private String lastname;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  @JsonIgnore()
  private String password;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @JsonIgnore()
  @JsonManagedReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
