package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.UserUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link UserUpdateDTO} objects.
 * Ensures that user update data meets the required criteria.
 */
@Component
public class UserUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserUpdateDTO userUpdateDTO = (UserUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userUpdateDTO.getUsername().length() < 3 || userUpdateDTO.getUsername().length() > 50 ) {
            errors.reject("username", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userUpdateDTO.getPassword().length() < 5 || userUpdateDTO.getPassword().length() > 100 ) {
            errors.reject("password", "size");
        }
    }
}
