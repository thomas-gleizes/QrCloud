package org.tamikalat.qrcloud.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RegisterBody {

  @Min(3)
  @Max(64)
  private String firstname;

  @Min(3)
  @Max(64)
  private String lastname;

  @Min(3)
  @Max(64)
  private String country;

  @Email
  @Max(64)
  private String email;

  @Min(6)
  @Max(32)
  private String password;

}
