package pl.pszczolkowski.chat.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.pszczolkowski.chat.client.shared.View;
import pl.pszczolkowski.chat.client.ui.menu.Menu;

@Service
public class SceneManager {

    @Autowired
    private Menu menuView;
    
    private Stage primaryStage;
    
    public void setStage(Stage stage) {
        primaryStage = stage;
    }
    
    public <T> void showScene(View<T> view) {
        showScene(view, null);
    }
    
    public <T> void showScene(View<T> view, T param) {
        primaryStage.setScene(buildScene(view));
        primaryStage.show();
        
        view.onTransition(param);
    }
    
    private <T> Scene buildScene(View<T> view) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuView.getRoot());
        borderPane.setCenter(view.getRoot());
        
        return new Scene(borderPane);
    }
    
}
