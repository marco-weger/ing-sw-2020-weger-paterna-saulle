package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;


public class WelcomeScreenController {
    public Button PlayButton;
    public Button HelpButton;


    public void HandleHelpButton(javafx.event.ActionEvent actionEvent) {
        Parent root;
    try{
        root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/Rules.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Santorini_Rules_eng");
        stage.setScene(new Scene(root, 450, 450));
        stage.setMaxWidth(1315);
        stage.setMaxHeight(800);
        stage.show();
    }
    catch (IOException e) {
            e.printStackTrace();

    }
    }

}
