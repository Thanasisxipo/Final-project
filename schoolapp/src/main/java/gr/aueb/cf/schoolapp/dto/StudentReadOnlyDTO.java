package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Gender;
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
public class StudentReadOnlyDTO extends BaseDTO{
    private String firstname;
    private String lastname;
    private Gender gender;
    private String email;
    private City city;
    private User user;
    private Set<Course> courses;

    public StudentReadOnlyDTO(@NotNull Long id, String firstname, String lastname, Gender gender, String email, City city, User user, Set<Course> courses) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.email = email;
        this.city = city;
        this.user = user;
        this.courses = courses;
    }
}
