package pl.pszczolkowski.chat.server.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Room {

    private final UUID id;
    private final String name;
    private LocalDateTime lastActivityTime;
    private final List<ActiveUser> users = new ArrayList<>();
    
    public Room(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.lastActivityTime = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void addUser(ActiveUser user) {
        synchronized (users) {
            users.add(user);
        }
        
        updateLastActivity();
    }

    public void removeUser(ActiveUser user) {
        synchronized (users) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.remove(i);
                    updateLastActivity();
                    break;
                }
            }
        }
    }

    private void updateLastActivity() {
        lastActivityTime = LocalDateTime.now();
    }

    public void broadcastMessage(Message message) {
        synchronized (users) {
            for (ActiveUser user : users) {
                user.pushMessage(message);
            }
        }
        
        updateLastActivity();
    }

    public LocalDateTime getLastActivityTime() {
        return lastActivityTime;
    }

}
