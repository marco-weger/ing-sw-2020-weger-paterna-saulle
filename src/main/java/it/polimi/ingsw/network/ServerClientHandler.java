package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.serverMessages.NameRequestServer;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable  {

    Socket socket;
    Server server;
    ObjectInputStream in;
    ObjectOutputStream out;
    private static Logger LOGGER = Logger.getLogger("ServerClientHandler");


    public ServerClientHandler (Socket socket, Server server){
        this.socket = socket;
        this.server = server;
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
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Object object;

            // username req
            String name = "";
            String ip = socket.getInetAddress().toString();

            // TODO: if ip address has an active match lets go to the match
            if(socket.isConnected())
                this.notify(new NameRequestServer());

            while(socket.isConnected()){
                object = in.readObject();
                if(object instanceof ConnectionClient){
                    ConnectionClient cc = (ConnectionClient) object;
                    if(!cc.name.equals("") && !server.isInWaitingRoom(cc.name)){
                        name = cc.name;
                        cc.ip = socket.getInetAddress().toString();
                        cc.sch = this;
                        server.addPlayer((ConnectionClient) object);
                    }
                }
                VirtualView vv = server.getVirtualViews().get(name+ip);
                if(vv != null && object != null)
                    vv.notify((ClientMessage) object);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            // TODO: disconnection event
        }
    }

    protected void notify(ServerMessage message) {
        try{
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
