package org.tamikalat.qrcloud.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class LoginBody {

  @Email
  private String email;

  @Min(6)
  @Max(64)
  private String password;

}
