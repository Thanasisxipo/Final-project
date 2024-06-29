package gr.aueb.cf.schoolapp.service.exceptions;

import java.io.Serial;

public class TeacherAlreadyExistsException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public TeacherAlreadyExistsException(String lastname) {
        super("Teacher with lastname: " + lastname + " already exists");
    }
}
