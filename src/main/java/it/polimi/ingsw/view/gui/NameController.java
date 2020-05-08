package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.util.logging.Logger;

public class NameController extends DefaultController {


    private static final Logger LOGGER = Logger.getLogger(NameController.class.getName());
    public Button buttonLogin;
    public TextField insertname;
    public String name;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name.png"));


        int sizex = 230;
        int sizey = 100;
        buttonLogin.setPrefSize(sizex, sizey);
        //buttonLogin.setMaxSize(sizex, sizey);
        //buttonLogin.setMinSize(sizex, sizey);
        buttonLogin.setLayoutX(150);
        buttonLogin.setLayoutY(86);

    }

    public void handleLoginButton(ActionEvent login) {
        do {
            if (insertname != null) {
                if (insertname.getText().length() > 12 || insertname.getText().matches("^\\s*$")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Name");
                    alert.setHeaderText("The chosen one is not allowed");
                    alert.setContentText("type new username (max 12 characters)");
                    alert.showAndWait();
                }
                else {
                    name = insertname.getText();
                    client.setUsername(name);
                    client.sendMessage(new ConnectionClient(name));
                }
            }
        } while (client.getUsername().isEmpty());

    }
}
