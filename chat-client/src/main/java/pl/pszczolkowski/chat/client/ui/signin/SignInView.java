package pl.pszczolkowski.chat.client.ui.signin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.client.shared.View;

@Component
public class SignInView extends View<Void> {

    @Autowired
    private SignInController controller;

    @Override
    protected String getViewUrl() {
        return "view/signIn.fxml";
    }

    @Override
    protected Object getController() {
        return controller;
    }
    
}
