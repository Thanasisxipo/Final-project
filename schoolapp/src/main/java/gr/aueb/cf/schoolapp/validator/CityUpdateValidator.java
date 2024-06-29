package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.CityUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link CityUpdateDTO} objects.
 * Ensures that city names meet the required criteria.
 */
@Component
public class CityUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CityUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CityUpdateDTO cityUpdateDTO = (CityUpdateDTO) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "empty");
        if (cityUpdateDTO.getCity().length() < 3 || cityUpdateDTO.getCity().length() > 50) {
            errors.reject("city", "size");
        }
    }
}
