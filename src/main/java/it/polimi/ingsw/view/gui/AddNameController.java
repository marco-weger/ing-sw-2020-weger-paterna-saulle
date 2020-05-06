package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class AddNameController extends DefaultController{


    public Button buttonLogin;
    public TextField insertname;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name.png"));


        int sizex = 230;
        int sizey = 100;
        buttonLogin.setPrefSize(sizex,sizey);
        buttonLogin.setMaxSize(sizex,sizey);
        buttonLogin.setMinSize(sizex,sizey);
    }

    public void handleLoginButton(ActionEvent actionEvent) {
    }
}
