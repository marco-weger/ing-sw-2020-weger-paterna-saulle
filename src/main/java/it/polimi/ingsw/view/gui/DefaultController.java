package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DefaultController {

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
    Stage stage;

    @FXML
    public void initialize(){
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

        //top.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
    }

    public void setBackground(Image image){
        center.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }

    public void topPressed(MouseEvent mouseEvent) {
        initialX = (int) (stage.getX() - mouseEvent.getScreenX());
        initialY = (int) (stage.getY() - mouseEvent.getScreenY());
    }

    public void topDragged(MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX() + initialX);
        stage.setY(mouseEvent.getScreenY() + initialY);
    }

    public void quitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonQuit.getScene().getWindow();
        new Alert(Alert.AlertType.ERROR, "SHOW IF YOU ARE SURE !").showAndWait();
        stage.close();
    }

}
