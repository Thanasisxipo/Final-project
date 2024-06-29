package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Speciality;
import gr.aueb.cf.schoolapp.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherUpdateDTO extends BaseDTO{
    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in firstname lenght")
    private String firstname;

    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in lastname lenght")
    private String lastname;

    @NotNull(message = "Error ssn should not be null")
    @Size(min = 9, max = 9, message = "Error in ssn lenght")
    private  String ssn;

    @NotNull(message = "Error email should not be null")
    private  String email;

    private Speciality speciality;
    private User user;
    private Set<Course> courses;

    public TeacherUpdateDTO(@NotNull Long id, String firstname, String lastname, String ssn, String email, Speciality speciality, User user, Set<Course> courses) {
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
