package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ChallengerChoseClient;
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

public class CardChallengerController extends DefaultController {

    @FXML
    public ImageView imageViewCard;
    
    @FXML
    public Button buttonRight;
    
    @FXML
    public Button buttonLeft;

    @FXML
    public ImageView imageViewFrame;

    @FXML
    public TextArea labelDescription;

    @FXML
    public Button buttonAccept;

    @FXML
    public TextField labelName;
    public Button buttonSend;
    public Label labelTEST;

    List<CardName> cards;

    int count = 0;
    
    List<CardName> selected;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_card.png"));
        cards = Arrays.asList(CardName.values());
        imageViewFrame.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/frame.png"));
        setCard();
        selected = new ArrayList<>();

        imageViewCard.setFitWidth(163);
        imageViewCard.setFitHeight(220);
        imageViewCard.setPreserveRatio(true);

        imageViewFrame.setFitWidth(169);
        imageViewFrame.setFitHeight(228);
        imageViewFrame.setPreserveRatio(true);

        buttonRight.setMinSize(50,50);
        buttonRight.setMaxSize(50,50);
        buttonRight.setPrefSize(50,50);
        buttonLeft.setMinSize(50,50);
        buttonLeft.setMaxSize(50,50);
        buttonLeft.setPrefSize(50,50);

        labelDescription.setMinSize(525,190);
        labelDescription.setMaxSize(525,190);
        labelDescription.setPrefSize(525,190);

        buttonAccept.setMinSize(80,30);
        buttonAccept.setMaxSize(80,30);
        buttonAccept.setPrefSize(80,30);

        labelName.setMinSize(220,48);
        labelName.setMaxSize(220,48);
        labelName.setPrefSize(220,48);
        labelName.setEditable(false);

        buttonSend.setDisable(true);
    }

    @Override
    public void setup(){
        super.setup();
        int y = 25;
        imageViewFrame.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2);
        imageViewFrame.setLayoutY(y-1);
        imageViewCard.setLayoutX(120);
        imageViewCard.setLayoutY(y);

        buttonLeft.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2-60);
        buttonLeft.setLayoutY(y+imageViewFrame.getFitHeight()/2-25);
        buttonRight.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2+10+169);
        buttonRight.setLayoutY(y+imageViewFrame.getFitHeight()/2-25);

        labelDescription.setLayoutY(y+38);
        labelDescription.setLayoutX(400);

        buttonAccept.setLayoutY(y);
        buttonAccept.setLayoutX(400+525-80);

        labelName.setLayoutY(y);
        labelName.setLayoutX(400);
        setUpTextField(labelName);
        labelDescription.setFont(f);
        labelDescription.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                labelDescription.setFont(f);
            }
        });
    }

    public void rightCard(ActionEvent actionEvent) {
        if(++count == cards.size()) count = 0;
        setCard();
    }

    public void leftCard(ActionEvent actionEvent) {
        if(--count == -1) count = cards.size()-1;
        setCard();
    }

    public void setCard(){
        try{
            imageViewCard.setImage(new Image("/it.polimi.ingsw/view/gui/img/card/"+cards.get(count).toString().toLowerCase()+".png"));
            labelName.setText(cards.get(count).toString());
            String[] splitted = cards.get(count).getDescription().split(" ");
            String desc = "";
            int cc = 0;
            for(int i=0; i<splitted.length; i++){
                cc += splitted[i].length();
                desc += splitted[i];
                if(cc > 38){
                    cc = 0;
                    desc += (i+1 == splitted.length ? "" : "\n");
                } else desc += " ";
            }
            labelDescription.setText(desc);
        } catch (Exception ex){
            imageViewCard.setImage(new Image("/it.polimi.ingsw/view/gui/img/card/random.png"));
            labelName.setText(cards.get(count).toString());
            labelDescription.setText("");
        }
    }

    public void acceptOnAction(ActionEvent actionEvent) {
        if(!selected.contains(cards.get(count))){
            selected.add(cards.get(count));
            labelTEST.setText(selected.size()+"/"+gui.getClient().getPlayers().size());
            if(selected.size() == gui.getClient().getPlayers().size()){
                buttonSend.setDisable(false);
            }
        }
    }

    public void sendOnAction(ActionEvent actionEvent){
        if(selected.size() == gui.getClient().getPlayers().size()){
            gui.getClient().sendMessage(new ChallengerChoseClient(gui.getClient().getUsername(), selected));
        }
    }
}
