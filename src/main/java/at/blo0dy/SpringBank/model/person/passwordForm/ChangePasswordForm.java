package at.blo0dy.SpringBank.model.person.passwordForm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {

  @NotBlank(message = "Old Password must be defined.")
  @NotNull(message = "Old Password must be defined.")
  private String oldPassword;

  @NotBlank(message = "New Password must be defined.")
  @NotNull(message = "New Password must be defined.")
  private String newPassword;

  @NotBlank(message = "Password must be confirmed.")
  @NotNull(message = "Password must be confirmed.")
  private String confirmPassword;




}
