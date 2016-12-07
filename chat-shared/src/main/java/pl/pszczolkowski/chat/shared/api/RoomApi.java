package pl.pszczolkowski.chat.shared.api;

import java.util.List;
import java.util.UUID;

import pl.pszczolkowski.chat.shared.exception.RoomAlreadyExistException;
import pl.pszczolkowski.chat.shared.exception.RoomDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.Nothing;
import pl.pszczolkowski.chat.shared.model.RoomResponse;


public interface RoomApi {

    RoomResponse create(String name) throws RoomAlreadyExistException;
    
    List<RoomResponse> getAll();
    
    Nothing join(UUID roomId) throws RoomDoesNotExistException;
    
    Nothing leave();
    
}
