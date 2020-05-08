package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DefaultController{

    protected Client client;

    @FXML
    public AnchorPane top;

    @FXML
    public AnchorPane bottom;

    @FXML
    public AnchorPane center;

    @FXML
    public Button buttonQuit;

    int initialX = 0;
    int initialY = 0;
    Stage mainstage;
    FXMLLoader playloader;

    @FXML
    public void initialize(){

        mainstage = new Stage();
        mainstage.setTitle("Santorini");
        mainstage.initStyle(StageStyle.TRANSPARENT);

        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        top.setBackground(new Background(new BackgroundImage(new Image("/it.polimi.ingsw/view/gui/img/scene/top.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        bottom.setBackground(new Background(new BackgroundImage(new Image("/it.polimi.ingsw/view/gui/img/scene/bottom.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
    }



    public void setClient(Client client){ this.client=client; }

    public void setBackground(Image image){
        center.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }

    public void topPressed(MouseEvent mouseEvent) {
        initialX = (int) (mainstage.getX() - mouseEvent.getScreenX());
        initialY = (int) (mainstage.getY() - mouseEvent.getScreenY());
    }

    public void topDragged(MouseEvent mouseEvent) {
        mainstage.setX(mouseEvent.getScreenX() + initialX);
        mainstage.setY(mouseEvent.getScreenY() + initialY);
    }

    public void quitOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Santorini Exit");
        alert.setHeaderText("Sei sicuro?");
        alert.setContentText("Marco riceve un biscotto per ogni partita in piu!");
        alert.initStyle(StageStyle.UNIFIED);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }



}

