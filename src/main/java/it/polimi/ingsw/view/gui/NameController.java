package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.application.Platform;
import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NameController extends DefaultController {

    @FXML
    public Button buttonLogin;

    @FXML
    public TextField name;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name2.png"));

        buttonLogin.setMinSize(200,114);
        buttonLogin.setMaxSize(200,114);
        buttonLogin.setPrefSize(200,114);
        buttonLogin.setMinSize(200,114);
        buttonLogin.setMaxSize(200,114);
        buttonLogin.setPrefSize(200,114);
    }

    @Override
    public void setup(){
        super.setup();
        buttonLogin.setLayoutX(gui.sceneWidth/2-100);
        name.setLayoutX(gui.sceneWidth/2-100);
        setUpTextField(name);
    }

    public void handleLoginButton(ActionEvent login) {
        trySetName();
    }

    public void nameOnAction(ActionEvent actionEvent) {
        trySetName();
    }

    public void trySetName(){
        this.gui.getClient().setUsername(name.getText());
        if(!(this.gui.getClient().getUsername().isEmpty() || this.gui.getClient().getUsername().length() > 12 || this.gui.getClient().getUsername().matches("^\\s*$")))
            this.gui.getClient().sendMessage(new ConnectionClient(this.gui.getClient().getUsername()));
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Name");
            alert.setHeaderText("The chosen one is not allowed");
            alert.setContentText("type new username (max 12 characters)");
            alert.showAndWait();
        }
    }
}
