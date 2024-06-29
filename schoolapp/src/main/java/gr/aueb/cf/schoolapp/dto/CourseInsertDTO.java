package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
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
public class CourseInsertDTO {
    @NotNull(message = "Error course name should not be null")
    @Size(min = 2, max = 45, message = "Error in course name lenght")
    private String courseName;

    private Teacher teacher;
    private Set<Student> students;
}
