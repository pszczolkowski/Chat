package pl.pszczolkowski.chat.server.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private final UUID id;
    private ActiveUser user;
    private LocalDateTime lastActivityTime;

    public Session(ActiveUser user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.lastActivityTime = LocalDateTime.now();
    }

    public UUID getId() {
        return this.id;
    }

    public ActiveUser getUser() {
        return user;
    }

    public void updateLastActivity() {
        this.lastActivityTime = LocalDateTime.now();
    }

    public LocalDateTime getLastActivityTime() {
        return lastActivityTime;
    }

}
