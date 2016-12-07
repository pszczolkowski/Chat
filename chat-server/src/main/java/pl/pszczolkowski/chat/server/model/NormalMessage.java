package pl.pszczolkowski.chat.server.model;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class NormalMessage extends Message {

    private final ActiveUser user;
    
    public NormalMessage(String text, ActiveUser user) {
        super(text);
        this.user = user;
    }

    public ActiveUser getUser() {
        return user;
    }

    @Override
    public MessageType getType() {
        return MessageType.NORMAL;
    }
    
}
