package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.ViewInterface;
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
import java.util.concurrent.CountDownLatch;

public class GUI extends Application implements ViewInterface {

    //int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    //int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    double sceneWidth = 0;
    double sceneHeight = 0;

    Scene scene;
    Parent root;
    DefaultController controller;

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static GUI gui;
    private Client client;

    public static GUI waitForGUI(Client client) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gui;
    }

    public static void setGUI(GUI gui) {
        GUI.gui = gui;
        latch.countDown();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        sceneWidth = 950;
        sceneHeight = 800;
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

        double limitY = 190* sceneWidth /1300;

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

        controller.setClient(client);

        scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

        stage = new Stage();
        stage.setTitle("Santorini");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
        controller.stage = stage;

        stage.setScene(scene);
        stage.show();
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

    }

    @Override
    public void handleMessage(LobbyServer message) {

    }

    @Override
    public void handleMessage(ModeRequestServer message) {

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

    }

    @Override
    public void statusHandler(CurrentStatusServer message) {

    }

    @Override
    public void close() {

    }
}
