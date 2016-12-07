package pl.pszczolkowski.chat.server.service;

import java.util.Optional;
import java.util.UUID;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Session;
import pl.pszczolkowski.chat.server.model.User;

/**
 * Application security system. Manages active user sessions 
 */
public interface Security {

    boolean authenticateUserUsingSessionId(UUID sessionId);

    Session authenticate(User user);

    Optional<ActiveUser> findActiveUserByNick(String receiverNick);

    void removeExpiredSessions();

    void logout();

}
