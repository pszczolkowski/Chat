package pl.pszczolkowski.chat.client.ui.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.client.shared.View;

@Component
public class Menu extends View<Void> {

    @Autowired
    private MenuController controller;

    @Override
    protected String getViewUrl() {
        return "view/menu.fxml";
    }

    @Override
    protected Object getController() {
        return controller;
    }
    
}
