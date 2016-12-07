package pl.pszczolkowski.chat.client.ui.signin;

import static javafx.scene.control.Alert.AlertType.WARNING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.pszczolkowski.chat.client.service.Authenticator;
import pl.pszczolkowski.chat.client.service.SceneManager;
import pl.pszczolkowski.chat.client.ui.alert.AlertBox;
import pl.pszczolkowski.chat.client.ui.room.RoomSelectionView;
import pl.pszczolkowski.chat.client.ui.signup.SignUpView;
import pl.pszczolkowski.chat.shared.exception.BadCredentialsException;

@Component
public class SignInController {

    @Autowired
    private Authenticator authenticator;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private RoomSelectionView roomSelectionView;
    @Autowired
    private SignUpView signUpView;
    
    @FXML
    private TextField nickTextField;
    @FXML
    private TextField passwordField;
    
    @FXML
    protected void onSignInButtonClick(ActionEvent event) {
        String nick = nickTextField.getText();
        String password = passwordField.getText(); 
        
        try {
            authenticator.authenticateWithCredentials(nick, password);
            goToRoomSelectionScene();
        } catch (BadCredentialsException exception) {
            new AlertBox()
                .ofType(WARNING)
                .withContent("Invalid nick or password")
                .showAndWait();
        }
    }
    
    private void goToRoomSelectionScene() {
        sceneManager.showScene(roomSelectionView);
    }
    
    @FXML
    protected void onSignUpHyperlinkClick(ActionEvent event) {
        goToSignUpScene();
    }

    private void goToSignUpScene() {
        sceneManager.showScene(signUpView);
    }

}
