package pl.pszczolkowski.chat.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Room;
import pl.pszczolkowski.chat.shared.Values;

@Service
public class ChatImpl implements Chat {

    private final List<Room> rooms = new ArrayList<>();
    
    @Override
    public synchronized void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public synchronized boolean isNameAlreadyInUsage(String name) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public synchronized List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    @Override
    public synchronized Optional<Room> findRoomById(UUID roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return Optional.of(room);
            }
        }
        
        return Optional.empty();
    }

    @Override
    public synchronized void addUserToRoom(ActiveUser user, Room room) {
        Room participatedRoom = user.getParticipatedRoom();
        if (participatedRoom != null) {
            participatedRoom.removeUser(user);
        }
        
        room.addUser(user);
        user.setParticipatedRoom(room);
    }

    @Override
    public synchronized void removeUserFromRoom(ActiveUser user, Room room) {
        if (room != null) {
            room.removeUser(user);
            user.leaveRoom();
        }
    }

    @Override
    public synchronized void removeInactiveRooms() {
        for (Iterator<Room> iterator = rooms.iterator(); iterator.hasNext();) {
            Room room = iterator.next();
            if (isInactive(room)) {
                iterator.remove();
            }
        }
    }

    private boolean isInactive(Room room) {
        return room.getLastActivityTime()
                .plusSeconds(Values.EMPTY_ROOM_ACTIVITY_TIME_IN_SECONDS)
                .isBefore(LocalDateTime.now());
    }
    
}
