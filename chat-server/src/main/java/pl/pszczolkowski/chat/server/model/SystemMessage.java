package pl.pszczolkowski.chat.server.model;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class SystemMessage extends Message {

    public SystemMessage(String text) {
        super(text);
    }

    @Override
    public MessageType getType() {
        return MessageType.SYSTEM;
    }

}
