package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HomeController extends DefaultController{

    @FXML
    public Button buttonPlay;

    @FXML
    public Button buttonHelp;


    @FXML
    @Override
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

        buttonHelper.setVisible(false);
    }

    public void handleHelpButton(javafx.event.ActionEvent actionEvent) {
        showHelper();
    }


    public void handlePlayButton(javafx.event.ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(!client.connect()) getCurrentView().getPrimaryStage().setScene(getCurrentView().load("/it.polimi.ingsw/view/gui/fxml/Server.fxml"));
            else getCurrentView().getPrimaryStage().setScene(getCurrentView().load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
            getCurrentView().getPrimaryStage().show();
        });
    }



}
