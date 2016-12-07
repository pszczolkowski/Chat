package pl.pszczolkowski.chat.server.api;

import static java.util.stream.Collectors.toList;
import static pl.pszczolkowski.chat.shared.model.Nothing.nothing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.chat.server.annotation.Api;
import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Room;
import pl.pszczolkowski.chat.server.model.SystemMessage;
import pl.pszczolkowski.chat.server.service.Chat;
import pl.pszczolkowski.chat.server.service.SecurityContext;
import pl.pszczolkowski.chat.shared.api.RoomApi;
import pl.pszczolkowski.chat.shared.exception.RoomAlreadyExistException;
import pl.pszczolkowski.chat.shared.exception.RoomDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.Nothing;
import pl.pszczolkowski.chat.shared.model.RoomResponse;

@Api
public class RoomApiImpl implements RoomApi {

    private final Chat chat;
    private final SecurityContext securityContext;
    
    @Autowired
    public RoomApiImpl(Chat chat, SecurityContext securityContext) {
        this.chat = chat;
        this.securityContext = securityContext;
    }

    @Override
    public RoomResponse create(String name) throws RoomAlreadyExistException {
        if (chat.isNameAlreadyInUsage(name)) {
            throw new RoomAlreadyExistException();
        }
        
        Room room = new Room(name);
        chat.addRoom(room);
        
        return responseOf(room);
    }

    private RoomResponse responseOf(Room room) {
        return new RoomResponse(room.getId(), room.getName());
    }

    @Override
    public List<RoomResponse> getAll() {
        securityContext.getSession().updateLastActivity();
        
        return chat.getRooms()
            .stream()
            .map(this::responseOf)
            .collect(toList());
    }

    @Override
    public Nothing join(UUID roomId) throws RoomDoesNotExistException {
        Optional<Room> room = chat.findRoomById(roomId);
        if (!room.isPresent()) {
            throw new RoomDoesNotExistException();
        }
        
        ActiveUser user = securityContext.getAuthenticatedUser();
        Room participatedRoom = user.getParticipatedRoom();
        if (participatedRoom != null) {
            chat.removeUserFromRoom(user, participatedRoom);
            participatedRoom.broadcastMessage(new SystemMessage(user.getNick() + " left the room"));
        }
        
        room.get().broadcastMessage(new SystemMessage(user.getNick() + " joined the room"));
        chat.addUserToRoom(user, room.get());
        
        return nothing();
    }

    @Override
    public Nothing leave() {
        ActiveUser user = securityContext.getAuthenticatedUser();
        Room participatedRoom = user.getParticipatedRoom();
        if (participatedRoom != null) {
            chat.removeUserFromRoom(user, participatedRoom);
            participatedRoom.broadcastMessage(new SystemMessage(user.getNick() + " left the room"));
        }
        
        return nothing();
    }

}
