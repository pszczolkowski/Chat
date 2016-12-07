package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class UserNotAuthenticatedException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserNotAuthenticatedException() {
        super(ErrorMessage.USER_NOT_AUTHENTICATED.toString());
    }
    
}
