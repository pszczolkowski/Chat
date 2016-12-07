package pl.pszczolkowski.chat.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.model.User;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    
    @Override
    public synchronized void addUser(User user) {
        users.add(user);
    }

    @Override
    public synchronized Optional<User> findUserByCredentials(String nick, String password) {
        for (User user : users) {
            if (user.getNick().equalsIgnoreCase(nick) && user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }

    @Override
    public synchronized Optional<User> findUserByNick(String nick) {
        for (User user : users) {
            if (user.getNick().equalsIgnoreCase(nick)) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }

}
