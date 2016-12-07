package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class RoomAlreadyExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public RoomAlreadyExistException() {
        super(ErrorMessage.ROOM_ALREADY_EXISTS.toString());
    }

}
