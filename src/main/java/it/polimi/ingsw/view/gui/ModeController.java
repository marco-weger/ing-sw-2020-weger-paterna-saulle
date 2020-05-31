package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ModeChoseClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    public void setReconnection(boolean reconnection) {
        this.reconnection = reconnection;
    }

    private boolean reconnection;

    @FXML
    public Button button2;

    @FXML
    public Button button3;

    /**
     * set the background image
     * set buttons
     */
    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode.png"));

        button2.setMinSize(200,114);
        button2.setMaxSize(200,114);
        button2.setPrefSize(200,114);
        button3.setMinSize(200,114);
        button3.setMaxSize(200,114);
        button3.setPrefSize(200,114);
        reconnection = false;
    }

    /**
     * Initialize background and buttons.
     */
    @Override
    public void setup(){
        super.setup();
        button3.setLayoutX(gui.sceneWidth-400);
        button2.setLayoutX(200);
        button2.setLayoutY(gui.sceneHeight/2);
        button3.setLayoutY(gui.sceneHeight/2);
    }

    /**
     * Once clicked it redirects to the current lobby of the 2 players mode.
     * @param actionEvent
     */
    public void button2OnAction(ActionEvent actionEvent) {
        if(reconnection) {
            this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(), 2, true));
            reconnection = false;
        }
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),2));
    }

    /**
     * Once clicked it redirects to the current lobby of the 3 players mode.
     * @param actionEvent
     */
    public void button3OnAction(ActionEvent actionEvent) {
        if (reconnection) {
            this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),3,true));
            reconnection = false;
        }
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),3));
    }
}
