package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ModeChoseClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    public Button buttonIIplayers;
    public Button buttonIIIplayers;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode.png"));

        int sizex = 250;
        int sizey = 143;
        buttonIIplayers.setPrefSize(sizex, sizey);
        buttonIIplayers.setMaxSize(sizex, sizey);
        buttonIIplayers.setMinSize(sizex, sizey);
        buttonIIIplayers.setPrefSize(sizex, sizey);
        buttonIIIplayers.setMaxSize(sizex, sizey);
        buttonIIIplayers.setMinSize(sizex, sizey);
    }

    public void handler2playersButton(ActionEvent actionEvent) {
        client.sendMessage(new ModeChoseClient(client.getUsername(),2));
    }


    public void handler3playersButton(ActionEvent actionEvent) {
        client.sendMessage(new ModeChoseClient(client.getUsername(),3));
    }
}
