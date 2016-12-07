package pl.pszczolkowski.chat.server.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Room;

public interface Chat {

    void addRoom(Room room);

    boolean isNameAlreadyInUsage(String name);

    List<Room> getRooms();

    Optional<Room> findRoomById(UUID roomId);

    void addUserToRoom(ActiveUser user, Room room);
    
    void removeUserFromRoom(ActiveUser user, Room room);

    void removeInactiveRooms();
    
}
