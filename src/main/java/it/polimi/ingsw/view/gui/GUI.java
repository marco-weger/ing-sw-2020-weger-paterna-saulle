package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class GUI extends Application implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    //int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    //int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    double sceneWidth = 0;
    double sceneHeight = 0;

    Parent root;
    DefaultController defaultcontroller;

    private Client client;
    private Stage primaryStage;

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        sceneWidth = 950;
        sceneHeight = 800;

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Santorini");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));

        client = new Client();
        client.setView(this);
        client.readParams(client);

        displayFirstWindow();
    }

    public Scene load(String file){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            root = loader.load();
            defaultcontroller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double limitY = 190* sceneWidth /1300;

        defaultcontroller.top.setMinWidth(sceneWidth);
        defaultcontroller.top.setMinHeight(limitY);
        defaultcontroller.top.setMaxWidth(sceneWidth);
        defaultcontroller.top.setMaxHeight(limitY);
        defaultcontroller.bottom.setMinWidth(sceneWidth);
        defaultcontroller.bottom.setMinHeight(limitY);
        defaultcontroller.bottom.setMaxWidth(sceneWidth);
        defaultcontroller.bottom.setMaxHeight(limitY);
        defaultcontroller.center.setMinWidth(sceneWidth);
        defaultcontroller.center.setMaxWidth(sceneWidth);

        defaultcontroller.buttonQuit.setMinSize(40,40);
        defaultcontroller.buttonQuit.setMaxSize(40,40);
        defaultcontroller.buttonQuit.setPrefSize(40,40);
        defaultcontroller.buttonHelper.setMinSize(62,40);
        defaultcontroller.buttonHelper.setMaxSize(62,40);
        defaultcontroller.buttonHelper.setPrefSize(62,40);
        defaultcontroller.buttonQuit.setLayoutY(limitY/2-20);
        defaultcontroller.buttonHelper.setLayoutY(limitY/2-20);
        defaultcontroller.buttonQuit.setLayoutX(sceneWidth-50);
        defaultcontroller.buttonHelper.setLayoutX(sceneWidth-120);

        defaultcontroller.setClient(client);

        Scene scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
        defaultcontroller.mainstage = primaryStage;
        return scene;
    }

    @Override
    public void handleMessage(CheckMoveServer message) {

    }

    @Override
    public void handleMessage(CheckBuildServer message) {

    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {

    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {

    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {

    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void handleMessage(NameRequestServer message) {
       // Platform.runLater(() -> {
       //     primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
      //      primaryStage.show();
      //  });
    }



    @Override
    public void handleMessage(LobbyServer message) {

    }

    @Override
    public void handleMessage(ModeRequestServer message) {
        //load("/it.polimi.ingsw/view/gui/fxml/Name.fxml");
    }

    @Override
    public void handleMessage(BuiltServer message) {

    }

    @Override
    public void handleMessage(MovedServer message) {

    }

    @Override
    public void handleMessage(PingServer message) {

    }

    @Override
    public void handleMessage(TimeOutServer message) {

    }

    @Override
    public void handleMessage(ReConnectionServer message) {

    }

    @Override
    public void displayFirstWindow() {
        Scene scene = load("/it.polimi.ingsw/view/gui/fxml/Home.fxml");
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    @Override
    public void statusHandler(CurrentStatusServer message) {

    }

    @Override
    public void close(boolean isError) {

    }
}
