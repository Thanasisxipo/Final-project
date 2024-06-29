package gr.aueb.cf.schoolapp.service.exceptions;

import java.io.Serial;

public class StudentAlreadyExistsException extends Exception{
    @Serial
    private static final long serialVersionUID = 2L;

    public StudentAlreadyExistsException(String lastname) {
        super("Student with lastname: " + lastname + " already exists");
    }
}
