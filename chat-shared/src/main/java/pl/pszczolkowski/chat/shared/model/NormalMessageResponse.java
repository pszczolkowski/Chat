package pl.pszczolkowski.chat.shared.model;

import java.time.LocalDateTime;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class NormalMessageResponse extends MessageResponse {

    private static final long serialVersionUID = 1L;
    
    private final String nick;
    
    public NormalMessageResponse(String text, String nick, LocalDateTime time) {
        super(text, time, MessageType.NORMAL);
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

}
