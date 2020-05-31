package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class NameController extends DefaultController {

    @FXML
    public Button buttonLogin;

    @FXML
    public TextField name;

    @FXML
    public Button banner;

    /**
     * set the background image
     * set buttons
     * set texts
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_name.png"));

        buttonLogin.setMinSize(200,114);
        buttonLogin.setMaxSize(200,114);
        buttonLogin.setPrefSize(200,114);
        name.setMinSize(400,50);
        name.setMaxSize(400,50);
        name.setPrefSize(400,50);

        banner.setMinSize(800,60);
        banner.setMaxSize(800,60);
        banner.setPrefSize(800,60);
        banner.setText("The chosen one is not allowed, type new username (max 12 characters)");
        banner.setVisible(false);
    }

    /**
     * initialize textfield and buttons
     */
    @Override
    public void setup(){
        super.setup();
        banner.setLayoutY(bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX((gui.sceneWidth/2)-400);

        buttonLogin.setLayoutX(gui.sceneWidth/2-100);
        name.setLayoutX(gui.sceneWidth/2-200);
        setUpTextField(name);
    }


    /**
     * When the login button is pressed, try to set the name.
     * @param login
     */
    public void handleLoginButton(ActionEvent login) {
        trySetName();
    }


    /**
     * When the player type enter on banner, try to set name.
     * @param actionEvent
     */
    public void nameOnAction(ActionEvent actionEvent) {
        trySetName();
    }


    /**
     * Compare the name whit the conditions (not empty,
     * not > of 12 characters ...) and if it's correct
     * it proceeds to establish a connection whit the server.
     * If not, display banner.
     * */
    public void trySetName(){
        this.gui.getClient().setUsername(name.getText());
        if(!(this.gui.getClient().getUsername().isEmpty() || this.gui.getClient().getUsername().length() > 12 || this.gui.getClient().getUsername().matches("^\\s*$")))
            this.gui.getClient().sendMessage(new ConnectionClient(this.gui.getClient().getUsername()));
        else {
            showBanner();
        }
    }


    /**
     * Shows the banner
     */
    public void showBanner() {
        banner.setVisible(true);
    }
}
