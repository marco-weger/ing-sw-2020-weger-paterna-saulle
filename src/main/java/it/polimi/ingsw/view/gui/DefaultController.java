package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultController{

    /**
     * The logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DefaultController.class.getName());

    Font f = Font.loadFont(getClass().getResourceAsStream("/it.polimi.ingsw/view/gui/font/Nefelibata-Brush.ttf"),13);
    private static final String CURSOR = "/it.polimi.ingsw/view/gui/img/pointer.png";


    //protected Client client;
    protected GUI gui;

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

    public Stage getExit() {
        return exit;
    }

    Stage exit;


    /**
     * set the background image
     * set size of the button
     */
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

    public void setup(){}

    public void setGUI(GUI gui){ this.gui=gui; }


    /**
     * Set the background of the current Scene
     * @param image the background image
     */
    public void setBackground(Image image){
        center.setBackground(new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, BackgroundSize.AUTO, true, false, true, true))));
    }


    /**
     * Store the initial position of the windows before been moved
     * @param mouseEvent mouse click
     */
    public void topPressed(MouseEvent mouseEvent) {
        initialX = (int) (mainstage.getX() - mouseEvent.getScreenX());
        initialY = (int) (mainstage.getY() - mouseEvent.getScreenY());
    }

    /**
     * store the position of the Stage when the windows's moved
     * @param mouseEvent mouse drag
     */
    public void topDragged(MouseEvent mouseEvent) {
        mainstage.setX(mouseEvent.getScreenX() + initialX);
        mainstage.setY(mouseEvent.getScreenY() + initialY);
    }


    /**
     * When clicked load the EXIT PopUp
     * @param actionEvent exit button clicked
     */
    public void quitOnAction(ActionEvent actionEvent) {
      //  Platform.runLater(() -> {
           exit = new Stage();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw/view/gui/fxml/Exit.fxml"));
                Parent root = loader.load();
                this.gui.defaultcontroller = loader.getController();
                this.gui.defaultcontroller.setGUI(this.gui);
                Scene scene = new Scene(Objects.requireNonNull(root), 421, 450, Color.TRANSPARENT);
                scene.setCursor(new ImageCursor(new Image(CURSOR), 36, 45));
                exit.initStyle(StageStyle.TRANSPARENT);
                exit.setAlwaysOnTop(true);
                scene.setUserData(loader);
                exit.initModality(Modality.WINDOW_MODAL);
                exit.initOwner(this.gui.getPrimaryStage());
                scene.setUserData(loader);
                exit.setScene(scene);

                exit.setX((this.gui.getPrimaryStage().getX() + this.gui.sceneWidth / 2 - 210.5));  /*(421/2)*/
                exit.setY((this.gui.getPrimaryStage().getY() + this.gui.sceneHeight / 2 - 225)); /*(450/2)*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            exit.show();
      //  });
    }


    /**
     * Shows the Rules file
     * @param actionEvent help button click
     */
    public void helperOnAction(ActionEvent actionEvent) {
        showHelper();
    }


    /**
     * Load the Rules fxml
     */
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
            rules.getScene().setCursor(new ImageCursor(new Image(CURSOR),36,45));

            rules.show();
        }
        catch (IOException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }
    }

    /**
     * load the font and the cursor image on the Textfield
     * avoid changes when it's clicked
     * @param tf the font chosen
     */
    public void setUpTextField(TextField tf){
        tf.setCursor(new ImageCursor(new Image(CURSOR)));
        tf.setFont(f);
        tf.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                tf.setFont(f);
            }
        });
    }
}

