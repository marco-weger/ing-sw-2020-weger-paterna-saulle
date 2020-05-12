package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientmessages.WorkerChoseClient;
import it.polimi.ingsw.commons.clientmessages.WorkerInitializeClient;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TextFormatting;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;



public class GUI extends Application implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    //int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    //int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    double sceneWidth = 0;
    double sceneHeight = 0;

    Parent root, root2;
    DefaultController defaultcontroller;
    //BoardController boardController;

    private Client client;
    private Stage primaryStage;

    public void setAndShow(Scene s){
        this.primaryStage.setScene(s);
        this.primaryStage.show();
    }

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
        client.readParams(client);
        client.setView(this);
        displayFirstWindow();
    }

    public Client getClient(){ return client; }

    public Scene load(String file){
        try {
            System.out.println(file);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            root = loader.load();
            defaultcontroller = loader.getController();
            defaultcontroller.setGUI(this);
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

            Scene scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
            scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
            scene.setUserData(loader);
            defaultcontroller.mainstage = primaryStage;
            defaultcontroller.setup();
            return scene;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void handleMessage(CheckMoveServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setCms(message);
            ((BoardController) controller).showcheckmove(message);
            ((BoardController) controller).setState(2);
        }
    }



    @Override
    public void handleMessage(CheckBuildServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setCbs(message);
            ((BoardController) controller).showcheckbuild(message);
            ((BoardController) controller).setState(3);
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setState(1);
        }

        /*
        boardController.setLevel(boardController.block00, boardController.floor1);
        boardController.setLevel(boardController.block22, boardController.dome);
        boardController.setLevel(boardController.block04, boardController.floor3);
        boardController.setPawn(boardController.square00, boardController.red);
        boardController.setPawn(boardController.square11, boardController.blu);
        boardController.setPawn(boardController.square22, boardController.green);
        boardController.setPawn(boardController.square33, boardController.bronze);
        boardController.setPawn(boardController.square44, boardController.yellow);*/
    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {
       // if(!client.getMyPlayer().loser && message.player.equals(client.getUsername())){
        FXMLLoader loader;
        DefaultController controller;
            switch(message.status){
                case WORKER_CHOICE:
                    Platform.runLater(() -> {
                        Scene s = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                        FXMLLoader xloader = (FXMLLoader) s.getUserData();
                        DefaultController xcontroller = xloader.getController();
                        if(xcontroller instanceof BoardController){
                            ((BoardController) xcontroller).setState(0);
                        }

                        primaryStage.setScene(s);
                        primaryStage.show();
                    });
                    break;
                case START:
                    //TODO wirte on the bottom "It's your turn!"
                    //TODO print timer
                    //TODO write on the bottom "Chose the worker to play with [x-y] "
                    //boardController.setState(1);
                    loader = (FXMLLoader) primaryStage.getScene().getUserData();
                    controller = loader.getController();
                    if(controller instanceof BoardController){
                        ((BoardController) controller).setState(1);
                    }
                    break;
                default:
                    break;
            }
        }
  //  }



    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {
        Platform.runLater(() -> {
            if(message.cardName.isEmpty()){
                primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/CardChallenger.fxml"));
            }
            else{
                Scene s = load("/it.polimi.ingsw/view/gui/fxml/Card.fxml");

                FXMLLoader loader = (FXMLLoader) s.getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof CardController){
                    ((CardController) controller).cards = message.cardName;
                    if(message.cardName.size() == 2){
                        ((CardController) controller).button1.setText(message.cardName.get(0).toString());
                        ((CardController) controller).button2.setText(message.cardName.get(1).toString());
                        ((CardController) controller).button3.setDisable(true);
                    } else if(message.cardName.size() == 3){
                        ((CardController) controller).button1.setText(message.cardName.get(0).toString());
                        ((CardController) controller).button2.setText(message.cardName.get(1).toString());
                        ((CardController) controller).button3.setText(message.cardName.get(2).toString());
                    }
                }

                primaryStage.setScene(s);
            }
            primaryStage.show();
        });
    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void handleMessage(NameRequestServer message) {
        Platform.runLater(() -> {
            primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
            primaryStage.show();
        });
    }

    @Override
    public void handleMessage(LobbyServer message) {
        client.setPlayersByString(message.players);
        Platform.runLater(() -> {
            Scene s = load("/it.polimi.ingsw/view/gui/fxml/Lobby.fxml");

            FXMLLoader loader = (FXMLLoader) s.getUserData();
            DefaultController controller = loader.getController();
            if(controller instanceof LobbyController){
                ((LobbyController) controller).textAreaLobby.setEditable(false);
                for(int i=0; i<message.players.size(); i++){
                    ((LobbyController) controller).textAreaLobby.setText(((LobbyController) controller).textAreaLobby.getText()+"\n"+message.players.get(i));
                }
            }

            primaryStage.setScene(s);
            primaryStage.show();
        });
    }

    @Override
    public void handleMessage(ModeRequestServer message) {
        Platform.runLater(() -> {
            primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            primaryStage.show();
        });
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
    public void statusHandler(CurrentStatusServer message) {

    }

    @Override
    public void close(boolean isError) {

    }

    @Override
    public void displayFirstWindow() {
        Scene scene = load("/it.polimi.ingsw/view/gui/fxml/Home.fxml");
        primaryStage.setScene(scene);
        primaryStage.show();

        /*
        Platform.runLater(() -> {
            Scene s = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
            FXMLLoader xloader = (FXMLLoader) s.getUserData();
            DefaultController xcontroller = xloader.getController();
            if(xcontroller instanceof BoardController){
                ((BoardController) xcontroller).setState(0);
            }

            primaryStage.setScene(s);
            primaryStage.show();
        });
         */
    }

}
