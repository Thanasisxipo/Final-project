package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.UserLoginDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link UserLoginDTO} objects.
 * Ensures that user login data meets the required criteria.
 */
@Component
public class UserLoginValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginDTO userLoginDTO = (UserLoginDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userLoginDTO.getUsername().length() < 3 || userLoginDTO.getUsername().length() > 50 ) {
            errors.reject("username", "size");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userLoginDTO.getPassword().length() < 5 || userLoginDTO.getPassword().length() > 100 ) {
            errors.reject("password", "size");
        }
    }
}
