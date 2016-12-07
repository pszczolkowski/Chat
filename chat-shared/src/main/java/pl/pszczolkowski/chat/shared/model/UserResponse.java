package pl.pszczolkowski.chat.shared.model;

import java.io.Serializable;

public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String nick;

    public UserResponse(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

}
