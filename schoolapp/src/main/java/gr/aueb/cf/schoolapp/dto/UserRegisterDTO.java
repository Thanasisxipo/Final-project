package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Role;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
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
public class UserRegisterDTO {
    @NotNull(message = "Error username should not be null")
    @Size(min = 3, max = 45, message = "Error in username lenght")
    private String username;

    @NotNull(message = "Error password should not be null")
    @Size(min = 5, max = 256, message = "Error in password lenght")
    private String password;

    private Role role;

    private Teacher teacher;
    private Student student;
}
