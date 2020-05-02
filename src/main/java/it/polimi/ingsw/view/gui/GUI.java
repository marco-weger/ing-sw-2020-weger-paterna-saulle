package it.polimi.ingsw.view.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {


    public GUI() {
        //setStartUpTest(this);
    }

    @Override
    public void start(Stage stage) {

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/WelcomeScreen.fxml"));
            root = loader.load(); //FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/WelcomeScreen.fxml"));
            WelcomeScreenController fXMLDocumentController = loader.getController();
            fXMLDocumentController.setGetHostController(getHostServices());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = new Stage();
        Scene scene = new Scene(Objects.requireNonNull(root));
        Image image = new Image("/it.polimi.ingsw/view/gui/image/pointer.png");  //pass in the image path
        scene.setCursor(new ImageCursor(image));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/image/icon.png"));
        stage.show();
    }

}
