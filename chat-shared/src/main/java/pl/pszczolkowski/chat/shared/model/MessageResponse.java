package pl.pszczolkowski.chat.shared.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public abstract class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String text;
    private final long time;
    private final MessageType type;
    
    public MessageResponse(String text, LocalDateTime time, MessageType type) {
        this.text = text;
        this.type = type;
        this.time = time.toInstant(ZoneOffset.UTC).getEpochSecond();
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
    }

    public MessageType getType() {
        return type;
    }
    
}
