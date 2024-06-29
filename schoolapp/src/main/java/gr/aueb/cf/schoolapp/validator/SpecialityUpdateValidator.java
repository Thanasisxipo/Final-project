package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.SpecialityUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link SpecialityUpdateDTO} objects.
 * Ensures that speciality names meet the required criteria.
 */
@Component
public class SpecialityUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SpecialityUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpecialityUpdateDTO specialityUpdateDTO = (SpecialityUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "speciality", "empty");
        if (specialityUpdateDTO.getSpeciality().length() < 5 || specialityUpdateDTO.getSpeciality().length() > 50) {
            errors.reject("speciality", "size");
        }
    }
}
