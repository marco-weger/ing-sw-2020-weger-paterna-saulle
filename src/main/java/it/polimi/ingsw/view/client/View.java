package it.polimi.ingsw.view.client;

import sun.rmi.transport.Connection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class View implements Runnable {

    private final ObjectInputStream in;

    private final ObjectOutputStream out;

    public View(Socket s) {
        ObjectInputStream tempIn = null;
        ObjectOutputStream tempOut = null;

        try {
            tempOut = new ObjectOutputStream(s.getOutputStream());
            tempIn = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
        }
        catch (IOException e){
            System.err.println("Socket view error!");
        }

        this.in = tempIn;
        this.out = tempOut;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}
