package it.polimi.ingsw.view.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
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
        textAreaLobby.setMinSize(400,275);
        textAreaLobby.setMaxSize(400,275);
        textAreaLobby.setPrefSize(400,275);

        imageLobby.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/lobby.png"));
        imageLobby.setPreserveRatio(true);
        imageLobby.setFitWidth(406);
        //imageLobby.set
    }

    @Override
    public void setup(){
        super.setup();
        imageLobby.setLayoutX(gui.sceneWidth/2-203);
        imageLobby.setLayoutY(65);
        textAreaLobby.setLayoutX(gui.sceneWidth/2-200);
        textAreaLobby.setLayoutY(73);

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
