package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;


public class HomeController extends DefaultController{

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

        buttonHelper.setVisible(false);
    }

    public void handleHelpButton(javafx.event.ActionEvent actionEvent) {
        showHelper();
    }


    public void handlePlayButton(javafx.event.ActionEvent actionEvent) {
        boolean go = this.gui.getClient().connect();
        Platform.runLater(() -> {
            if(go)
                this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Name.fxml"));
            else
                this.gui.getPrimaryStage().setScene(this.gui.load("/it.polimi.ingsw/view/gui/fxml/Server.fxml"));

            this.gui.getPrimaryStage().show();
        });
    }

}
