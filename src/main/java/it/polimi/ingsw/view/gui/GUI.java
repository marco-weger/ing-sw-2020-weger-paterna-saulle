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
    BoardController boardController;

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

    }

    @Override
    public void handleMessage(CheckBuildServer message) {

    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {
        Parent rootX;
        FXMLLoader loaderX = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Board.fxml"));
        try{
            rootX = loaderX.load();
            Platform.runLater(() -> {
                primaryStage.setScene(new Scene(Objects.requireNonNull(rootX), sceneWidth, sceneHeight, Color.TRANSPARENT));
                primaryStage.show();
        });

        }
        catch (IOException e){
            e.printStackTrace();
        }

        boardController = loaderX.getController();
        boardController.setState(0);

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

    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {
        Platform.runLater(() -> {
            if(message.cardName.isEmpty())
                primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/CardChallenger.fxml"));
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
        //Platform.runLater(() -> {
            //primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/CardChallenger.fxml"));
            //primaryStage.show();
        //});
    }
}
