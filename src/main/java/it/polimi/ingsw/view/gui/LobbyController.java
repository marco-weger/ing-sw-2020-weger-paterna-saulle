package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LobbyController extends DefaultController {

    @FXML
    public Button buttonLobby;

    @FXML
    public ImageView imageLobby;

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
    }
}
