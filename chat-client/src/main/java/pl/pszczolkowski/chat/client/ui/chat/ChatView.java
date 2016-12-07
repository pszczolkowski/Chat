package pl.pszczolkowski.chat.client.ui.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.client.shared.View;
import pl.pszczolkowski.chat.shared.model.RoomResponse;

@Component
public class ChatView extends View<RoomResponse> {

    @Autowired
    private ChatController controller;
    
    @Override
    protected String getViewUrl() {
        return "view/chat.fxml";
    }

    @Override
    protected Object getController() {
        return controller;
    }
    
    @Override
    public void onTransition(RoomResponse roomResponse) {
        controller.onTransition(roomResponse);
    }

}
