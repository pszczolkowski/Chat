package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class UserAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super(ErrorMessage.USER_ALREADY_EXISTS.toString());
    }
    
}
