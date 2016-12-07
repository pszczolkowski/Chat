package pl.pszczolkowski.chat.shared.api;

import java.util.List;

import pl.pszczolkowski.chat.shared.exception.NoParticipatedRoomException;
import pl.pszczolkowski.chat.shared.exception.UserDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.MessageResponse;

public interface ChatApi {

    List<MessageResponse> sendMessage(String text) throws NoParticipatedRoomException;
    
    List<MessageResponse> getNewMessages();

    List<MessageResponse> sendPrivateMessage(String text, String receiverNick) throws UserDoesNotExistException;
    
}
