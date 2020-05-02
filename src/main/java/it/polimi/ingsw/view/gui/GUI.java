package it.polimi.ingsw.view.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {


    public GUI() {
        //setStartUpTest(this);
    }


    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    int sceneWidth = 0;
    int sceneHeight = 0;

    Scene scene;
    Parent root;
    DefaultController controller;

    @Override
    public void start(Stage stage) {

        // Responsive Design
        if (screenWidth <= 800 && screenHeight <= 600) {
            sceneWidth = 592;
            sceneHeight = 703;
        } else if (screenWidth <= 1280 && screenHeight <= 768) {
            sceneWidth = 800;
            sceneHeight = 950;
        } else if (screenWidth <= 1920 && screenHeight <= 1080) {
            sceneWidth = 950;
            sceneHeight = 800;
        } else {
            sceneWidth = 1104;
            sceneHeight = 1311;
        }

        load(stage,"/it.polimi.ingsw/view/gui/fxml/Home.fxml");
    }

    public void load(Stage stage, String file){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int limitY = 190* sceneWidth /1300;

        controller.top.setMinWidth(sceneWidth);
        controller.top.setMinHeight(limitY);
        controller.top.setMaxWidth(sceneWidth);
        controller.top.setMaxHeight(limitY);
        controller.bottom.setMinWidth(sceneWidth);
        controller.bottom.setMinHeight(limitY);
        controller.bottom.setMaxWidth(sceneWidth);
        controller.bottom.setMaxHeight(limitY);
        controller.center.setMinWidth(sceneWidth);
        //controller.center.setMinHeight(sceneHeight-2*limitY);
        controller.center.setMaxWidth(sceneWidth);
        //controller.center.setMaxHeight(sceneHeight-2*limitY);

        controller.buttonQuit.setLayoutX(sceneWidth-100);

        scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));

        controller.stage = stage;

        stage.setScene(scene);
        stage.show();
    }

}
