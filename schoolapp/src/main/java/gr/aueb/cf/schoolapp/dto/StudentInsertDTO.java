package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Gender;
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
public class StudentInsertDTO {
    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in firstname lenght")
    private String firstname;

    @NotNull(message = "Error firstname should not be null")
    @Size(min = 2, max = 45, message = "Error in lastname lenght")
    private String lastname;

    private Gender gender;
    @NotNull(message = "Error email should not be null")
    private  String email;
    private City city;
    private User user;
    private Set<Course> courses;
}
