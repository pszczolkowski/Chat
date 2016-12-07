package pl.pszczolkowski.chat.shared.model;

import java.time.LocalDateTime;

import pl.pszczolkowski.chat.shared.enums.MessageType;

public class PrivateMessageResponse extends MessageResponse {

    private static final long serialVersionUID = 1L;
    
    private final String senderNick;
    private final String receiverNick;
    
    public PrivateMessageResponse(String text, String senderNick, String receiverNick, LocalDateTime time) {
        super(text, time, MessageType.PRIVATE);
        this.senderNick = senderNick;
        this.receiverNick = receiverNick;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public String getReceiverNick() {
        return receiverNick;
    }

}
