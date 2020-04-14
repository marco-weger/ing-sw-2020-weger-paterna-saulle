package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.TextFormatting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
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

        CLI.printTitle();

        String version;
        do{
            System.out.print("Choose the version [CLI/GUI]" + TextFormatting.getInputLine());
            System.out.flush();
            version = new Scanner(System.in).nextLine();

            if(version.equals("CLI")){
                CLI view = new CLI(client);
                client.setView(view);
                view.displayFirstWindow();
            }
            else if(version.equals("GUI")){
                // TODO run gui
                System.out.println("RUN GUI...");
            }
        }while(!version.equals("CLI") && !version.equals("GUI"));
    }

    public boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip,port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(this);
            return true;
        } catch (IOException e) {
            return false;
        }
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
