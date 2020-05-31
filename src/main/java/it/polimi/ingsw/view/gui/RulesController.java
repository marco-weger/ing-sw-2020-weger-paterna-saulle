package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RulesController {

    @FXML
    public Button buttonExit;

    /**
     * Once clicked it close the window whit the rules
     * of the game.
     * @param actionEvent
     */
    public void exitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
    }

}
