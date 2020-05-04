package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    @FXML
    public Button TwoPlayers;

    @FXML
    public Button ThreePlayers;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode.png"));
    }
}
