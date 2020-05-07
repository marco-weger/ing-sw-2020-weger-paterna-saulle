package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HomeController extends DefaultController{

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    @FXML
    public Button buttonPlay;

    @FXML
    public Button buttonHelp;


    @FXML
    @Override
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

    public void handleHelpButton(javafx.event.ActionEvent actionEvent) {
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


        public void handlePlayButton(javafx.event.ActionEvent actionEvent) {
            Parent root;

            try{
                root = playloader.load();
                mainstage.setScene(new Scene(root));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            mainstage.show();
    }


}
