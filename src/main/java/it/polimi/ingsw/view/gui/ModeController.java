package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    @FXML
    public Button buttonIIplayers;

    @FXML
    public Button buttonIIIplayers;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode.png"));

        buttonIIplayers.setMinSize(200,114);
        buttonIIplayers.setMaxSize(200,114);
        buttonIIplayers.setPrefSize(200,114);
        buttonIIIplayers.setMinSize(200,114);
        buttonIIIplayers.setMaxSize(200,114);
        buttonIIIplayers.setPrefSize(200,114);
    }
}
