package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ChallengerChoseClient;
import it.polimi.ingsw.commons.clientmessages.EasterEggClient;
import it.polimi.ingsw.model.cards.CardName;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengerController extends DefaultController {

    @FXML
    public ImageView imageViewCard;

    @FXML
    public Button buttonRight;

    @FXML
    public Button buttonLeft;

    @FXML
    public ImageView imageViewFrame;

    @FXML
    public Button buttonDescription;

    @FXML
    public Button buttonAccept;

    @FXML
    public TextField textFieldName;

    @FXML
    public Button buttonSend;

    @FXML
    public Button buttonSelected1;

    @FXML
    public Button buttonSelected2;

    @FXML
    public Button buttonSelected3;

    @FXML
    public Button buttonGod1;

    @FXML
    public Button buttonGod2;

    @FXML
    public Button buttonGod3;

    @FXML
    public Button buttonClear;

    public List<CardName> cards;

    @FXML
    public Button banner;

    boolean eeBlock = false;

    int count = 0;

    List<CardName> selected;

    private int eeCounter;


    /**
     * set the background image
     * set the image in the frame
     * set size of the button
     */
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

        buttonDescription.setMinSize(525,175);
        buttonDescription.setMaxSize(525,175);
        buttonDescription.setPrefSize(525,175);

        buttonAccept.setMinSize(197,48);
        buttonAccept.setMaxSize(197,48);
        buttonAccept.setPrefSize(197,48);

        textFieldName.setMinSize(220,48);
        textFieldName.setMaxSize(220,48);
        textFieldName.setPrefSize(220,48);
        textFieldName.setEditable(false);

        buttonSend.setDisable(true);
        buttonSend.setVisible(false);

        double w = 170;
        double h = 180;
        buttonSelected1.setPrefSize(w,88);
        buttonSelected2.setPrefSize(w,88);
        buttonSelected3.setPrefSize(w,88);

        buttonSend.setPrefSize(150,42);
        buttonClear.setPrefSize(150,42);
        buttonClear.setPrefSize(150,42);

        w = 185;
        buttonGod1.setMinSize(w,h);
        buttonGod1.setMaxSize(w,h);
        buttonGod1.setPrefSize(w,h);
        buttonGod2.setMinSize(w,h);
        buttonGod2.setMaxSize(w,h);
        buttonGod2.setPrefSize(w,h);
        buttonGod3.setMinSize(w,h);
        buttonGod3.setMaxSize(w,h);
        buttonGod3.setPrefSize(w,h);

        buttonSend.setMinSize(154,88);
        buttonSend.setMaxSize(154,88);
        buttonSend.setPrefSize(154,88);
        buttonClear.setMinSize(154,88);
        buttonClear.setMaxSize(154,88);
        buttonClear.setPrefSize(154,88);

        banner.setMinSize(800,60);
        banner.setMaxSize(800,60);
        banner.setPrefSize(800,60);
        banner.setText("Waiting for opponent's choice...");
        banner.setVisible(false);

        eeCounter = 0;
    }


    /**
     * set the button position
     * set the Textfield position
     */
    @Override
    public void setup(){
        super.setup();
        int y = 25;

        banner.setLayoutY(bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX((gui.sceneWidth/2)-400);

        if(gui.getClient().getPlayers().size() == 3){
            int offset = 45;
            buttonSelected1.setLayoutX(gui.sceneWidth/4 - buttonSelected1.getPrefWidth()/2+offset);
            buttonSelected2.setLayoutX(gui.sceneWidth/2 - buttonSelected2.getPrefWidth()/2);
            buttonSelected3.setLayoutX(3*gui.sceneWidth/4 - buttonSelected3.getPrefWidth()/2-offset);

            buttonGod1.setLayoutX(gui.sceneWidth/4 - buttonGod1.getPrefWidth()/2+offset);
            buttonGod2.setLayoutX(gui.sceneWidth/2 - buttonGod2.getPrefWidth()/2);
            buttonGod3.setLayoutX(3*gui.sceneWidth/4 - buttonGod3.getPrefWidth()/2-offset);
        } else if(gui.getClient().getPlayers().size() == 2){
            int offset = 45;
            buttonSelected1.setLayoutX(gui.sceneWidth/3 - buttonSelected1.getPrefWidth()/2+offset);
            buttonSelected2.setLayoutX(2*gui.sceneWidth/3 - buttonSelected2.getPrefWidth()/2-offset);
            buttonSelected3.setVisible(false);

            buttonGod1.setLayoutX(gui.sceneWidth/3 - buttonGod1.getPrefWidth()/2+offset);
            buttonGod2.setLayoutX(2*gui.sceneWidth/3 - buttonGod2.getPrefWidth()/2-offset);
            buttonGod3.setVisible(false);
        }

        buttonSelected1.setLayoutY(425);
        buttonSelected2.setLayoutY(425);
        buttonSelected3.setLayoutY(425);

        buttonClear.setLayoutY(425);
        buttonSend.setLayoutY(425);
        int offset = 30;
        buttonClear.setLayoutX(offset);
        buttonSend.setLayoutX(gui.sceneWidth-offset-150);

        buttonGod1.setLayoutY(282);
        buttonGod2.setLayoutY(282);
        buttonGod3.setLayoutY(282);

        imageViewFrame.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2);
        imageViewFrame.setLayoutY(y-1);
        imageViewCard.setLayoutX(120);
        imageViewCard.setLayoutY(y);

        buttonLeft.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2-60);
        buttonLeft.setLayoutY(y+imageViewFrame.getFitHeight()/2-25);
        buttonRight.setLayoutX(gui.sceneWidth/5-imageViewFrame.getFitWidth()/2+10+169);
        buttonRight.setLayoutY(y+imageViewFrame.getFitHeight()/2-25);

        buttonDescription.setLayoutY(y+imageViewFrame.getFitHeight()-buttonDescription.getPrefHeight());
        buttonDescription.setLayoutX(400);

        buttonAccept.setLayoutY(y);
        buttonAccept.setLayoutX(400+525-197);

        textFieldName.setLayoutY(y);
        textFieldName.setLayoutX(400);
        setUpTextField(textFieldName);
        buttonDescription.setFont(f);

        eeCounter = 0;
    }


    /**
     * Change the card in the frame
     * @param actionEvent right button clicked
     */
    public void rightCard(ActionEvent actionEvent) {
        eeCounter = 0;
        if(++count == cards.size()) count = 0;
        setCard();
    }


    /**
     * Change the card in the frame
     * @param actionEvent left button clicked
     */
    public void leftCard(ActionEvent actionEvent) {
        eeCounter = 0;
        if(--count == -1) count = cards.size()-1;
        setCard();
    }


    /**
     * set the current card image, and write him description in the banner
     */
    public void setCard(){
        eeCounter = 0;
        try{
            imageViewCard.setImage(new Image("/it.polimi.ingsw/view/gui/img/card/"+cards.get(count).toString().toLowerCase()+".png"));
            textFieldName.setText(cards.get(count).toString());
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
            buttonDescription.setText(desc);
        } catch (Exception ex){
            imageViewCard.setImage(new Image("/it.polimi.ingsw/view/gui/img/card/random.png"));
            textFieldName.setText(cards.get(count).toString());
            buttonDescription.setText("");
        }
    }


    /**
     * Choice the current card
     * @param actionEvent accept button clicked
     */
    public void acceptOnAction(ActionEvent actionEvent) {
        eeCounter = 0;
        if(!selected.contains(cards.get(count))){
            selected.add(cards.get(count));
            showSelected();
            if(selected.size() == gui.getClient().getPlayers().size()){
                buttonSend.setDisable(false);
                buttonSend.setVisible(true);
            }
        }
    }


    /**
     * Send the chosen cards to the opponents
     * @param actionEvent send button clicked
     */
    public void sendOnAction(ActionEvent actionEvent){
        eeCounter = 0;
        if(selected.size() == gui.getClient().getPlayers().size()){
            gui.getClient().sendMessage(new ChallengerChoseClient(gui.getClient().getUsername(), selected));
            banner.setVisible(true);
            buttonSend.setDisable(true);
            buttonClear.setDisable(true);
            eeBlock = true;
        }
    }


    /**
     * remove the chosen god
     * remove the send button
     * reload the old chosen god
     * @param actionEvent clear button clicked
     */
    public void clearOnAction(ActionEvent actionEvent) {
        eeCounter = 0;
        selected.clear();
        buttonSend.setDisable(true);
        buttonSend.setVisible(false);
        showSelected();
    }


    /**
     * show the selected gods in the Gods Chosen Zone
     */
    public void showSelected(){
        eeCounter = 0;
        buttonGod1.getStyleClass().clear();
        buttonGod2.getStyleClass().clear();
        buttonGod3.getStyleClass().clear();
        for(int i = 0; i < selected.size(); i++){
            switch (i){
                case  0:
                    buttonGod1.getStyleClass().addAll("button",selected.get(i).toString().toLowerCase());
                    break;
                case  1:
                    buttonGod2.getStyleClass().addAll("button",selected.get(i).toString().toLowerCase());
                    break;
                case  2:
                    buttonGod3.getStyleClass().addAll("button",selected.get(i).toString().toLowerCase());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * EASTER EGG, NO SPOILER
     * @param actionEvent 5 click on Something in this page
     */
    public void easterEgg(ActionEvent actionEvent) {
        if(++eeCounter==5 && !eeBlock)
            gui.getClient().sendMessage(new EasterEggClient(gui.getClient().getUsername()));
    }


    /**
     * shows helper
     * reset the easter egg counter
     */
    @Override
    public void showHelper(){
        super.showHelper();
        eeCounter = 0;
    }


    /**
     * shows the exit popUp
     * reset the easter egg counter
     * @param actionEvent exit button clicked
     */
    @Override
    public void quitOnAction(ActionEvent actionEvent) {
        super.quitOnAction(actionEvent);
        eeCounter = 0;
    }


    /**
     * Store the initial position of the windows before been moved
     * @param mouseEvent mouse click
     */
    @Override
    public void topPressed(MouseEvent mouseEvent) {
        super.topPressed(mouseEvent);
        eeCounter = 0;
    }


     /**
     * store the position of the Stage when the windows's moved
     * @param mouseEvent mouse drag
     */
    @Override
    public void topDragged(MouseEvent mouseEvent) {
        super.topDragged(mouseEvent);
        eeCounter = 0;
    }
}
