package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class UserDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserDoesNotExistException() {
        super(ErrorMessage.USER_DOES_NOT_EXIST.toString());
    }
    
}
