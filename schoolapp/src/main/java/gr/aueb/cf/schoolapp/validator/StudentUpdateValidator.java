package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link StudentUpdateDTO} objects.
 * Ensures that student data for update meets the required criteria.
 */
@Component
public class StudentUpdateValidator implements Validator {
    @Override
        public boolean supports(Class<?> clazz) {
            return StudentUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudentUpdateDTO studentUpdateDTO = (StudentUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (studentUpdateDTO.getFirstname().length() < 3 || studentUpdateDTO.getFirstname().length() > 50 ) {
            errors.reject("firstname", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (studentUpdateDTO.getLastname().length() < 3 || studentUpdateDTO.getLastname().length() > 50 ) {
            errors.reject("lastname", "size");
        }
    }
}
