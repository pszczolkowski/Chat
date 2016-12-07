package pl.pszczolkowski.chat.shared.exception;

import pl.pszczolkowski.chat.shared.enums.ErrorMessage;

public class NoParticipatedRoomException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoParticipatedRoomException() {
        super(ErrorMessage.NO_PARTICIPATED_ROOM.toString());
    }
    
}
