package pl.pszczolkowski.chat.client.ui.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.client.shared.View;

@Component
public class RoomSelectionView extends View<Void> {

    @Autowired
    private RoomSelectionController controller;
    
    @Override
    protected String getViewUrl() {
        return "view/roomSelection.fxml";
    }

    @Override
    protected Object getController() {
        return controller;
    }
    
    @Override
    public void onTransition(Void dummy) {
        controller.onTransition();
    }

}
