package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class HomeController extends DefaultController{

    @FXML
    public Button buttonPlay;

    @FXML
    public Button buttonHelp;


    @FXML
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_home.png"));

        int size = 200;
        buttonPlay.setPrefSize(size,size);
        buttonPlay.setMaxSize(size,size);
        buttonPlay.setMinSize(size,size);
        buttonHelp.setPrefSize(size,size);
        buttonHelp.setMaxSize(size,size);
        buttonHelp.setMinSize(size,size);
    }

    public void HandleHelpButton(javafx.event.ActionEvent actionEvent) {
        Parent root;
    try{
        root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Rules.fxml"));
        Stage rules = new Stage();
        rules.setTitle("Santorini RulesController");
        rules.setScene(new Scene(root));
        rules.setHeight(1000);
        rules.setWidth(1290);
        rules.setMinHeight(1000);
        rules.setMinWidth(1290);
        rules.setMaxWidth(1290);
        rules.setMaxHeight(800);
        rules.initStyle(StageStyle.TRANSPARENT);
        rules.getIcons().add(new Image("/it.polimi.ingsw/view/gui/img/icon.png"));
        rules.getScene().setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));

        rules.show();
    }
    catch (IOException e) {
            e.printStackTrace();

    }
    }

}
