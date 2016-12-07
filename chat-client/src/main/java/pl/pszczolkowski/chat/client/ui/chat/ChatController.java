package pl.pszczolkowski.chat.client.ui.chat;

import static javafx.scene.control.Alert.AlertType.WARNING;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.pszczolkowski.chat.client.service.ApiWrapper;
import pl.pszczolkowski.chat.client.service.SceneManager;
import pl.pszczolkowski.chat.client.ui.alert.AlertBox;
import pl.pszczolkowski.chat.client.ui.room.RoomSelectionView;
import pl.pszczolkowski.chat.shared.Values;
import pl.pszczolkowski.chat.shared.exception.NoParticipatedRoomException;
import pl.pszczolkowski.chat.shared.exception.UserDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.MessageResponse;
import pl.pszczolkowski.chat.shared.model.NormalMessageResponse;
import pl.pszczolkowski.chat.shared.model.PrivateMessageResponse;
import pl.pszczolkowski.chat.shared.model.RoomResponse;

@Component
public class ChatController implements Initializable {

    @Autowired
    private ApiWrapper apiWrapper;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private RoomSelectionView roomSelectionView;
    
    @FXML
    private Label roomNameLabel;
    @FXML
    private TextField messageTextField;
    @FXML
    private VBox messagesWrapper;
    @FXML
    private ScrollPane messagesScrollPane;
    
    private Timer timer;
    private String lastMessageText = "";

    public void onTransition(RoomResponse room) {
        roomNameLabel.setText(room.getName());
        
        runThreadListeningForNewMessages();
    }

    private void runThreadListeningForNewMessages() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<MessageResponse> newMessages = apiWrapper.getChatApi().getNewMessages();
                Platform.runLater(() -> {
                    addMessages(newMessages);
                });
            }
        }, 0, 1000);
    }
    
    @FXML
    protected void onSendButtonClick(ActionEvent event) {
        String messageText = messageTextField.getText().trim();
        String privateMessageReceiverNick = null;
        
        if (messageText.startsWith("@")) {
            String[] messageParts = messageText.split("\\s+");
            privateMessageReceiverNick = messageParts[0].substring(1).replace('_', ' ');
            messageText = String.join(" ", Arrays.asList(messageParts).subList(1, messageParts.length));
        }
        
        if (messageText.isEmpty()) {
            return;
        }
        
        if (messageText.equalsIgnoreCase(lastMessageText)) {
            new AlertBox()
                .ofType(WARNING)
                .withContent("Don't repeat your messages")
                .showAndWait();
            return;
        }
        
        if (messageText.length() > Values.MAX_MESSAGE_LENGTH) {
            messageText = messageText.substring(0, Values.MAX_MESSAGE_LENGTH);
            new AlertBox()
                .withContent("Your message was too long so it was trimmed")
                .showAndWait();
        }
        
        if (privateMessageReceiverNick != null) {
            sendPrivateMessage(privateMessageReceiverNick, messageText);
        } else {
            sendMessage(messageText);
        }
    }
    
    private void sendPrivateMessage(String nick, String messageText) {
        try {
            List<MessageResponse> newMessages = apiWrapper.getChatApi().sendPrivateMessage(messageText, nick);
            addMessages(newMessages);
            
            lastMessageText = messageText;
            messageTextField.clear();
        } catch (UserDoesNotExistException e) {
            new AlertBox()
                .withContent("There's no logged user with nick \"" + nick + "\"")
                .showAndWait();
        }
    }

    private void sendMessage(String messageText) {
        try {
            List<MessageResponse> newMessages = apiWrapper.getChatApi().sendMessage(messageText);
            addMessages(newMessages);
            
            lastMessageText = messageText;
            messageTextField.clear();
        } catch (NoParticipatedRoomException e) {
            throw new RuntimeException(e);
        }
    }
    
    void addMessages(List<MessageResponse> messages) {
        for (MessageResponse message : messages) {
            messagesWrapper.getChildren().add(buildMessage(message));
        }
    }
    
    private TextFlow buildMessage(MessageResponse message) {
        switch (message.getType()) {
            case NORMAL:
                return buildNormalMessage(message);
            case PRIVATE:
                return buildPrivateMessage(message);
            case SYSTEM:
                return buildSystmMessage(message);
            default:
                throw new IllegalArgumentException("unsupported message type <" + message.getType() + ">");
        }
    }
   
    private TextFlow buildNormalMessage(MessageResponse message) {
        TextFlow textFlow = new TextFlow();
        
        NormalMessageResponse normalMessage = (NormalMessageResponse) message;
        Text text = new Text("<" + normalMessage.getNick() + "> ");
        text.setFill(new Color(0.2, 0.2, 1, 1));
//        text.wrappingWidthProperty().bind(messagesScrollPane.widthProperty());
        textFlow.getChildren().add(text);
        textFlow.getChildren().add(new Text(message.getText()));
        return textFlow;
    }
    
    private TextFlow buildPrivateMessage(MessageResponse message) {
        TextFlow textFlow = new TextFlow();
        PrivateMessageResponse privateMessage = (PrivateMessageResponse) message;
        
        Text text = new Text("<" + privateMessage.getSenderNick() + " -> " + privateMessage.getReceiverNick() + "> ");
        text.setFill(Color.BLUEVIOLET);
        textFlow.getChildren().add(text);
        Text messageText = new Text(message.getText());
        messageText.setFill(Color.VIOLET);
        textFlow.getChildren().add(messageText);
        return textFlow;
    }

    private TextFlow buildSystmMessage(MessageResponse message) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text("* " + message.getText());
        text.setFill(new Color(0.2, 0.7, 0.2, 1));
        textFlow.getChildren().add(text);
        return textFlow;
    }

    @FXML
    protected void onLeaveButtonClick(ActionEvent event) {
        timer.cancel();
        timer.purge();
        apiWrapper.getRoomApi().leave();
        sceneManager.showScene(roomSelectionView);
    }
    
    @PreDestroy
    public void cleanUp() throws Exception {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        messagesScrollPane.vvalueProperty().bind(messagesWrapper.heightProperty());        
    }

}
