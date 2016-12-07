package pl.pszczolkowski.chat.server.model;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String nick;
    private final String password;

    public User(String nick, String password) {
        this.id = UUID.randomUUID();
        this.nick = nick;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

}
