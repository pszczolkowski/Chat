package pl.pszczolkowski.chat.client.ui.menu;

import static pl.pszczolkowski.chat.client.shared.Protocol.BURLAP;
import static pl.pszczolkowski.chat.client.shared.Protocol.HESSIAN;
import static pl.pszczolkowski.chat.client.shared.Protocol.XML_RPC;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioMenuItem;
import pl.pszczolkowski.chat.client.service.ApiWrapper;

@Component
public class MenuController implements Initializable {
    
    @Autowired
    private ApiWrapper apiWrapper;
    
    @FXML
    private RadioMenuItem xmlRpcMenuItem;;
    @FXML
    private RadioMenuItem hessianMenuItem;;
    @FXML
    private RadioMenuItem burlapMenuItem;;
    
    @FXML
    protected void onXmlRpcItemClick(ActionEvent event) {
        apiWrapper.setCurrentProtocol(XML_RPC);
    }
    
    @FXML
    protected void onHessianItemClick(ActionEvent event) {
        apiWrapper.setCurrentProtocol(HESSIAN);
    }
    
    @FXML
    protected void onBurlapcItemClick(ActionEvent event) {
        apiWrapper.setCurrentProtocol(BURLAP);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        switch (apiWrapper.getCurrentProtocol()) {
            case XML_RPC:
                xmlRpcMenuItem.setSelected(true);
                break;
            case HESSIAN:
                hessianMenuItem.setSelected(true);
                break;
            case BURLAP:
                burlapMenuItem.setSelected(true);
                break;
            default:
                throw new IllegalStateException("unsupported protocol: " + apiWrapper.getCurrentProtocol());
        }
    }

}
