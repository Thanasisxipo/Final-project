package gr.aueb.cf.schoolapp.dto;

import gr.aueb.cf.schoolapp.model.Student;
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
public class CityUpdateDTO extends BaseDTO{
    @NotNull(message = "Error city should not be null")
    @Size(min = 2, max = 45, message = "Error in city lenght")
    private String city;
    private Set<Student> students;

    public CityUpdateDTO(@NotNull Long id, String city, Set<Student> students) {
        this.setId(id);
        this.city = city;
        this.students = students;
    }
}
