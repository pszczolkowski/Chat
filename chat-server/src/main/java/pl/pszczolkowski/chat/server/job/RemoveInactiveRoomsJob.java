package pl.pszczolkowski.chat.server.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.server.service.Chat;
import pl.pszczolkowski.chat.shared.Values;

@Component
public class RemoveInactiveRoomsJob {

    private final Chat chat;
    
    @Autowired
    public RemoveInactiveRoomsJob(Chat chat) {
        this.chat = chat;
    }

    @Scheduled(fixedRate = Values.REMOVE_INACTIVE_ROOMS_INTERVAL)
    public void removeInactiveRooms() {
        chat.removeInactiveRooms();
    }
    
}
