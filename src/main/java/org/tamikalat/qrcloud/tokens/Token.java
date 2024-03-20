package org.tamikalat.qrcloud.tokens;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.tamikalat.qrcloud.users.User;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hash", nullable = false)
  private String hash;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "is_activate")
  private Boolean isActivate;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private Date createdAt;

}
