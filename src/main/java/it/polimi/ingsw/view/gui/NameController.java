package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.application.Platform;
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

        Parent ModeParent;
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            ModeParent = loader.load();
            Scene ModeScene = new Scene(ModeParent);
            ModeScene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));


            mainstage.setScene(ModeScene);
            mainstage.show();


        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }

    }


}
