package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;


public class HomeController extends DefaultController{

    @FXML
    public Button buttonPlay;

    @FXML
    public Button buttonHelp;


    @FXML
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_home.png"));

        int size = 200;
        buttonPlay.setPrefSize(size,size);
        buttonPlay.setMaxSize(size,size);
        buttonPlay.setMinSize(size,size);
        buttonHelp.setPrefSize(size,size);
        buttonHelp.setMaxSize(size,size);
        buttonHelp.setMinSize(size,size);
    }
}
