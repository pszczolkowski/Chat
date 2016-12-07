package pl.pszczolkowski.chat.server.api;

import static pl.pszczolkowski.chat.shared.model.Nothing.nothing;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.chat.server.annotation.Api;
import pl.pszczolkowski.chat.server.annotation.AccessibleByAnonymousUsers;
import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Room;
import pl.pszczolkowski.chat.server.model.Session;
import pl.pszczolkowski.chat.server.model.SystemMessage;
import pl.pszczolkowski.chat.server.model.User;
import pl.pszczolkowski.chat.server.service.Chat;
import pl.pszczolkowski.chat.server.service.Security;
import pl.pszczolkowski.chat.server.service.SecurityContext;
import pl.pszczolkowski.chat.server.service.UserRepository;
import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.exception.BadCredentialsException;
import pl.pszczolkowski.chat.shared.exception.UserAlreadyExistsException;
import pl.pszczolkowski.chat.shared.model.AuthenticationResponse;
import pl.pszczolkowski.chat.shared.model.Nothing;
import pl.pszczolkowski.chat.shared.model.UserResponse;

@Api
@AccessibleByAnonymousUsers
public class AccountApiImpl implements AccountApi {

    private final Security security;
    private final SecurityContext securityContext;
    private final UserRepository userRepository;
    private final Chat chat;
    
    @Autowired
    public AccountApiImpl(Security security, SecurityContext securityContext, UserRepository userRepository,
            Chat chat) {
        this.security = security;
        this.securityContext = securityContext;
        this.userRepository = userRepository;
        this.chat = chat;
    }

    @Override
    public UserResponse register(String nick, String password) throws UserAlreadyExistsException {
        Optional<User> userWithNick = userRepository.findUserByNick(nick);
        if (userWithNick.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        
        User user = new User(nick, password);
        userRepository.addUser(user);
        
        return responseOf(user);
    }

    private UserResponse responseOf(User user) {
        return new UserResponse(user.getNick());
    }

    @Override
    public AuthenticationResponse authenticate(String nick, String password) throws BadCredentialsException {
        Optional<User> user = userRepository.findUserByCredentials(nick, password);
        if (!user.isPresent()) {
            throw new BadCredentialsException();
        }
        
        Session session = security.authenticate(user.get());
        
        return new AuthenticationResponse(user.get().getId(), user.get().getNick(), session.getId());
    }
    
    @Override
    public Nothing logout() {
        if (!securityContext.isUserAuthenticated()) {
            return nothing();
        }
        
        ActiveUser authenticatedUser = securityContext.getAuthenticatedUser();
        Room participatedRoom = authenticatedUser.getParticipatedRoom();
        
        if (participatedRoom != null) {
            chat.removeUserFromRoom(authenticatedUser, participatedRoom);
            participatedRoom.broadcastMessage(new SystemMessage(authenticatedUser.getNick() + " left the room"));
        }
        
        security.logout();
        return nothing();
    }

}
