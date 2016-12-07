package pl.pszczolkowski.chat.client.ui.room;

import static javafx.scene.control.Alert.AlertType.WARNING;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.pszczolkowski.chat.client.service.ApiWrapper;
import pl.pszczolkowski.chat.client.service.SceneManager;
import pl.pszczolkowski.chat.client.service.Validator;
import pl.pszczolkowski.chat.client.ui.alert.AlertBox;
import pl.pszczolkowski.chat.client.ui.chat.ChatView;
import pl.pszczolkowski.chat.shared.exception.RoomAlreadyExistException;
import pl.pszczolkowski.chat.shared.exception.RoomDoesNotExistException;
import pl.pszczolkowski.chat.shared.model.RoomResponse;

@Component
public class RoomSelectionController implements Initializable {
    
    @Autowired
    private ApiWrapper apiWrapper;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private ChatView chatView;
    @Autowired
    private Validator validator;
    
    @FXML
    private ListView<RoomResponse> roomsListView;
    @FXML
    private Button joinButton;
    @FXML
    private TextField roomNameTextField;
    
    private final ObservableList<RoomResponse> rooms = FXCollections.observableArrayList();
    private Timer timer;
    
    public void onTransition() {
        refreshRooms();
        runThreadRefreshingRoomList();
    }
    
    private void runThreadRefreshingRoomList() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<RoomResponse> availableRooms = apiWrapper.getRoomApi().getAll();
                Platform.runLater(() -> {
                    displayRooms(availableRooms);
                });
            }
        }, 0, 5000);
    }
    
    private void displayRooms(List<RoomResponse> availableRooms) {
        int selectedIndex = -1;
        if (!roomsListView.getSelectionModel().isEmpty()) {
            selectedIndex = roomsListView.getSelectionModel().getSelectedIndex();
        }
        
        rooms.clear();
        rooms.addAll(availableRooms);
        
        if (selectedIndex >= 0) {
            roomsListView.getSelectionModel().select(selectedIndex);
        }
    }

    @FXML
    protected void onRefreshButtonClick(ActionEvent event) {
        refreshRooms();
    }
    
    void refreshRooms() {
        List<RoomResponse> availableRooms = apiWrapper.getRoomApi().getAll();
        displayRooms(availableRooms);
    }
    
    @FXML
    protected void onJoinButtonClick(ActionEvent event) {
        RoomResponse room = roomsListView.getSelectionModel().getSelectedItem();
        joinRoom(room);
    }
    
    private void joinRoom(RoomResponse room) {
        try {
            apiWrapper.getRoomApi().join(room.getId());
            goToChatView(room);
        } catch (RoomDoesNotExistException exception) {
            informAboutNotExistingRoom(room);
        }
    }

    private void goToChatView(RoomResponse room) {
        timer.cancel();
        timer.purge();
        sceneManager.showScene(chatView, room);
    }
    
    private void informAboutNotExistingRoom(RoomResponse room) {
        new AlertBox()
            .ofType(WARNING)
            .withContent("Room \"" + room.getName() + "\" does not exist")
            .showAndWait();
    }
    
    @FXML
    protected void onCreateButtonClick(ActionEvent event) {
        String roomName = roomNameTextField.getText().trim();
        
        List<String> errors = validator.validateRoomName(roomName);
        if (errors.isEmpty()) {
            createRoom(roomName);
        } else {
            new AlertBox()
            .ofType(WARNING)
            .withContent(String.join("\n", errors))
            .showAndWait();
        }
    }
    
    private void createRoom(String roomName) {
        if (roomExists(roomName)) {
            informAboutExistingRoom(roomName);
            return;
        }
        
        try {
            RoomResponse createdRoom = apiWrapper.getRoomApi().create(roomName);
            joinRoom(createdRoom);
        } catch (RoomAlreadyExistException e) {
            informAboutExistingRoom(roomName);
        }
    }
    
    private boolean roomExists(String roomName) {
        for (RoomResponse room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return true;
            }
        }
        
        return false;
    }

    private void informAboutExistingRoom(String roomName) {
        new AlertBox()
            .ofType(WARNING)
            .withContent("Room \"" + roomName + "\" already exists")
            .showAndWait();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeRoomsList();
        joinButton.disableProperty().bind(Bindings.size(roomsListView.getSelectionModel().getSelectedIndices()).isEqualTo(0));
    }

    private void initializeRoomsList() {
        roomsListView.setItems(rooms);
        
        roomsListView.setCellFactory(param -> new ListCell<RoomResponse>() {
            @Override
            public void updateItem(RoomResponse item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(new Label(item.getName()));
                }
            }
        });
    }
    
    @PreDestroy
    public void cleanUp() throws Exception {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

}
