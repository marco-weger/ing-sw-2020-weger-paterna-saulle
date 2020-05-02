package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    @FXML
    public Button IIPlayers;

    @FXML
    public Button IIIPlayers;

    @FXML
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode.png"));

        int size = 200;
        /*
        buttonPlay.setPrefSize(size,size);
        buttonPlay.setMaxSize(size,size);
        buttonPlay.setMinSize(size,size);
        buttonHelp.setPrefSize(size,size);
        buttonHelp.setMaxSize(size,size);
        buttonHelp.setMinSize(size,size);
        */
    }
}
