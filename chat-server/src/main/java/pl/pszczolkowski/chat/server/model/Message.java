package pl.pszczolkowski.chat.server.model;

import java.time.LocalDateTime;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public abstract class Message {

    private final String text;
    private final LocalDateTime time;
    
    public Message(String text) {
        this.text = text;
        this.time = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return time;
    }
    
    public abstract MessageType getType();

}
