package pl.pszczolkowski.chat.shared.api;

import pl.pszczolkowski.chat.shared.exception.BadCredentialsException;
import pl.pszczolkowski.chat.shared.exception.UserAlreadyExistsException;
import pl.pszczolkowski.chat.shared.model.AuthenticationResponse;
import pl.pszczolkowski.chat.shared.model.Nothing;
import pl.pszczolkowski.chat.shared.model.UserResponse;

public interface AccountApi {

    UserResponse register(String nick, String password) throws UserAlreadyExistsException;
    
    AuthenticationResponse authenticate(String nick, String password) throws BadCredentialsException;

    Nothing logout();
    
}
