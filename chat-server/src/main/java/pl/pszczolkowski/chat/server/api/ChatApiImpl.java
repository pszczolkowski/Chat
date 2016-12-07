package pl.pszczolkowski.chat.server.api;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.chat.server.annotation.Api;
import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Message;
import pl.pszczolkowski.chat.server.model.NormalMessage;
import pl.pszczolkowski.chat.server.model.PrivateMessage;
import pl.pszczolkowski.chat.server.model.Room;
import pl.pszczolkowski.chat.server.model.SystemMessage;
import pl.pszczolkowski.chat.server.service.Security;
import pl.pszczolkowski.chat.server.service.SecurityContext;
import pl.pszczolkowski.chat.shared.Values;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.exception.NoParticipatedRoomException;
import pl.pszczolkowski.chat.shared.exception.UserDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.MessageResponse;
import pl.pszczolkowski.chat.shared.model.NormalMessageResponse;
import pl.pszczolkowski.chat.shared.model.PrivateMessageResponse;
import pl.pszczolkowski.chat.shared.model.SystemMessageResponse;

@Api
public class ChatApiImpl implements ChatApi {

    private final Security security;
    private final SecurityContext securityContext;
    
    @Autowired
    public ChatApiImpl(Security security, SecurityContext securityContext) {
        this.security = security;
        this.securityContext = securityContext;
    }

    @Override
    public List<MessageResponse> sendMessage(String text) throws NoParticipatedRoomException {
        securityContext.getSession().updateLastActivity();
        
        ActiveUser user = securityContext.getAuthenticatedUser();
        Room room = user.getParticipatedRoom();
        if (room == null) {
            throw new NoParticipatedRoomException(); 
        }
        
        text = removeSpecialCharacters(trim(text));
        room.broadcastMessage(new NormalMessage(text, user));
        
        List<Message> awaitingMessages = user.getAwaitingMessages();
        return awaitingMessages
                .stream()
                .map(this::responseOf)
                .collect(toList());
    }

    private String trim(String text) {
        text = text.trim();
        return text.substring(0, Math.min(text.length(), Values.MAX_MESSAGE_LENGTH));
    }
    
    private String removeSpecialCharacters(String text) {
        return text
            .replace("\n", "")
            .replace("\r", "");
    }

    @Override
    public List<MessageResponse> sendPrivateMessage(String text, String receiverNick) throws UserDoesNotExistException {
        securityContext.getSession().updateLastActivity();
        
        Optional<ActiveUser> receiver = security.findActiveUserByNick(receiverNick);
        
        if (!receiver.isPresent()) {
            throw new UserDoesNotExistException();
        }
        
        ActiveUser sender = securityContext.getAuthenticatedUser();
        
        PrivateMessage privateMessage = new PrivateMessage(text, sender, receiver.get());
        sender.pushMessage(privateMessage);
        receiver.get().pushMessage(privateMessage);
        
        List<Message> awaitingMessages = sender.getAwaitingMessages();
        return awaitingMessages
                .stream()
                .map(this::responseOf)
                .collect(toList());
    }

    @Override
    public List<MessageResponse> getNewMessages() {
        securityContext.getSession().updateLastActivity();
        
        ActiveUser user = securityContext.getAuthenticatedUser();
        
        try {
            List<Message> awaitingMessages = user.pollAwaitingMessages(10000);
            return awaitingMessages
                    .stream()
                    .map(this::responseOf)
                    .collect(toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return emptyList();
        }
    }
    
    private MessageResponse responseOf(Message message) {
        switch (message.getType()) {
            case NORMAL:
                NormalMessage normalMessage = (NormalMessage) message;
                return new NormalMessageResponse(
                        normalMessage.getText(), 
                        normalMessage.getUser().getNick(), 
                        normalMessage.getTime());
            case PRIVATE:
                PrivateMessage privateMessage = (PrivateMessage) message;
                return new PrivateMessageResponse(
                        privateMessage.getText(), 
                        privateMessage.getSender().getNick(), 
                        privateMessage.getReceiver().getNick(), 
                        privateMessage.getTime());
            case SYSTEM:
                SystemMessage systemMessage = (SystemMessage) message;
                return new SystemMessageResponse(
                        systemMessage.getText(), 
                        systemMessage.getTime());
            default:
                throw new IllegalStateException("unsupported message type <" + message.getType() + ">");
        }
    }

}
