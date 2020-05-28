package it.polimi.ingsw.view.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class ServerController extends DefaultController {

    @FXML
    public TextField ip1;

    @FXML
    public TextField ip2;

    @FXML
    public TextField ip3;

    @FXML
    public TextField ip4;

    @FXML
    public TextField port;

    @FXML
    public Button buttonConnect;

    @FXML
    public Button banner;

    /**
     * Regex for an IP address (ref. https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/)
     */
    private static final String zeroTo255
            = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

    @FXML
    @Override
    public void initialize(){
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_server.png"));
        // force the field to be numeric only
        port.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,6}")) {
                    port.setText(oldValue);
                } else port.getStyleClass().remove("text-field.error");
            }
        });
        // force the field to be ip address
        ip1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(zeroTo255)) {
                    ip1.setText(oldValue);
                } else ip1.getStyleClass().remove("text-field.error");
            }
        });
        ip2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(zeroTo255)) {
                    ip2.setText(oldValue);
                } else ip2.getStyleClass().remove("text-field.error");
            }
        });
        ip3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(zeroTo255)) {
                    ip3.setText(oldValue);
                } else ip3.getStyleClass().remove("text-field.error");
            }
        });
        ip4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(zeroTo255)) {
                    ip4.setText(oldValue);
                } else ip4.getStyleClass().remove("text-field.error");
            }
        });

        buttonConnect.setMinSize(200,114);
        buttonConnect.setMaxSize(200,114);
        buttonConnect.setPrefSize(200,114);
        buttonConnect.setMinSize(200,114);
        buttonConnect.setMaxSize(200,114);
        buttonConnect.setPrefSize(200,114);

        banner.setMinSize(800,60);
        banner.setMaxSize(800,60);
        banner.setPrefSize(800,60);
        banner.setText("Server Unavailable, try again later or type a new address");
        banner.setVisible(true);
    }

    @Override
    public void setup(){
        super.setup();
        buttonConnect.setLayoutX(gui.sceneWidth/2-100);
        buttonConnect.setLayoutX(gui.sceneWidth/2-100);

        banner.setLayoutY(gui.sceneHeight-top.getPrefHeight()-center.getPrefHeight()-bottom.getPrefHeight()/2-banner.getPrefHeight()/2);
        banner.setLayoutX((gui.sceneWidth/2)-400);

        setUpTextField(port);
        setUpTextField(ip1);
        setUpTextField(ip2);
        setUpTextField(ip3);
        setUpTextField(ip4);
    }

    public void connection(ActionEvent actionEvent) {
        tryConnect();
    }

    public void portOnAction(ActionEvent actionEvent) {
        tryConnect();
    }

    public void ip1OnAction(ActionEvent actionEvent) {
        tryConnect();
    }

    public void tryConnect(){
        this.gui.getClient().setIp(ip1.getText()+'.'+ip2.getText()+'.'+ip3.getText()+'.'+ip4.getText());
        this.gui.getClient().setPort(Integer.parseInt(port.getText()));
        if(!this.gui.getClient().connect()){
            banner.setText("New Server Unreachable, try again later or type a new address");

        }
    }

    public void ip2OnAction(ActionEvent actionEvent) {
        tryConnect();
    }

    public void ip3OnAction(ActionEvent actionEvent) {
        tryConnect();
    }

    public void ip4OnAction(ActionEvent actionEvent) {
        tryConnect();
    }
}
