package pl.pszczolkowski.chat.shared.model;

import java.io.Serializable;
import java.util.UUID;

public class RoomResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final UUID id;
    private final String name;

    public RoomResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
