package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Speciality;
import gr.aueb.cf.schoolapp.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherReadOnlyDTO extends BaseDTO {
    private String firstname;
    private String lastname;
    private  String ssn;
    private String email;
    private Speciality speciality;
    private User user;
    private Set<Course> courses;

    public TeacherReadOnlyDTO(@NotNull Long id, String firstname, String lastname, String ssn, String email, Speciality speciality, User user, Set<Course> courses) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.email = email;
        this.speciality = speciality;
        this.user = user;
        this.courses = courses;
    }
}
