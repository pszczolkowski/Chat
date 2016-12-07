package pl.pszczolkowski.chat.client.ui.signup;

import static javafx.scene.control.Alert.AlertType.WARNING;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pl.pszczolkowski.chat.client.service.ApiWrapper;
import pl.pszczolkowski.chat.client.service.Authenticator;
import pl.pszczolkowski.chat.client.service.SceneManager;
import pl.pszczolkowski.chat.client.service.Validator;
import pl.pszczolkowski.chat.client.ui.alert.AlertBox;
import pl.pszczolkowski.chat.client.ui.room.RoomSelectionView;
import pl.pszczolkowski.chat.client.ui.signin.SignInView;
import pl.pszczolkowski.chat.shared.exception.BadCredentialsException;
import pl.pszczolkowski.chat.shared.exception.UserAlreadyExistsException;

@Component
public class SignUpController {

    @Autowired
    private ApiWrapper apiWrapper;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private SignInView signInView;
    @Autowired
    private RoomSelectionView roomSelectionView;
    @Autowired
    private Validator validator;

    @FXML
    private TextField nickTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    protected void onSignUpButtonClick(ActionEvent event) {
        String nick = nickTextField.getText().trim();
        String password = passwordField.getText();

        List<String> errors = validateRegisterData(nick, password);
        if (errors.isEmpty()) {
            signUp(nick, password);
        } else {
            new AlertBox()
                .ofType(WARNING)
                .withContent(String.join("\n", errors))
                .showAndWait();
        }
    }

    private List<String> validateRegisterData(String nick, String password) {
        List<String> errors = new ArrayList<>();
        errors.addAll(validator.validateNick(nick));
        errors.addAll(validator.validatePassword(password));
        errors.addAll(validator.validatePasswordConfirmation(password, confirmPasswordField.getText()));
        return errors;
    }

    private void signUp(String nick, String password) {
        try {
            apiWrapper.getAccountApi().register(nick, password);
            authenticator.authenticateWithCredentials(nick, password);
            goToRoomSelectionView();
        } catch (UserAlreadyExistsException exception) {
            new AlertBox()
                .ofType(WARNING)
                .withContent("User with given nick already exists")
                .showAndWait();
        } catch (BadCredentialsException exception) {
            // could not happen
            throw new RuntimeException(exception);
        }
    }

    private void goToRoomSelectionView() {
        sceneManager.showScene(roomSelectionView);
    }

    @FXML
    protected void onSignInHyperlinkClick(ActionEvent event) {
        goToSignInView();
    }

    private void goToSignInView() {
        sceneManager.showScene(signInView);
    }

}
