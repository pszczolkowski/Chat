package pl.pszczolkowski.chat.client.shared;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class View<T> {

    public void onTransition(T param) {}

    public Parent getRoot() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(getViewUrl()));
            fxmlLoader.setController(getController());
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getViewUrl();
    
    protected abstract Object getController();
    
}
