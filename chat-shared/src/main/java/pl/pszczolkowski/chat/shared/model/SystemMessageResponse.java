package pl.pszczolkowski.chat.shared.model;

import java.time.LocalDateTime;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class SystemMessageResponse extends MessageResponse {

    private static final long serialVersionUID = 1L;
    
    public SystemMessageResponse(String text, LocalDateTime time) {
        super(text, time, MessageType.SYSTEM);
    }

}
