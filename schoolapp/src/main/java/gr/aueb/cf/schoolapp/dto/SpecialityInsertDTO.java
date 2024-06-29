package gr.aueb.cf.schoolapp.dto;

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
public class SpecialityInsertDTO {
    @NotNull(message = "Error speciality should not be null")
    @Size(min = 5, max = 50, message = "Error in speciality lenght")
    private String speciality;

    private Set<Teacher> teachers;
}
