package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TextFormatting;
import it.polimi.ingsw.view.ViewInterface;
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
import java.util.concurrent.CountDownLatch;

public class GUI extends Application implements ViewInterface {

    //int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    //int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    double sceneWidth = 0;
    double sceneHeight = 0;

    Scene scene;
    Parent root;
    DefaultController defaultcontroller;

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

        defaultcontroller.buttonQuit.setLayoutX(sceneWidth-100);

        defaultcontroller.setClient(client);

        scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

        stage = new Stage();
        stage.setTitle("Santorini");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
        defaultcontroller.mainstage = stage;

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
        /* do{
            if(message.isFirstTime){
                print(COLOR_CPU + "Type your username (max 12 characters) " + TextFormatting.input());
            }
            else{
                clearLine();
                print(COLOR_CPU + "The chosen one is not allowed, type new username (max 12 characters) " + TextFormatting.input());
            }
            String username = read();
            print(COLOR_CPU + "Validating username... " + TextFormatting.RESET);
            this.client.setUsername(username);
            message.isFirstTime = false;
        }while (this.client.getUsername().isEmpty() || this.client.getUsername().length() > 12 || this.client.getUsername().matches("^\\s*$") && client.getContinueReading());
        client.sendMessage(new ConnectionClient(this.client.getUsername()));*/
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
    public void close(boolean isError) {

    }
}
