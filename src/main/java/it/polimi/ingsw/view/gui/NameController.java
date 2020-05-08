package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


import java.io.IOException;
import java.util.logging.Level;

public class NameController extends DefaultController {

    @FXML
    public Button buttonLogin;

    @FXML
    public TextField name;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name.png"));

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
    }

    public void handleLoginButton(ActionEvent login) {
        this.gui.getClient().setUsername(name.getText());
        if(!(this.gui.getClient().getUsername().isEmpty() || this.gui.getClient().getUsername().length() > 12 || this.gui.getClient().getUsername().matches("^\\s*$")))
            this.gui.getClient().sendMessage(new ConnectionClient(this.gui.getClient().getUsername()));
    }

}
