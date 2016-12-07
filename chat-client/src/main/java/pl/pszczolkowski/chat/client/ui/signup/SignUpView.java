package pl.pszczolkowski.chat.client.ui.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.client.shared.View;

@Component
public class SignUpView extends View<Void> {

    @Autowired
    private SignUpController controller;

    @Override
    protected String getViewUrl() {
        return "view/signUp.fxml";
    }

    @Override
    protected Object getController() {
        return controller;
    }

}
