package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.TimerTurnClient;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GUI extends Application implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    double sceneWidth = 0;
    double sceneHeight = 0;
    int currentP = 0; /* a parameter to Save CurrentPlayer during the CARD_CHOSE Status*/
    int NetworkErrorFlag = 0; /*a flag to avoid mutliple netork error freeze*/


    Parent root;
    DefaultController defaultcontroller;

    private static final String CURSOR = "/it.polimi.ingsw/view/gui/img/pointer.png";
    private static final String BOARD = "/it.polimi.ingsw/view/gui/fxml/Board.fxml";

    private static final double POPUPX = 421;
    private static final double POPUPY = 450;

    private TimerTurnClient timer;
    private Client client;
    private Stage primaryStage;
    private Stage win;
    private Stage lose;
    private Stage error;

    private ScheduledExecutorService timeOut;

    public Stage getLose() {
        return lose;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public Stage getWin() {
        return win;
    }


    /**
     * Main JavaFX function, set size,cursor and Style of Santorini
     * Creates a new Client
     * @param primaryStage the mainstage of the GUI
     */
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


    /**
     * Load safely an FXML file to the primaryStage
     * @param file path to an FXML file
     *
     */
    public Scene load(String file){
        try {
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
            scene.setCursor(new ImageCursor(new Image(CURSOR)));
            //ImageCursor.getBestSize(30,30).getWidth(),ImageCursor.getBestSize(30,30).getHeight())
            scene.setUserData(loader);
            defaultcontroller.mainstage = primaryStage;
            defaultcontroller.setup();
            return scene;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Shows on board the cells available included in the CheckMoveServer message
     * updates the banner message
     * Enables the currentPlayer to send MovedClient message
     * @param message a CheckMoveServer message
     */
    @Override
    public void handleMessage(CheckMoveServer message) {
        if(client.getUsername().equals(client.getCurrentPlayer())){
            Platform.runLater(() -> {
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    ((BoardController) controller).setCms(message);
                    ((BoardController) controller).banner.setText("Choose the cell where you want to move");
                    ((BoardController) controller).showCheckMove(message);
                    ((BoardController) controller).setState(2);
                    ((BoardController) controller).refresh();
                } else System.out.println("FATAL ERROR - CHECKMOVESERVER MESSAGE");
            });
        }
    }


    /**
     * Shows on board the cells available included in the CheckBuildServer message
     * updates the banner message
     * Enables the currentPlayer to send BuildClient message
     * @param message a CheckBuildServer message
     */
    @Override
    public void handleMessage(CheckBuildServer message) {
        if(client.getUsername().equals(client.getCurrentPlayer())) {
            Platform.runLater(() -> {
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if (controller instanceof BoardController) {
                    ((BoardController) controller).setCbs(message);
                    ((BoardController) controller).banner.setText("Choose the cell where you want to build");
                    ((BoardController) controller).showCheckBuild(message);
                    ((BoardController) controller).setState(3);
                    ((BoardController) controller).refresh();
                } else System.out.println("FATAL ERROR - CHECKBUILDSERVER MESSAGE");
            });
        }
    }


    /**
     * Assign the the currentPlayer the card that he've chosen
     * @param message
     */
    @Override
    public void handleMessage(CardChosenServer message) {
        getPlayerByName(message.player).card = message.cardName;
    }


    /**
     *Updates the banner message
     * Enables the currentPlayer to send WorkerInitialize message
     * @param message a WorkerChosenServer message
     */
    @Override
    public void handleMessage(WorkerChosenServer message) {
        currentP++; //FRANCESCO COUNTER
        if(currentP/2 == client.getPlayers().size()){
            currentP--;
        }
        boolean flag = false;
        try {
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            client.getWorkers().add(new SnapWorker(message.x, message.y, message.player, message.worker));
            if (message.player.equals(this.client.getUsername())) {
                if (message.worker == 1) {
                    DefaultController controllerx;
                    controllerx = loader.getController();
                    //second Worker
                    if (controllerx instanceof BoardController) {
                        Platform.runLater(() -> {
                            ((BoardController) controllerx).banner.setText("Choose the position of the second worker");
                            ((BoardController) controllerx).setState(0);
                        });
                        flag = true;
                    }
                }
            } else {
                for (int i = 0; i < client.getPlayers().size() - 1; i++) {
                    if (client.getPlayers().get(i).name.equals(message.player) && client.getPlayers().get(i + 1).name.equals(client.getUsername()) && message.worker == 2) {
                        DefaultController controllerx;
                        controllerx = loader.getController();

                        if (controllerx instanceof BoardController) {
                            Platform.runLater(() -> {
                                controllerx.setup();
                                ((BoardController) controllerx).banner.setText("Choose the position of the first worker");
                                ((BoardController) controllerx).setState(0);
                                ((BoardController) controllerx).refresh();
                            });
                            flag = true;
                        }
                    }
                }

            }
        }catch (Exception ex){
            System.err.println("IMPORTANT!!!");
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }
        if(!flag){
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController controllerx;
            controllerx = loader.getController();
            if (controllerx instanceof BoardController) {
                Platform.runLater(() -> {
                    controllerx.setup();
                    ((BoardController) controllerx).banner.setText("WAIT, " + client.getPlayers().get(currentP/2).name+ "'s Turn");
                    ((BoardController) controllerx).refresh();
                });
            }

        }
    }


    /**
     * Updates the banner message
     * Shows Yes and No button on the Banner
     * Enables the Client to send AnswerAbilityClient message
     * @param message a QuestionAbilityMessage message
     */
    @Override
    public void handleMessage(QuestionAbilityServer message) {
        if(client.getUsername().equals(client.getCurrentPlayer())){
            Platform.runLater(() -> {
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    ((BoardController) controller).setQas(message);
                    ((BoardController) controller).question();
                    ((BoardController) controller).refresh();
                } else System.out.println("FATAL ERROR - QUESTIONABILITYSERVER MESSAGE");
            });
        }
    }


    /**
     * Handle the First WorkerInitialize, and the WorkerChose
     * in both case update the banner message
     * In case of WORKER_CHOSE, enable the current player to send WorkerInitalizeClient message
     * IN case of START, enable the current player to send WorkerChoseClient message
     * @param message a CurrentStatusServer message
     */
    @Override
    public void handleMessage(CurrentStatusServer message) {
        DefaultController controller;
        FXMLLoader loader;
        if(!client.getMyPlayer().loser && message.player.equals(client.getUsername())){
            switch(message.status){
                case WORKER_CHOICE:
                    Platform.runLater(() -> {
                        Scene s = load(BOARD);
                        FXMLLoader xloader = (FXMLLoader) s.getUserData();
                        DefaultController xcontroller = xloader.getController();
                        //player1, initalize first worker
                        if(xcontroller instanceof BoardController){
                            Platform.runLater(() -> {
                                ((BoardController) xcontroller).refresh();
                                ((BoardController) xcontroller).banner.setText("Choose the position of the first worker");
                                xcontroller.setup();
                                ((BoardController) xcontroller).setState(0);
                                s.setOnKeyPressed(e -> {
                                    if (e.getCode() == KeyCode.Y || e.getCode() == KeyCode.N) {
                                        ((BoardController) xcontroller).activeQuestionIfPossible(e.getCode());
                                    }
                                });
                            });
                        }

                        primaryStage.setScene(s);
                        primaryStage.show();
                    });
                    break;
                case START:
                    Platform.runLater(() -> {
                        FXMLLoader l = (FXMLLoader) primaryStage.getScene().getUserData();
                        DefaultController c = l.getController();
                        if(c instanceof BoardController){
                            Platform.runLater(() -> {
                                ((BoardController) c).setCss(message);
                                ((BoardController) c).banner.setText("Choose a Worker");
                                ((BoardController) c).refresh();
                                ((BoardController) c).setState(1);
                            });
                        } else System.out.println("FATAL ERROR - CURRENTSTATUSSERVER [START] MESSAGE!");
                    });
                    break;
                default:
                    break;
            }
        }
        if(!message.player.equals(client.getUsername()) && !client.getMyPlayer().loser && message.status == Status.WORKER_CHOICE ){
            Platform.runLater(() -> {
                Scene sn = load(BOARD);
                FXMLLoader loader2 = (FXMLLoader) sn.getUserData();
                DefaultController controllerx = loader2.getController();
                if(controllerx instanceof BoardController){
                    Platform.runLater(() -> {
                        ((BoardController) controllerx).banner.setText("WAIT, "+message.player+"'s Turn");
                        ((BoardController) controllerx).refresh();
                        sn.setOnKeyPressed(e -> {
                            if (e.getCode() == KeyCode.Y || e.getCode() == KeyCode.N) {
                                ((BoardController) controllerx).activeQuestionIfPossible(e.getCode());
                            }
                        });
                    });
                }

                primaryStage.setScene(sn);
                primaryStage.show();
            });
        }
        if(!message.player.equals(client.getUsername()) && !client.getMyPlayer().loser && message.status != Status.WORKER_CHOICE ){
            Platform.runLater(() -> {
                FXMLLoader loaderx = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controllerx = loaderx.getController();
                if (controllerx instanceof BoardController) {
                    ((BoardController) controllerx).banner.setText("WAIT, " + message.player + "'s Turn");
                    ((BoardController) controllerx).refresh();
                }
            });
        }

        if(message.status.equals(Status.START) || message.status.equals(Status.WORKER_CHOICE)){
            startTimer(message.timer);
        }
    }


    /**
     * Removes the loser to the Players Arraylist
     * Shows to the Loser the Lose PopUp
     * Updates the banner message
     * @param message a SomeoneLoseServer message
     */
    @Override
    public void handleMessage(SomeoneLoseServer message) {
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
            Platform.runLater(() -> {
                unShowTimer();
                lose = new Stage();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Lose.fxml"));
                    Parent root3 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    if(defaultcontroller instanceof LoseController) {
                        if (getClient().getPlayers().size() > 1)
                            ((LoseController) defaultcontroller).Spectator.getStyleClass().add("spectator");
                        else
                            ((LoseController) defaultcontroller).Spectator.getStyleClass().add("exit");
                    }

                    Scene scene = new Scene(Objects.requireNonNull(root3), POPUPX, POPUPY, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image(CURSOR),36,45));
                    lose.initStyle(StageStyle.TRANSPARENT);
                    lose.setAlwaysOnTop(true);
                    lose.initModality(Modality.WINDOW_MODAL);
                    lose.initOwner(primaryStage);
                    scene.setUserData(loader);

                    lose.setScene(scene);

                    lose.setX((primaryStage.getX()+sceneWidth/2- POPUPX /2));
                    lose.setY((primaryStage.getY()+sceneHeight/2- POPUPY /2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lose.showAndWait();

            });
        }
        else alert.setText(message.player + "has lost!");

        Platform.runLater(() -> {
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController controller = loader.getController();
            if(controller instanceof BoardController){
                ((BoardController) controller).banner.setText(alert.getText());
                ((BoardController) controller).refresh();
            } else System.out.println("FATAL ERROR - SOMEONELOSE SERVER MESSAGE");
        });
    }


    /**
     * If is the first message, shows to the Challenger the Challenger Scene
     * Otherwise shows th Card Scene to the Current PLayer
     * @param message an AbailableCardServer
     */
    @Override
    public void handleMessage(AvailableCardServer message) {
        if(message.player.equals(this.getClient().getUsername())){
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
    }


    /**
     * Updates the banner
     * Shows to the winner the Win PopUp
     * Shows to the Losers the Lose PopUp
     * @param message a SomeoneWinServer message
     */
    @Override
    public void handleMessage(SomeoneWinServer message) {
        unShowTimer();
        Text end = new Text();
        if(this.client.getUsername().equals(message.player)){
            Platform.runLater(() -> {
                win = new Stage();
                end.setText("YOU WIN!");
                unShowTimer();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Win.fxml"));
                    Parent root2 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    Scene scene = new Scene(Objects.requireNonNull(root2), POPUPX, POPUPY, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image(CURSOR),36,45));
                    win.initStyle(StageStyle.TRANSPARENT);
                    win.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    win.initModality(Modality.WINDOW_MODAL);
                    win.initOwner(primaryStage);
                    scene.setUserData(loader);
                    win.setScene(scene);

                    win.setX((primaryStage.getX()+sceneWidth/2- POPUPX /2));
                    win.setY((primaryStage.getY()+sceneHeight/2- POPUPY /2));
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Lose.fxml"));
                    Parent root3 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    Scene scene = new Scene(Objects.requireNonNull(root3), POPUPX, POPUPY, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image(CURSOR),36,45));
                    lose.initStyle(StageStyle.TRANSPARENT);
                    lose.setAlwaysOnTop(true);
                    lose.initModality(Modality.WINDOW_MODAL);
                    lose.initOwner(primaryStage);
                    if(defaultcontroller instanceof LoseController) {
                        ((LoseController) defaultcontroller).Spectator.getStyleClass().add("exit");
                        ((LoseController) defaultcontroller).setSomeonewinflag(true);
                    }
                    scene.setUserData(loader);
                    lose.setScene(scene);

                    lose.setX((primaryStage.getX()+sceneWidth/2- POPUPX /2));
                    lose.setY((primaryStage.getY()+sceneHeight/2- POPUPY /2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lose.showAndWait();
            });
        }
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof BoardController) {
            Platform.runLater(() -> {
                ((BoardController) controller).banner.setLayoutX((sceneWidth/2)-400);
                ((BoardController) controller).banner.setText(end.getText());
                ((BoardController) controller).refresh();
            });
        }

    }


    /**
     * Shows the Name Scene to the Client
     * @param message a NameRequestServer message
     */
    @Override
    public void handleMessage(NameRequestServer message) {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if(controller instanceof NameController){
            ((NameController) controller).showBanner();
        } else {
            Platform.runLater(() -> {
                primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
                primaryStage.show();
            });
        }
    }


    /**
     * Shows the Lobby Scene to the players
     * Assigns colors to the Players
     * @param message a LobbyServer message
     */
    @Override
    public void handleMessage(LobbyServer message) {
        client.setPlayersByString(message.players);

        for(int i=0;i<client.getPlayers().size();i++){
            if(i==0)
                client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png";
            else if(i==1)
                client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_blu.png";
            else if(i==2)
                client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_yellow.png";
        }

        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();

        if(controller instanceof LobbyController){
            Platform.runLater(() -> {
                ((LobbyController) controller).setReconnection(false);
                ((LobbyController) controller).new2.setVisible(false);
                ((LobbyController) controller).new3.setVisible(false);
                ((LobbyController) controller).banner.setVisible(false);
                ((LobbyController) controller).buttonLobby.setText("");
                for(int i=0; i<message.players.size(); i++){
                    ((LobbyController) controller).buttonLobby.setText(((LobbyController) controller).buttonLobby.getText()+message.players.get(i)
                            +(i+1 == message.players.size() ? "" : "\n"));
                }
            });
        } else {
            Platform.runLater(() -> {
                Scene s = load("/it.polimi.ingsw/view/gui/fxml/Lobby.fxml");

                FXMLLoader newLoader = (FXMLLoader) s.getUserData();
                DefaultController newController = newLoader.getController();
                if(newController instanceof LobbyController){
                    for(int i=0; i<message.players.size(); i++){
                        ((LobbyController) newController).buttonLobby.setText(((LobbyController) newController).buttonLobby.getText()+message.players.get(i)
                                +(i+1 == message.players.size() ? "" : "\n"));
                    }
                }

                primaryStage.setScene(s);
                primaryStage.show();
            });
        }
    }


    /**
     * Shows the Mode Scene to the Client
     * Enables the Client to send ModeChoseClient message
     * @param message
     */
    @Override
    public void handleMessage(ModeRequestServer message) {
        Platform.runLater(() -> {
            primaryStage.setScene(load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            primaryStage.show();
        });
    }


    /**
     * refresh the board due to a costruction
     * @param message a BuiltServer message
     */
    @Override
    public void handleMessage(BuiltServer message) {
        Platform.runLater(() -> {
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController controller = loader.getController();
            if(controller instanceof BoardController){
                ((BoardController) controller).refresh();
            } else System.out.println("FATAL ERROR - BUILTSERVER MESSAGE");
        });
    }


    /**
     * refresh the board due to a move of a Pawn
     * @param message a MovedServer message
     */
    @Override
    public void handleMessage(MovedServer message) {
        Platform.runLater(() -> {
            FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController controller = loader.getController();
            if(controller instanceof BoardController){
                ((BoardController) controller).refresh();
            } else System.out.println("FATAL ERROR - MOVEDSERVER MESSAGE");
        });
    }


    /**
     * Just a ping message to check the reachability client/server
     * @param message
     */
    @Override
    public void handleMessage(PingServer message) {

    }

    @Override
    public void handleMessage(TimeOutServer message) {
        Platform.runLater(() -> {
            FXMLLoader l = (FXMLLoader) primaryStage.getScene().getUserData();
            DefaultController c = l.getController();
            if(c instanceof BoardController){
                Platform.runLater(() -> {
                    if(((BoardController) c).getQuestionFlag()){
                        ((BoardController) c).yOff(((BoardController) c).yes);
                        ((BoardController) c).nOff(((BoardController) c).no);
                        ((BoardController) c).setQuestionFlag(false);
                        ((BoardController) c).refresh();
                    }
                    if(message.n == 0 && ((BoardController) c).Cms != null && ((BoardController) c).Cms.sc != null)
                        for (SnapCell cellx : ((BoardController) c).Cms.sc)
                            ((BoardController) c).lightItDown(((BoardController) c).getCell(cellx.row,cellx.column));
                    ((BoardController) c).unShowTimer();
                    ((BoardController) c).banner.setLayoutX((this.sceneWidth/2)-400);
                    ((BoardController) c).banner.setText(message.player+" has a network issue... Reconnecting " + (message.n+1) + "/" + (message.of+1));
                    ((BoardController) c).setState(4);
                });
            } else System.out.println("FATAL ERROR - TIMEROUT SERVER MESSAGE!");
        });
    }


    /**
     * PERSISTENCE (Case Server Crash)
     * Resend to the Client all the state of the previous match
     * Resend to the Client them pawns color
     * Reload the Lobby Scene to every Client
     * @param message
     */
    @Override
    public void handleMessage(ReConnectionServer message) {
        if(message.player.equals(client.getUsername())){
            client.setBoard(message.board);
            client.setWorkers(message.workers);
            client.setPlayersBySnap(message.players);
            client.setCurrentPlayer(message.currentPlayer);
            client.setMustPrint(true);
            for(int i=0;i<client.getPlayers().size();i++){
                if(i==0)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png";
                else if(i==1)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_blu.png";
                else if(i==2)
                    client.getPlayers().get(i).color = "/it.polimi.ingsw/view/gui/img/pawn/pawn_yellow.png";
            }

            if(message.type == 2){
                // LOBBY
                Platform.runLater(() -> {
                    Scene s = load("/it.polimi.ingsw/view/gui/fxml/Lobby.fxml");

                    FXMLLoader loader = (FXMLLoader) s.getUserData();
                    DefaultController controller = loader.getController();
                    if(controller instanceof LobbyController){
                        ((LobbyController) controller).setReconnection(true);
                        ((LobbyController) controller).new2.setVisible(true);
                        ((LobbyController) controller).new3.setVisible(true);
                        ((LobbyController) controller).banner.setVisible(true);
                        for(int i=0; i<message.players.size(); i++){
                            ((LobbyController) controller).buttonLobby.setText(((LobbyController) controller).buttonLobby.getText()+message.players.get(i).name
                                    +(i+1 == message.players.size() ? "" : "\n"));
                        }
                    }
                    primaryStage.setScene(s);
                    primaryStage.show();
                });
            }
        } else {
            Platform.runLater(() -> {
                FXMLLoader l = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController c = l.getController();
                if(c instanceof BoardController){
                    ((BoardController) c).showTimer();
                    ((BoardController) c).banner.setText(message.player+" IS BACK!");
                    ((BoardController) c).refresh();
                }
            });
        }
    }


    /**
     * EasterEgg Secret
     * NO SPOILERS
     * @param easterEggServer
     */
    @Override
    public void handleMessage(EasterEggServer easterEggServer) {
        if(easterEggServer.player.equals(client.getUsername())){
            Platform.runLater(() -> {
                Scene s = load("/it.polimi.ingsw/view/gui/fxml/EasterEgg.fxml");

                FXMLLoader newLoader = (FXMLLoader) s.getUserData();
                DefaultController newController = newLoader.getController();
                if(newController instanceof EasterEggController){
                    boolean go = true;
                    ((EasterEggController) newController).setLast(easterEggServer.last);
                    ((EasterEggController) newController).buttonLobby.setText("");
                    Object[] values = easterEggServer.win.entrySet().toArray();
                    Arrays.sort(values, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            return ((Map.Entry<String, Integer>) o2).getValue()
                                    .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                        }
                    });
                    for (Object entry : values) {
                        ((EasterEggController) newController).buttonLobby.setText(
                                ((EasterEggController) newController).buttonLobby.getText()+(go ? "" : "\n")+((Map.Entry) entry).getKey() + "   " + ((Map.Entry) entry).getValue());
                        go = false;
                    }
                }

                primaryStage.setScene(s);
                primaryStage.show();
            });
        }
    }


    /**
     * Used in the CLI the handle the opponent's banner message
     * Not used in GUI mode
     * @param message a CurrentStatusServer message
     */
    @Override
    public void statusHandler(CurrentStatusServer message){

    }


    /**
     * In CLI mode, handle the close of the match due to an error or a quit request
     * In GUI mode, handle the clos of the march ONLY due to an error.
     * Shows the error PopUp.
     * @param isError Useless in GUI mode
     */
    @Override
    public void close(boolean isError) {
        if(NetworkErrorFlag == 0) {
            NetworkErrorFlag++;
            Platform.runLater(() -> {
                error = new Stage();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Error.fxml"));
                    Parent root2 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    Scene scene = new Scene(Objects.requireNonNull(root2), POPUPX, POPUPY, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image(CURSOR), 36, 45));
                    error.initStyle(StageStyle.TRANSPARENT);
                    error.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    error.initModality(Modality.WINDOW_MODAL);
                    error.initOwner(getPrimaryStage());
                    scene.setUserData(loader);
                    error.setScene(scene);

                    error.setX((getPrimaryStage().getX() + sceneWidth / 2 - POPUPX / 2));
                    error.setY((getPrimaryStage().getY() + sceneHeight / 2 - POPUPY / 2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                error.showAndWait();
            });
        }
    }


    /**
     * PERSISTENCE (Client Crash)
     * Just reload the old Match if the Client is returned in time
     */
    @Override
    public void displayBoard() {
        Platform.runLater(() -> {
            Scene s = load(BOARD);
            FXMLLoader xloader = (FXMLLoader) s.getUserData();
            DefaultController xcontroller = xloader.getController();
            //player1, initalize first worker
            if(xcontroller instanceof BoardController){
                ((BoardController) xcontroller).refresh();
                xcontroller.setup();
                s.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.Y || e.getCode() == KeyCode.N) {
                        ((BoardController) xcontroller).activeQuestionIfPossible(e.getCode());
                    }
                });
            }
            primaryStage.setScene(s);
            primaryStage.show();
        });
    }


    /**
     * load the Home Scene
     * Shows the PrimaryStage
     */
    @Override
    public void displayFirstWindow() {
        Scene scene = load("/it.polimi.ingsw/view/gui/fxml/Home.fxml");
        primaryStage.setScene(scene);
        primaryStage.show();

        /* TEST BENCH */
/*
        SnapPlayer p;
        p = new SnapPlayer("asdasd");
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
            Scene sn = load("/it.polimi.ingsw/view/gui/fxml/Board.fxml");;
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
                win = new Stage();
                //end.setText("YOU WIN!");
                unShowTimer();
                try {
                    //System.out.println(file);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Win.fxml"));
                    Parent root2 = loader.load();
                    defaultcontroller = loader.getController();
                    defaultcontroller.setGUI(this);
                    Scene scene = new Scene(Objects.requireNonNull(root2), 421, 450, Color.TRANSPARENT);
                    scene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
                    win.initStyle(StageStyle.TRANSPARENT);
                    win.setAlwaysOnTop(true);
                    scene.setUserData(loader);
                    win.initModality(Modality.WINDOW_MODAL);
                    win.initOwner(primaryStage);
                    scene.setUserData(loader);
                    win.setScene(scene);
                    win.setX((primaryStage.getX()+sceneWidth/2-421/2));
                    win.setY((primaryStage.getY()+sceneHeight/2-450/2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //win.showAndWait();
                close(true);
            });
        });*/

    }


    /**
     * Shows the timer on the board
     * @param val ask to Marco
     */
    public void setTimerText(long val){
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    if(((BoardController) controller).getShowTimer()){
                        ((BoardController) controller).buttonTimer.setVisible(true);
                        ((BoardController) controller).buttonTimer.setText(val+"");
                    }
                }
            } catch (Exception ignored){}
        });
    }


    /**
     * Remove the timer on the banner
     */
    public void unShowTimer(){
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if(controller instanceof BoardController){
                    ((BoardController) controller).unShowTimer(); //.setVisible(false);
                } else LOGGER.log( Level.SEVERE, "UNSHOWTIMER");
            } catch (Exception ex){
                LOGGER.log( Level.SEVERE, ex.toString(), ex );
            }
        });
    }


    /**
     * Start the timer on the banner
     * @param second the current second
     */
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


    /**
     * @param name the name of a Player
     * @return the Player Pointer on the Arraylist of SnapPlayers
     */
    public SnapPlayer getPlayerByName(String name) {
        for (SnapPlayer p : client.getPlayers())
            if (p.name.equals(name))
                return p;
        return null;
    }


    /**
     * Set the loserBanner message
     * Refresh the board
     */
    public void setLoserBar() {
        FXMLLoader loader = (FXMLLoader) primaryStage.getScene().getUserData();
        DefaultController controller = loader.getController();
        if (controller instanceof BoardController) {
            ((BoardController) controller).loserbanner();
            ((BoardController) controller).refresh();

        }
    }
}