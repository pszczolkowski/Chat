package pl.pszczolkowski.chat.server.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Room;
import pl.pszczolkowski.chat.server.model.SystemMessage;
import pl.pszczolkowski.chat.server.service.Chat;

@Component
public class SessionExpiredListener implements ApplicationListener<SessionExpiredEvent> {

    private final Chat chat;
    
    @Autowired
    public SessionExpiredListener(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void onApplicationEvent(SessionExpiredEvent event) {
        ActiveUser user = event.getSession().getUser();
        
        Room participatedRoom = user.getParticipatedRoom();
        if (participatedRoom != null) {
            chat.removeUserFromRoom(user, participatedRoom);
            participatedRoom.broadcastMessage(new SystemMessage(user.getNick() + " timed out"));
        }
    }

}
