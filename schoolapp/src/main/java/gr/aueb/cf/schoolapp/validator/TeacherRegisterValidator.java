package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.RegisterTeacherDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link RegisterTeacherDTO} objects.
 * Ensures that student registration data meets the required criteria.
 */
@Component
public class TeacherRegisterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterTeacherDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterTeacherDTO dto = (RegisterTeacherDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (dto.getUsername().length() < 3 || dto.getUsername().length() > 50 ) {
            errors.reject("username", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (dto.getPassword().length() < 5 || dto.getPassword().length() > 100 ) {
            errors.reject("password", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (dto.getFirstname().length() < 3 || dto.getFirstname().length() > 50 ) {
            errors.reject("firstname", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (dto.getLastname().length() < 3 || dto.getLastname().length() > 50 ) {
            errors.reject("lastname", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ssn", "empty");
        if (dto.getSsn().length() != 9 ) {
            errors.reject("ssn", "size");
        }
    }
}
