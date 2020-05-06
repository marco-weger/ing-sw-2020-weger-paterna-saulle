package it.polimi.ingsw.view.gui;

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

        int sizex = 130;
        int sizex2 = 230;
        int sizey = 100;
        buttonIIplayers.setPrefSize(sizex, sizey);
        buttonIIplayers.setMaxSize(sizex, sizey);
        buttonIIplayers.setMinSize(sizex, sizey);
        buttonIIIplayers.setPrefSize(sizex2, sizey);
        buttonIIIplayers.setMaxSize(sizex2, sizey);
        buttonIIIplayers.setMinSize(sizex2, sizey);
    }
}
