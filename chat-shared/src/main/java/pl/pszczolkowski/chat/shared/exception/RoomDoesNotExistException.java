package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class RoomDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public RoomDoesNotExistException() {
        super(ErrorMessage.ROOM_DOES_NOT_EXISTS.toString());
    }

}
