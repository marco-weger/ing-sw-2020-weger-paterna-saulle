package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class WelcomeScreenController {

    @FXML
    public Button PlayButton;

    @FXML
    public Button HelpButton;

    @FXML
    public Button QuitButton;

    public void HandleHelpButton(javafx.event.ActionEvent actionEvent) {
        Parent root;
    try{
        root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/Rules.fxml"));


        Stage stage = new Stage();
        stage.setTitle("Santorini_Rules_eng");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }
    catch (IOException e) {
            e.printStackTrace();

    }
    }

}
