package gr.aueb.cf.schoolapp.service.exceptions;

import java.io.Serial;

public class WrongPasswordException extends Exception{
    @Serial
    private static final long serialVersionUID = 5L;

    public WrongPasswordException() {
        super("Wrong password");
    }
}
