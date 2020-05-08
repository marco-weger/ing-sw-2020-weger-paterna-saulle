package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

        if(insertname != null){
            name = insertname.getText();
            client.setUsername(name);
            client.sendMessage(new ConnectionClient(name));
        }

        Platform.runLater(() -> {
            getCurrentView().getPrimaryStage().setScene(getCurrentView().load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            getCurrentView().getPrimaryStage().show();
        });
    }


}
