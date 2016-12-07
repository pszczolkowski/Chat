package pl.pszczolkowski.chat.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Validator {

    public List<String> validateNick(String nick) {
        List<String> errors = new ArrayList<>();
        
        if (nick.length() < 3) {
            errors.add("Nick has to be at least 3 characters long");
        } else if (nick.length() > 20) {
            errors.add("Nick has to be maximum 20 characters long");
        }
        if (!nick.matches("^[a-zA-Z0-9 ]+$")) {
            errors.add("Nick contains invalid characters");
        }
        
        return errors;
    }
    
    public List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password.length() < 5) {
            errors.add("Password has to be at least 5 characters long");
        } else if (password.length() > 30) {
            errors.add("Password has to be maximum 30 characters long");
        }
        
        return errors;
    }

    public List<String> validatePasswordConfirmation(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList("Passwords don't match");
        }
    }

    public List<String> validateRoomName(String roomName) {
        List<String> errors = new ArrayList<>();
        
        if (roomName.length() < 3) {
            errors.add("Name has to be at least 3 characters long");
        } else if (roomName.length() > 20) {
            errors.add("Name has to be maximum 20 characters long");
        }
        if (!roomName.matches("^[a-zA-Z0-9 ]+$")) {
            errors.add("Name contains invalid characters");
        }
        
        return errors;
    }
    
}
