package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.DisconnectionClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ExitController extends  DefaultController{

    @FXML
    public Button Yes;

    @FXML
    public Button No;

    @FXML
    public AnchorPane anchorExit;


    /**
     * set the background image
     * set size of the button
     */
    @FXML
    @Override
    public void initialize() {
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/exit.png"));

        int x=125, y=71;

        No.setMinSize(x,y);
        No.setMaxSize(x,y);
        No.setPrefSize(x,y);
        Yes.setMinSize(x,y);
        Yes.setMaxSize(x,y);
        Yes.setPrefSize(x,y);

        //anchorWin.setStyle("-fx-border-color: green; -fx-border-width: 1px 1px 1px 1px");
    }

    /**
     * Send a disconnection message, close the game.
     * @param actionEvent yes button clicked
     */
    public void yes(ActionEvent actionEvent) {
        gui.getClient().disconnectionHandler();
        this.gui.getClient().sendMessage(new DisconnectionClient(this.gui.getClient().getUsername(),false));
        System.exit(0);
    }

    /**
     * Remove the Exit PopUp
     * @param actionEvent no button clicked
     */
    public void no(ActionEvent actionEvent) {
        Stage stage = (Stage) No.getScene().getWindow();
        stage.close();
    }


    /**
     * Set the background of the current Scene
     * @param image the background image
     */
    @Override
    public void setBackground(Image image){
        anchorExit.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }
}
