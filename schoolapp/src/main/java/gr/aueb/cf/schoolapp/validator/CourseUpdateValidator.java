package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link CourseUpdateDTO} objects.
 * Ensures that course names meet the required criteria.
 */
@Component
public class CourseUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CourseUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseUpdateDTO courseUpdateDTO = (CourseUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "courseName", "empty");
        if (courseUpdateDTO.getCourseName().length() < 2 || courseUpdateDTO.getCourseName().length() > 45) {
            errors.reject("course name", "size");
        }
    }
}
