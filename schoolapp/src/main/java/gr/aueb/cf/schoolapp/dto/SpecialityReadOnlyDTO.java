package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpecialityReadOnlyDTO extends BaseDTO{
    private String speciality;


    public SpecialityReadOnlyDTO(@NotNull Long id, String speciality) {
        this.setId(id);
        this.speciality = speciality;

    }
}
