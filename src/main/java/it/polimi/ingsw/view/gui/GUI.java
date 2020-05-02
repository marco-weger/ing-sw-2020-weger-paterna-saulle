package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {

    public static Stage stage;

    /*
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static TEST startUpTest = null;

    public static TEST waitForStartUpTest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return startUpTest;
    }

    public static void setStartUpTest(TEST startUpTest0) {
        startUpTest = startUpTest0;
        latch.countDown();
    }
     */

    public GUI() {
        //setStartUpTest(this);
    }

    public void printSomething() {
        System.out.println("You called a method on the application");
    }

    @Override
    public void start(Stage stage) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/it.polimi.ingsw/view/gui/FXMLfiles/WelcomeScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(Objects.requireNonNull(root),950,731));
        stage.setResizable(false);
        stage.show();
    }

}
