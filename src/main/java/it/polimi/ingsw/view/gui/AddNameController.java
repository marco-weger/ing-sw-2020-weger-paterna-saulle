package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import it.polimi.ingsw.view.gui.HomeController;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNameController extends DefaultController {


    private static final Logger LOGGER = Logger.getLogger(AddNameController.class.getName());
    public Button buttonLogin;
    public TextField insertname;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name.png"));


        int sizex = 230;
        int sizey = 100;
        buttonLogin.setPrefSize(sizex, sizey);
        buttonLogin.setMaxSize(sizex, sizey);
        buttonLogin.setMinSize(sizex, sizey);
    }

    public void handleLoginButton(ActionEvent login) {
        Parent ModeParent;
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Mode.fxml"));
            ModeParent = loader.load();
            Scene ModeScene = new Scene(ModeParent);
            ModeScene.setCursor(new ImageCursor(new Image("/it.polimi.ingsw/view/gui/img/pointer.png")));
            mainstage.setScene(ModeScene);
            mainstage.show();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }

    }


}
