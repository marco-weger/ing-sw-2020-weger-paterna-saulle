package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ErrorController extends  DefaultController{

    @FXML
    public Button Quit;

    @FXML
    public AnchorPane anchorError;


    /**
     * set the background image
     * set size of the button
     */
    @FXML
    @Override
    public void initialize() {
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/error.png"));

        int x=125, y=71;
        Quit.setMinSize(x,y);
        Quit.setMaxSize(x,y);
        Quit.setPrefSize(x,y);
    }


    /**
     * close the error PopUp
     * close the game
     * @param actionEvent quit button clicked
     */
    public void quit(ActionEvent actionEvent) {
        //gui.getClient().disconnectionHandler();
        gui.close(true);
        System.exit(0);
    }


    /**
     * Set the background of the current Scene
     * @param image the background image
     */
    @Override
    public void setBackground(Image image){
        anchorError.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }
}
