package pl.pszczolkowski.chat.server.service;

import java.util.Optional;

import pl.pszczolkowski.chat.server.model.User;

public interface UserRepository {

    void addUser(User user);

    Optional<User> findUserByCredentials(String nick, String password);

    Optional<User> findUserByNick(String nick);
    
}
