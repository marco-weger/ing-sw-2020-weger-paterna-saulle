package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.view.CLI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{

    private CLI view;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    private static Logger LOGGER = Logger.getLogger("Client");

    public Client(){}

    public CLI getView() {
        return view;
    }

    public void setView(CLI view) {
        this.view = view;
    }

    public static void main(String[] args){
        Client client = new Client();

        int answer = JOptionPane.showOptionDialog(null,"Choose the version","Santorini",
                JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"CLI", "GUI"},"CLI");

        if(answer == 0){
            CLI view = new CLI(client);
            client.setView(view);
            view.displayFirstWindow();
        }
        else if(answer == 1){
            // TODO run gui
            System.out.println("RUN GUI...");
        }
    }

    public boolean connect() {
        try {
            socket = new Socket("127.0.0.1", 1234);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(this);
            //sendMessage(cc);
            return true;
        } catch (Exception e) {
            // TODO: display error
            //view.displayConnectionErrorClient(loginMessage);
            //System.out.println(socket.isConnected());
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected() && in != null) {
                ServerMessage msg = (ServerMessage) in.readObject();
                System.out.println(msg.toString());
                msg.accept(view);
            }
        }
        catch (IOException | ClassNotFoundException e){
            //System.err.println("Error while receiving new Question object through SOCKET");
            //e.printStackTrace();
            //userInterface.receiveEvent(new DisconnectedQuestion());
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public void sendMessage(ClientMessage msg){
        try {
            out.reset();
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

    }
}
