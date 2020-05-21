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

    public void setReconnection(boolean reconnection) {
        this.reconnection = reconnection;
    }

    private boolean reconnection;

    @FXML
    public Button buttonLobby;
    
    @FXML
    public Button newGame;

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
        newGame.setMinSize(200,114);
        newGame.setMaxSize(200,114);
        newGame.setPrefSize(200,114);


        imageLobby.setImage(new Image("/it.polimi.ingsw/view/gui/img/other/lobby.png"));
        imageLobby.setPreserveRatio(true);
        imageLobby.setFitWidth(406);
        reconnection = false;
    }

    @Override
    public void setup(){
        super.setup();
        buttonLobby.setPadding(new Insets(30, 0, 0, 0));
        imageLobby.setLayoutX(gui.sceneWidth/2-203);
        imageLobby.setLayoutY(65);
        newGame.setLayoutX(950/2 -103);
        buttonLobby.setLayoutX(950/2-225);
        buttonLobby.setLayoutY(95);
        buttonLobby.setFont(f);
        newGame.getStyleClass().add("empty");
    }

    public void newgame(ActionEvent actionEvent) {
        if (reconnection == true){
            gui.getClient().resetMatch();
        Platform.runLater(() -> {
                this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
                FXMLLoader loader = (FXMLLoader) mainstage.getScene().getUserData();
                DefaultController controller = loader.getController();
                if (controller instanceof ModeController) {
                     ((ModeController) controller).setReconnection(true);
                 }
                newGame.getStyleClass().remove("newgame");
                newGame.getStyleClass().add("empty");
        });
        this.setReconnection(false);
        }
    }

    public void rec(){
        this.setReconnection(true);
        newGame.getStyleClass().remove("empty");
        newGame.getStyleClass().add("newgame");
    }
}
