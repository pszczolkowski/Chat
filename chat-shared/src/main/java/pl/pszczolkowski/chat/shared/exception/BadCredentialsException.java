package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class BadCredentialsException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadCredentialsException() {
        super(ErrorMessage.BAD_CREDENTIALS.toString());
    }

}
