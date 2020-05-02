package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    HostServices hostServices ;

    public void setGetHostController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }

    public void HandleHelpButton(javafx.event.ActionEvent actionEvent) {
        /*
        Parent root;
    try{
        root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/Rules.fxml"));


        Stage stage = new Stage();
        stage.setTitle("Santorini_Rules_eng");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }
    catch (IOException e) {
            e.printStackTrace();*/



        hostServices.showDocument(getClass()
                        .getResource("/it.polimi.ingsw/view/gui/santorini_rules_en.pdf").toString());


    }


    public void quitHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) QuitButton.getScene().getWindow();
        new Alert(Alert.AlertType.ERROR, "SHOW IF YOU ARE SURE !").showAndWait();

        // do what you have to do
        stage.close();
    }
}
