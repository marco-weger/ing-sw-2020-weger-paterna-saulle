package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultController{

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultController.class.getName());

    protected Client client;

    @FXML
    public AnchorPane top;

    @FXML
    public AnchorPane bottom;

    @FXML
    public AnchorPane center;

    @FXML
    public Button buttonQuit;

    @FXML
    public Button buttonHelper;

    int initialX = 0;
    int initialY = 0;
    Stage mainstage;
    //FXMLLoader playloader;

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

        /*
        buttonQuit.setLayoutX(mainstage.getWidth()-100);
        buttonQuit.setLayoutY(50);
        buttonHelper.setLayoutX(mainstage.getWidth()-200);
        buttonHelper.setLayoutY(50);
         */
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
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void helperOnAction(ActionEvent actionEvent) {
        showHelper();
    }

    public void showHelper(){
        Parent root;
        try{
            root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Rules.fxml"));
            Stage rules = new Stage();
            rules.setTitle("Santorini Rules");
            rules.setScene(new Scene(root));

            int width = 1300;

            if(Screen.getPrimary().getBounds().getWidth() < width)
                width = (int) Screen.getPrimary().getBounds().getWidth();

            rules.setHeight(Screen.getPrimary().getBounds().getHeight()-100);
            rules.setWidth(width);
            rules.setMinHeight(Screen.getPrimary().getBounds().getHeight()-100);
            rules.setMinWidth(width);
            rules.setMaxWidth(width);
            rules.setMaxHeight(Screen.getPrimary().getBounds().getHeight()-100);
            rules.initStyle(StageStyle.TRANSPARENT);
            rules.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
            rules.getScene().setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

            rules.show();
        }
        catch (IOException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }
    }

    public GUI getCurrentView(){
        if(client.getView() instanceof GUI)
           return (GUI)client.getView();
        else return null;
    }

}

