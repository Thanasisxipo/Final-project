package gr.aueb.cf.schoolapp.service.exceptions;

import java.io.Serial;

public class UserAlreadyExistsException extends Exception{
    @Serial
    private static final long serialVersionUID = 3L;

    public UserAlreadyExistsException(String username) {
        super("User with username: " + username + " already exists");
    }
}
