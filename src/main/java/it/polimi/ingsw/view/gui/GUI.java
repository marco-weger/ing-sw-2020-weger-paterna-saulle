package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.TimerDisconnection;
import it.polimi.ingsw.network.TimerTurnClient;
import it.polimi.ingsw.network.TimerTurnServer;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
//import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends Application implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    //int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    //int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    double sceneWidth = 0;
    double sceneHeight = 0;

    Parent root;
    DefaultController defaultcontroller;

    private TimerTurnClient timer;

    private Client client;
    private Stage primaryStage;
    private Stage win;
    private Stage lose;

    private ScheduledExecutorService timeOut;

    public void setAndShow(Scene s){
        this.primaryStage.setScene(s);
        this.primaryStage.show();
    }

    public Stage getLose() {
        return lose;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public Stage getWin() {
        return win;
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
            //System.out.println(file);
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
            ((BoardController) controller).showCheckMove(message);
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
            ((BoardController) controller).showCheckBuild(message);
            ((BoardController) controller).setState(3);
            ((BoardController) controller).refresh();
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) {
        getPlayerByName(message.player).card = message.cardName;
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
                   // Platform.runLater(() -> {
                       // Scene sn = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                      //  FXMLLoader loader2 = (FXMLLoader) sn.getUserData();
                       // DefaultController controllerx = loader2.getController();
                    DefaultController controllerx;
                    controllerx = loader.getController();

                        if (controllerx instanceof BoardController) {
                            controllerx.setup();
                            ((BoardController) controllerx).banner.setText(((BoardController) controllerx).workerITA.getText());
                            ((BoardController) controllerx).setState(0);
                            ((BoardController) controllerx).refresh();
                            //primaryStage.setScene(sn);
                            //primaryStage.show();
                        }
                   // });
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
        int loading = 0;
        FXMLLoader loader;
        DefaultController controller;
        if(!client.getMyPlayer().loser && message.player.equals(client.getUsername())){
            switch(message.status){
                case WORKER_CHOICE:
                    Platform.runLater(() -> {
                        Scene s = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                        FXMLLoader xloader = (FXMLLoader) s.getUserData();
                        DefaultController xcontroller = xloader.getController();
                        //player1, initalize first worker
                        if(xcontroller instanceof BoardController){
                            ((BoardController) xcontroller).refresh();
                            ((BoardController) xcontroller).banner.setText(((BoardController) xcontroller).workerITA.getText());
                            xcontroller.setup();
                            ((BoardController) xcontroller).setState(0);
                            s.setOnKeyPressed(e -> {
                                if (e.getCode() == KeyCode.Y || e.getCode() == KeyCode.N) {
                                    ((BoardController) xcontroller).activeQuestionIfPossible(e.getCode());
                                }
                            });
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
        if(!message.player.equals(client.getUsername()) && !client.getMyPlayer().loser && message.status == Status.WORKER_CHOICE ){
            Platform.runLater(() -> {
                Scene sn = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
                FXMLLoader loader2 = (FXMLLoader) sn.getUserData();
                DefaultController controllerx = loader2.getController();
                if(controllerx instanceof BoardController){
                    ((BoardController) controllerx).banner.setText("WAIT, "+message.player+"'s Turn");
                    ((BoardController) controllerx).refresh();
                    sn.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.Y || e.getCode() == KeyCode.N) {
                            ((BoardController) controllerx).activeQuestionIfPossible(e.getCode());
                        }
                    });
                }

                primaryStage.setScene(sn);
                primaryStage.show();
            });
        }
        if(!message.player.equals(client.getUsername()) && !client.getMyPlayer().loser && message.status != Status.WORKER_CHOICE ){
            loader = (FXMLLoader) primaryStage.getScene().getUserData();
            controller = loader.getController();
            if(controller instanceof BoardController){
                ((BoardController) controller).banner.setText("WAIT, "+message.player+"'s Turn");
                ((BoardController) controller).refresh();
            }

        }

        if(message.status.equals(Status.START) || message.status.equals(Status.WORKER_CHOICE)){
            startTimer(message.timer);
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
            Platform.runLater(() -> {
                lose = new Stage();
                try {
                    //System.out.println(file);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Lose.fxml"));
                    Parent root3 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    double limitY = 190* sceneWidth /1300;

                    Scene scene = new Scene(Objects.requireNonNull(root3), 421, 450, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
                    lose.initStyle(StageStyle.TRANSPARENT);
                    lose.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    lose.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lose.showAndWait();

            });
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

        unShowTimer();
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
        Text end = new Text();
        if(this.client.getUsername().equals(message.player)){
            Platform.runLater(() -> {
                win = new Stage();
                end.setText("YOU WIN!");
                unShowTimer();
                try {
                    //System.out.println(file);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Win.fxml"));
                    Parent root2 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    double limitY = 190* sceneWidth /1300;

                    Scene scene = new Scene(Objects.requireNonNull(root2), 421, 450, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
                    win.initStyle(StageStyle.TRANSPARENT);
                    win.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    win.initModality(Modality.WINDOW_MODAL);
                    win.initOwner(primaryStage);
                    scene.setUserData(loader);
                    win.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                win.showAndWait();
            });
        }
        else {
            Platform.runLater(() -> {
                lose = new Stage();
                end.setText("YOU LOSE!");
                unShowTimer();
                try {
                    //System.out.println(file);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Lose.fxml"));
                    Parent root3 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    double limitY = 190* sceneWidth /1300;

                    Scene scene = new Scene(Objects.requireNonNull(root3), 421, 450, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
                    lose.initStyle(StageStyle.TRANSPARENT);
                    lose.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    lose.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lose.showAndWait();
            });
        }
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController) {
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
                for(int i=0; i<message.players.size(); i++){
                    ((LobbyController) controller).buttonLobby.setText(((LobbyController) controller).buttonLobby.getText()+message.players.get(i)
                            +(i+1 == message.players.size() ? "" : "\n"));
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

    //private String currentPlayer = "";
    @Override
    public void statusHandler(CurrentStatusServer message){

    }

    @Override
    public void close(boolean isError) {

    }

    @Override
    public void displayFirstWindow() {
        //Scene scene = load("/it.polimi.ingsw/view/gui/fxml/Home.fxml");
      //  primaryStage.setScene(scene);
       // primaryStage.show();

        SnapPlayer p = new SnapPlayer("asdasd");
        p.card = CardName.ARTEMIS;
        client.getPlayers().add(p);
        p = new SnapPlayer("fdggh");
        p.card = CardName.MINOTAUR;
        client.getPlayers().add(p);
        p = new SnapPlayer("dfgds");
        p.card = CardName.DEMETER;
        client.getPlayers().add(p);
        client.setUsername("fdggh");

        Platform.runLater(() -> {
            Scene sn = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");
            FXMLLoader loader2 = (FXMLLoader) sn.getUserData();
            DefaultController controllerx = loader2.getController();

            if (controllerx instanceof BoardController) {
                controllerx.setup();
                ((BoardController) controllerx).banner.setText(((BoardController) controllerx).workerITA.getText());
                ((BoardController) controllerx).setState(0);
                ((BoardController) controllerx).refresh();
                primaryStage.setScene(sn);
                primaryStage.show();


            }

            Platform.runLater(() -> {
                lose = new Stage();
                try {
                    //System.out.println(file);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Lose.fxml"));
                    Parent root3 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    double limitY = 190* sceneWidth /1300;

                    Scene scene = new Scene(Objects.requireNonNull(root3), 421, 450, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
                    lose.initStyle(StageStyle.TRANSPARENT);
                    lose.setAlwaysOnTop(true);
                    lose.initModality(Modality.WINDOW_MODAL);
                    lose.initOwner(primaryStage);
                    scene.setUserData(loader);
                    lose.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lose.showAndWait();

            });
        });

    }

    public void setTimerText(long val){
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    //System.out.println(val);
                    ((BoardController) controller).buttonTimer.setVisible(true);
                    ((BoardController) controller).buttonTimer.setText(val+"");
                }
            } catch (Exception ignored){}
        });
    }

    public void unShowTimer(){
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    ((BoardController) controller).buttonTimer.setVisible(false);
                }
            } catch (Exception ignored){}
        });
    }

    public void startTimer(long second){
        Platform.runLater(() -> {
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController controller = loader.getController();
            if(controller instanceof BoardController){
                try{
                    if(timeOut != null)
                        timeOut.shutdown();
                } catch (Exception ignored){}
                try{
                    timeOut = Executors.newSingleThreadScheduledExecutor();
                    timer = new TimerTurnClient(this,timeOut,second);
                    timeOut.scheduleAtFixedRate(timer, 0, 1000, TimeUnit.MILLISECONDS);
                } catch (Exception ex){
                    LOGGER.log( Level.SEVERE, ex.toString(), ex );
                }
            }
        });
    }

    public SnapPlayer getPlayerByName(String name) {
        for (SnapPlayer p : client.getPlayers())
            if (p.name.equals(name))
                return p;
        return null;
    }

    public void setLoserBar() {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if (controller instanceof BoardController) {
            ((BoardController) controller).loserbanner();
            ((BoardController) controller).refresh();

        }
    }
}