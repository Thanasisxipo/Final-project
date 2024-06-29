package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentDTO {
    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in firstname lenght")
    private String firstname;

    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in lastname lenght")
    private String lastname;

    @NotNull(message = "Error username should not be null")
    @Size(min = 3, max = 45, message = "Error in username lenght")
    private String username;

    @NotNull(message = "Error password should not be null")
    @Size(min = 5, max = 256, message = "Error in password lenght")
    private String password;

    private Role role = Role.valueOf("STUDENT");

    private Gender gender;
    @NotNull(message = "Error email should not be null")
    private  String email;

}
