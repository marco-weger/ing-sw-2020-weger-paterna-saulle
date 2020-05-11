package it.polimi.ingsw.view.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LobbyController extends DefaultController {

    public TextArea textAreaLobby;
    public ImageView imageLobby;

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_lobby.png"));
        textAreaLobby.setMinSize(450,240);
        textAreaLobby.setMaxSize(450,260);
        textAreaLobby.setPrefSize(450,260);

        imageLobby.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/lobby.png"));
        imageLobby.setPreserveRatio(true);
        imageLobby.setFitWidth(406);
    }

    @Override
    public void setup(){
        super.setup();
        imageLobby.setLayoutX(gui.sceneWidth/2-203);
        imageLobby.setLayoutY(65);
        //textAreaLobby.setLayoutX(gui.sceneWidth/2-200);
        textAreaLobby.setLayoutX(950/2-225);
        textAreaLobby.setLayoutY(95);

        textAreaLobby.setFont(f);
        textAreaLobby.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                textAreaLobby.setFont(f);
            }
        });
    }
}
