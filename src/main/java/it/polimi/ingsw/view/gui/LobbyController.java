package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ModeChoseClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LobbyController extends DefaultController {

    @FXML
    public Button new2;

    @FXML
    public Button new3;

    @FXML
    public Button banner;

    private boolean reconnection;

    @FXML
    public Button buttonLobby;
    
    //@FXML
    //public Button newGame;

    @FXML
    public ImageView imageLobby;

    /**
     * set the background image
     * set lobby logo
     * set reconnected question
     */
    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_lobby.png"));
        buttonLobby.setMinSize(450,240);
        buttonLobby.setMaxSize(450,240);
        buttonLobby.setPrefSize(450,240); //240

        imageLobby.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/lobby.png"));
        imageLobby.setPreserveRatio(true);
        imageLobby.setFitWidth(406);
        reconnection = false;

        int y = 50;
        int x = 50*250/143;
        new2.setMinSize(x,y);
        new2.setMaxSize(x,y);
        new2.setPrefSize(x,y);
        new3.setMinSize(x,y);
        new3.setMaxSize(x,y);
        new3.setPrefSize(x,y);

        banner.setText("Match resumed... Click if you want to refuse!");
        banner.setVisible(false);

        new2.setVisible(false);
        new3.setVisible(false);
    }

    /**
     * set buttons and images
     */
    @Override
    public void setup(){
        super.setup();
        buttonLobby.setPadding(new Insets(30, 0, 0, 0));
        imageLobby.setLayoutX(gui.sceneWidth/2-203);
        imageLobby.setLayoutY(65);
        buttonLobby.setLayoutX(950/2-225);
        buttonLobby.setLayoutY(95);
        buttonLobby.setFont(f);

        new3.setLayoutY(bottom.getPrefHeight()/2-new3.getPrefHeight()/2);
        new3.setLayoutX(gui.sceneWidth-200);

        new2.setLayoutY(bottom.getPrefHeight()/2-new2.getPrefHeight()/2);
        new2.setLayoutX(new3.getLayoutX()-new2.getPrefWidth()-15);

        banner.setMinSize(new2.getLayoutX(),60);
        banner.setMaxSize(new2.getLayoutX(),60);
        banner.setPrefSize(new2.getLayoutX(),60);

        banner.setLayoutY(bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX(0);
    }

    /**
     * constructor
     */
    public void setReconnection(boolean reconnection) {
        this.reconnection = reconnection;
    }

    /**
     * join a new game lobby for 2 players.
     * @param actionEvent newgame2 button clicked.
     */
    public void newgame2(ActionEvent actionEvent) {
        if(reconnection) {
            this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(), 2, true));
            reconnection = false;
        }
        this.gui.getClient().setMustPrint(false);
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),2));
    }

    /**
     * join a new game lobby for 3 players.
     * @param actionEvent newgame3 button clicked.
     */
    public void newgame3(ActionEvent actionEvent) {
        if (reconnection) {
            this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),3,true));
            reconnection = false;
        }
        this.gui.getClient().setMustPrint(false);
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),3));
    }
}
