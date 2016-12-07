package pl.pszczolkowski.chat.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ActiveUser {

    private final User user;
    private final BlockingQueue<Message> awaitingMessages = new LinkedBlockingQueue<>();
    private Room participatedRoom;
    
    public ActiveUser(User user) {
        this.user = user;
    }

    public UUID getId() {
        return user.getId();
    }
    
    public String getNick () {
        return user.getNick();
    }
    
    public Room getParticipatedRoom() {
        return participatedRoom;
    }

    public void setParticipatedRoom(Room room) {
        this.participatedRoom = room;
    }

    public void pushMessage(Message message) {
        awaitingMessages.add(message);
    }

    public List<Message> pollAwaitingMessages(long timeout) throws InterruptedException {
        List<Message> messages = new ArrayList<>();
        Message firstMessage = awaitingMessages.poll(timeout, TimeUnit.MILLISECONDS);
        
        if (firstMessage != null) {
            messages.add(firstMessage);
        }
        
        Message message = awaitingMessages.poll();
        while (message != null) {
            messages.add(message);
            message = awaitingMessages.poll();
        }
        
        return messages;
    }

    public List<Message> getAwaitingMessages() {
        List<Message> messages = new ArrayList<>();
        
        Message message = awaitingMessages.poll();
        while (message != null) {
            messages.add(message);
            message = awaitingMessages.poll();
        }
        
        return messages;
    }

    public void leaveRoom() {
        this.participatedRoom = null;
    }
    
}
