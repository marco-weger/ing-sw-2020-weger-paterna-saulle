package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.AnswerAbilityClient;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            ((BoardController) controller).banner.setText(((BoardController) controller).moveTA.getText());
            ((BoardController) controller).showcheckmove(message);
            ((BoardController) controller).setState(2);
            ((BoardController) controller).refresh();
        }
    }



    @Override
    public void handleMessage(CheckBuildServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setCbs(message);
            ((BoardController) controller).banner.setText(((BoardController) controller).buildTA.getText());
            ((BoardController) controller).showcheckbuild(message);
            ((BoardController) controller).setState(3);
            ((BoardController) controller).refresh();
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setState(0);
        }
    }

    @Override
    public void handleMessage(WorkerChosenServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        client.getWorkers().add(new SnapWorker(message.x, message.y, message.player, message.worker));
        if (message.player.equals(this.client.getUsername())) {
            if (message.worker == 1) {
                DefaultController controllerx;
                controllerx = loader.getController();
                //second Worker
                if (controllerx instanceof BoardController) {
                    ((BoardController) controllerx).refresh();
                    ((BoardController) controllerx).banner.setText(((BoardController) controllerx).workerITA2.getText());
                    ((BoardController) controllerx).setState(0);
                }
            }
        }
        else {
            for (int i = 0; i < client.getPlayers().size() - 1; i++) {
                if (client.getPlayers().get(i).name.equals(message.player) && client.getPlayers().get(i + 1).name.equals(client.getUsername()) && message.worker == 2) {
                        Platform.runLater(() -> {
                            Scene sn = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                            FXMLLoader loader2 = (FXMLLoader) sn.getUserData();
                            DefaultController controllerx = loader2.getController();

                            if (controllerx instanceof BoardController) {
                                ((BoardController) controllerx).setup();
                                ((BoardController) controllerx).banner.setText(((BoardController) controllerx).workerITA.getText());
                                ((BoardController) controllerx).setState(0);
                                ((BoardController) controllerx).refresh();
                                primaryStage.setScene(sn);
                                primaryStage.show();
                            }
                        });
                }
            }

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
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).setQas(message);
            ((BoardController) controller).question();
            ((BoardController) controller).refresh();
        }
    }



    @Override
    public void handleMessage(CurrentStatusServer message) {
        if(!client.getMyPlayer().loser && message.player.equals(client.getUsername())){
        FXMLLoader loader;
        DefaultController controller;
            switch(message.status){
                case WORKER_CHOICE:
                    Platform.runLater(() -> {
                        Scene s = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                        FXMLLoader xloader = (FXMLLoader) s.getUserData();
                        DefaultController xcontroller = xloader.getController();
                        System.out.println("Type the position of first worker [x-y]");
                        if(xcontroller instanceof BoardController){
                            ((BoardController) xcontroller).refresh();
                            ((BoardController) xcontroller).banner.setText(((BoardController) xcontroller).workerITA.getText());
                            ((BoardController) xcontroller).setup();
                            ((BoardController) xcontroller).setState(0);
                        }

                        primaryStage.setScene(s);
                        primaryStage.show();
                    });
                    break;
                case START:
                    loader = (FXMLLoader) primaryStage.getScene().getUserData();
                    controller = loader.getController();
                    if(controller instanceof BoardController){
                        ((BoardController) controller).setCss(message);
                        ((BoardController) controller).banner.setText(((BoardController) controller).workerCTA.getText());
                        ((BoardController) controller).refresh();
                        ((BoardController) controller).setState(1);
                    }
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    public void handleMessage(SomeoneLoseServer message) {
        //TODO provvisorio
        Text alert = new Text();
        for(SnapPlayer sp : client.getPlayers())
            if(sp.name.equals(message.player))
                sp.loser = true;

        ArrayList<SnapWorker> toDelete = new ArrayList<>();
        for(SnapWorker sw : client.getWorkers())
            if(sw.name.equals(message.player))
                toDelete.add(sw);
        client.removeWorkers(toDelete);


        if(this.client.getUsername().equals(message.player)){
            alert.setText("Time is up... YOU LOSE!");
        }
        else{

           alert.setText(message.player.toString() + "has lost!");
        }

        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).banner.setText(alert.getText());
            ((BoardController) controller).refresh();
        }
    }



    @Override
    public void handleMessage(AvailableCardServer message) {
        Platform.runLater(() -> {
            if(message.cardName.isEmpty()){
                primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Challenger.fxml"));
            }
            else{
                Scene scene = load("/it.polimi.ingsw/view/gui/fxml/Card.fxml");

                FXMLLoader loader = (FXMLLoader) scene.getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof CardController){
                    ((CardController) controller).cards = message.cardName;
                    ((CardController) controller).setGods();
                }

                primaryStage.setScene(scene);
            }
            primaryStage.show();
        });
    }

    @Override
    public void handleMessage(SomeoneWinServer message) {
    //TODO provvisorio
        Text end = new Text();
        if(this.client.getUsername().equals(message.player))
            end.setText("YOU WIN!");
        else {
            end.setText("Time is Up... YOU LOSE!");
        }

    FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
    DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).banner.setText(end.getText());
            ((BoardController) controller).refresh();
        }
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

            for(int i=0;i<client.getPlayers().size();i++){
                if(i==0)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png";
                else if(i==1)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_blu.png";
                else if(i==2)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_yellow.png";
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
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).banner.setText(((BoardController) controller).opponentTA.getText());
            ((BoardController) controller).refresh();
        }
    }

    @Override
    public void handleMessage(MovedServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController){
            ((BoardController) controller).refresh();
        }
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

       /*  Platform.runLater(() -> {
                        Scene s = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                        FXMLLoader xloader = (FXMLLoader) s.getUserData();
                        DefaultController xcontroller = xloader.getController();
                        System.out.println("Type the position of first worker [x-y]");
                        if(xcontroller instanceof BoardController){
                            ((BoardController) xcontroller).square00.setImage(new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png"));
                            ((BoardController) xcontroller).block00.setImage(new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png"));
                            ((BoardController) xcontroller).getBLock(2,2).setImage(((BoardController) xcontroller).floor2);
                            ((BoardController) xcontroller).question();
                            ((BoardController) xcontroller).refresh();


                        }

                        primaryStage.setScene(s);
                        primaryStage.show();
                    });*/


    }


}