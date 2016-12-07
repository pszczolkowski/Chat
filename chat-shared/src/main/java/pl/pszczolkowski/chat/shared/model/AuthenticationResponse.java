package pl.pszczolkowski.chat.shared.model;

import java.io.Serializable;
import java.util.UUID;

public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final UUID userId;
    private final String userNick;
    private final UUID sessionId;
    
    public AuthenticationResponse(UUID userId, String userNick, UUID sessionId) {
        this.userId = userId;
        this.userNick = userNick;
        this.sessionId = sessionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public UUID getSessionId() {
        return sessionId;
    }
    
}
