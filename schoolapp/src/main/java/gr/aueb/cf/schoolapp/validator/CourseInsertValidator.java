package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link CourseInsertDTO} objects.
 * Ensures that course names meet the required criteria.
 */
@Component
public class CourseInsertValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CourseInsertDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseInsertDTO courseInsertDTO = (CourseInsertDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "courseName", "empty");
        if (courseInsertDTO.getCourseName().length() < 2 || courseInsertDTO.getCourseName().length() > 45) {
            errors.reject("course name", "size");
        }
    }
}
