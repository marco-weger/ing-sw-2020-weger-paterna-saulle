package it.polimi.ingsw.view.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class ServerController extends DefaultController {

    public TextField ip1;
    public TextField ip2;
    public TextField ip3;
    public TextField ip4;
    public TextField port;

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
    }

    public void connection(ActionEvent actionEvent) {
        client.setIp(ip1.getText()+'.'+ip2.getText()+'.'+ip3.getText()+'.'+ip4.getText());
        client.setPort(Integer.parseInt(port.getText()));
        if(!client.connect()){ // TODO show wait image round
            System.out.println("KO....");
            ip1.getStyleClass().add("error"); // or false to unset it
            ip2.getStyleClass().add("error"); // or false to unset it
            ip3.getStyleClass().add("error"); // or false to unset it
            ip4.getStyleClass().add("error"); // or false to unset it
            port.getStyleClass().add("error"); // or false to unset it
        } else System.out.println("OKKK...");
    }
}
