package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ModeChoseClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class ModeController extends DefaultController{

    @FXML
    public Button button2;

    @FXML
    public Button button3;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_mode2.png"));

        button2.setMinSize(200,114);
        button2.setMaxSize(200,114);
        button2.setPrefSize(200,114);
        button3.setMinSize(200,114);
        button3.setMaxSize(200,114);
        button3.setPrefSize(200,114);
    }

    @Override
    public void setup(){
        super.setup();
        button3.setLayoutX(gui.sceneWidth-400);
        button2.setLayoutX(200);
        button2.setLayoutY(gui.sceneHeight/2);
        button3.setLayoutY(gui.sceneHeight/2);
    }

    public void button2OnAction(ActionEvent actionEvent) {
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),2));
    }

    public void button3OnAction(ActionEvent actionEvent) {
        this.gui.getClient().sendMessage(new ModeChoseClient(this.gui.getClient().getUsername(),3));
    }
}
