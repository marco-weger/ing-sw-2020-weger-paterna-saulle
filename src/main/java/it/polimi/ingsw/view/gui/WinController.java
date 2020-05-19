package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class WinController extends DefaultController {

    @FXML
    public Button Exit;

    @FXML
    public Button NewGame;

    @FXML
    public AnchorPane anchorWin;

    @FXML
    @Override
    public void initialize() {
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/win.png"));

        int x=125, y=71;

        Exit.setMinSize(x,y);
        Exit.setMaxSize(x,y);
        Exit.setPrefSize(x,y);
        NewGame.setMinSize(x,y);
        NewGame.setMaxSize(x,y);
        NewGame.setPrefSize(x,y);

        anchorWin.setStyle("-fx-border-color: green; -fx-border-width: 1px 1px 1px 1px");
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void newgame(ActionEvent actionEvent) {
        this.gui.getClient().resetMatch();
        this.gui.getWin().close();
        Platform.runLater(() -> {
            this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            this.gui.getPrimaryStage().show();
        });
        //this.gui.getClient().sendMessage(new ModeChoseClient(gui.getClient().getUsername(),Integer.parseInt(text)));
    }

    @Override
    public void setBackground(Image image){
        anchorWin.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }
}
