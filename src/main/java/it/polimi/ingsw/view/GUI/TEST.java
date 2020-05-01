package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;
/*
public class TEST extends Application {

   /* public static final CountDownLatch latch = new CountDownLatch(1);
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

    public TEST() {
        setStartUpTest(this);
    }

    public void printSomething() {
        System.out.println("You called a method on the application");
    }

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(l, 200, 100);
        stage.setScene(scene);
        stage.show();
    }

}
*/