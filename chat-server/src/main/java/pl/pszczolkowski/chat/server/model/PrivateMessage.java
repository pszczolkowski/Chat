package pl.pszczolkowski.chat.server.model;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class PrivateMessage extends Message {

    private final ActiveUser sender;
    private final ActiveUser receiver;
    
    public PrivateMessage(String text, ActiveUser user, ActiveUser receiver) {
        super(text);
        this.sender = user;
        this.receiver = receiver;
    }

    public ActiveUser getSender() {
        return sender;
    }

    public ActiveUser getReceiver() {
        return receiver;
    }
    
    @Override
    public MessageType getType() {
        return MessageType.PRIVATE;
    }

}
