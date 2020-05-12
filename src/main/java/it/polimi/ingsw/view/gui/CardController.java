package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ChallengerChoseClient;
import it.polimi.ingsw.commons.clientmessages.PlayerChoseClient;
import it.polimi.ingsw.model.cards.CardName;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardController extends DefaultController {


    public Button button1;
    public Button button2;
    public Button button3;
    public Button buttonSend;

    public CardName cardName;

    public List<CardName> cards;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_card.png"));
    }

    @Override
    public void setup(){
        super.setup();
        int y = 25;
    }

    public void button1OnAction(ActionEvent actionEvent) {
        cardName = cards.get(0);
    }

    public void button2OnAction(ActionEvent actionEvent) {
        cardName = cards.get(1);
    }

    public void button3OnAction(ActionEvent actionEvent) {
        cardName = cards.get(2);
    }

    public void buttonSend(ActionEvent actionEvent) {
        gui.getClient().sendMessage(new PlayerChoseClient(gui.getClient().getUsername(), cardName));
    }
}
