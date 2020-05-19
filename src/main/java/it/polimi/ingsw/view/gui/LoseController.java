package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class LoseController extends DefaultController{

    public void setSomeonewinflag(boolean someonewinflag) {
        this.someonewinflag = someonewinflag;
    }

    private boolean someonewinflag;

    @FXML
    public Button Spectator;

    @FXML
    public Button NewGame;

    @FXML
    public AnchorPane anchorLose;

    @FXML
    @Override
    public void initialize() {
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/lose.png"));

        int x=125, y=71;

        Spectator.setMinSize(x,y);
        Spectator.setMaxSize(x,y);
        Spectator.setPrefSize(x,y);
        NewGame.setMinSize(x,y);
        NewGame.setMaxSize(x,y);
        NewGame.setPrefSize(x,y);
        someonewinflag = false;

        //
    }

    public void spectator(ActionEvent actionEvent) {
        if(this.gui.getClient().getPlayers().size() > 1 && !someonewinflag) {
            this.gui.getLose().close();
            this.gui.setLoserBar();
            Spectator.getStyleClass().remove("spectator");
        }
        else{
            this.gui.getLose().close();
            Platform.exit();
            System.exit(0);
        }
    }

    public void newgame(ActionEvent actionEvent) {
        this.gui.getClient().resetMatch();
        this.gui.getLose().close();
        Platform.runLater(() -> {
            this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            this.gui.getPrimaryStage().show();
        });
        //this.gui.getClient().sendMessage(new ModeChoseClient(gui.getClient().getUsername(),Integer.parseInt(text)));
    }

    @Override
    public void setBackground(Image image){
        anchorLose.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }
}

