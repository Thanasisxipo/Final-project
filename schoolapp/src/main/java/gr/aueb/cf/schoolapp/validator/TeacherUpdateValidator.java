package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link TeacherUpdateDTO} objects.
 * Ensures that teacher data for update meets the required criteria.
 */
@Component
public class TeacherUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherUpdateDTO teacherUpdateDTO = (TeacherUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (teacherUpdateDTO.getFirstname().length() < 3 || teacherUpdateDTO.getFirstname().length() > 50 ) {
            errors.reject("firstname", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (teacherUpdateDTO.getLastname().length() < 3 || teacherUpdateDTO.getLastname().length() > 50 ) {
            errors.reject("lastname", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ssn", "empty");
        if (teacherUpdateDTO.getSsn().length() != 9 ) {
            errors.reject("ssn", "size");
        }
    }
}
