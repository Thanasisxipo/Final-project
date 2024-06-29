package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Role;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO extends BaseDTO {
    private String username;
    private String password;
    private Role role;
    private Teacher teacher;
    private Student student;

    public UserReadOnlyDTO(@NotNull Long id, String username, String password, Role role, Teacher teacher, Student student) {
        this.setId(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.teacher = teacher;
        this.student = student;
    }
}
