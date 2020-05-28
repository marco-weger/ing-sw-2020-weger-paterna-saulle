package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.servermessages.AvailableCardServer;
import it.polimi.ingsw.commons.servermessages.EasterEggServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class EasterEggController extends DefaultController{

    @FXML
    public Button buttonLobby;

    @FXML
    public ImageView imageLobby;
    
    @FXML
    public Button buttonContinue;

    @FXML
    public Button banner;

    private AvailableCardServer last;


    /**
     * set the background image
     * set size of the button
     */
    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_extra.png"));

        buttonQuit.setVisible(false);
        buttonHelper.setVisible(false);

        buttonLobby.setMinSize(450,400);
        buttonLobby.setMaxSize(450,400);
        buttonLobby.setPrefSize(450,400); //240

        imageLobby.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/scoreboard.png"));
        imageLobby.setPreserveRatio(true);
        imageLobby.setFitWidth(406);

        buttonLobby.setAlignment(Pos.CENTER);

        int y = 50;
        int x = 50*250/143;
        buttonContinue.setMinSize(x,y);
        buttonContinue.setMaxSize(x,y);
        buttonContinue.setPrefSize(x,y);

        banner.setMinSize(800,60);
        banner.setMaxSize(800,60);
        banner.setPrefSize(800,60);
        banner.setText("A SECRET IS REVEALED!");

    }

    @Override
    public void setup(){
        super.setup();
        buttonLobby.setPadding(new Insets(30, 0, 0, 0));
        imageLobby.setLayoutX(gui.sceneWidth/2-203);
        imageLobby.setLayoutY(65);
        buttonLobby.setLayoutX(950/2-225);
        buttonLobby.setLayoutY(95);
        buttonLobby.setFont(f);

        buttonContinue.setLayoutY(39);
        buttonContinue.setLayoutX(95);

        banner.setLayoutY(bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX((gui.sceneWidth/2)-400);
    }

    public void setLast(AvailableCardServer last){ this.last=last; }

    public void buttonContinue(ActionEvent actionEvent) {
        gui.getClient().runAfterEasterEgg(last);
    }
}
