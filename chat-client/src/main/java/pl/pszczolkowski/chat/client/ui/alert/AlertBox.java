package pl.pszczolkowski.chat.client.ui.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {

    private String content;
    private AlertType type = AlertType.INFORMATION;

    public AlertBox withContent(String content) {
        this.content = content;
        return this;
    }
    
    public AlertBox ofType(AlertType type) {
        this.type = type;
        return this;
    }
    
    public void showAndWait() {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        alert.showAndWait();
    }
    
}
