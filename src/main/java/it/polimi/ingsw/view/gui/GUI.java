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

    private Client client;
    private Stage primaryStage;

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

        displayFirstWindow();

        /*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        */

        /*
        this.primaryStage = primaryStage;

        //List<String> args = getParameters().getRaw();




        /*
        // initialize your splash stage.
        Platform.setImplicitExit(false);
        splashStage.initStyle(StageStyle.TRANSPARENT);
. . .
// create your main stage.
        Stage mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.initStyle(StageStyle.DECORATED);
        mainStage.setOnHide(event -> Platform.exit())
                . . .
// on some later event hide your splash stage and show your main stage.
        splashStage.hide();
        mainStage.show();

        //primaryStage.
        primaryStage.setTitle("Santorini");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
        //defaultcontroller.mainstage = primaryStage;



        //startMusic();
        */
    }

    /*
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
        displayFirstWindow();
    }
    */

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

        defaultcontroller.buttonQuit.setLayoutX(sceneWidth-100);

        defaultcontroller.setClient(client);

        Scene scene = new Scene(Objects.requireNonNull(root), sceneWidth, sceneHeight, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

        //Stage s  = new Stage();
        //s.setTitle("Santorini");
        //s.initStyle(StageStyle.TRANSPARENT);
        //s.setScene(scene);
        //s.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
        defaultcontroller.mainstage = primaryStage;


        //primaryStage.setScene(scene);
        //primaryStage.show();
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
        Platform.runLater(() -> {
            primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
            primaryStage.show();
        });
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
        Scene scene;
        if(!client.connect())
        {
            /*
            println(TextFormatting.RESET+"Server unreachable!");
            print(TextFormatting.RESET+"Type new ip address " + TextFormatting.input());
            client.setIp(new Scanner(System.in).nextLine());
            print(TextFormatting.RESET+"Type new port " + TextFormatting.input());
            try {
                client.setPort(Integer.parseInt(new Scanner(System.in).nextLine()));
            } catch (NumberFormatException nfe) { client.setPort(1234); }
            */
            System.out.println("ARRIVO QUI... DEVO CHIEDERE NUOVO IP");

            scene =load("/it.polimi.ingsw/view/gui/fxml/Server.fxml");
        } else scene = load("/it.polimi.ingsw/view/gui/fxml/Name.fxml");
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
