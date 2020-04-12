package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.serverMessages.NameRequestServer;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable  {

    Socket socket;
    Server server;
    ObjectInputStream in;
    ObjectOutputStream out;
    private static Logger LOGGER = Logger.getLogger("ServerClientHandler");
    VirtualView virtualView;

    public ServerClientHandler (Socket socket, Server server, VirtualView virtualView){
        this.socket = socket;
        this.server = server;
        this.virtualView = virtualView;
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
            // TODO: if ip address has an active match lets go to the match
            if(socket.isConnected()){
                String tmpName = "FIRST";
                do{
                    ArrayList<String> players = new ArrayList<>();
                    for(Player p : server.getCurrentWaitingRoom())
                        players.add(p.getName());
                    this.notify(new NameRequestServer(players,tmpName.equals("FIRST")));

                    object = in.readObject();

                    if(object instanceof ConnectionClient){
                        ConnectionClient cc = (ConnectionClient) object;
                        tmpName = cc.name;
                        cc.ip = socket.getRemoteSocketAddress().toString();
                        cc.sch = this;
                        for(Player p:server.getCurrentWaitingRoom()){
                            if(p.getName().equals(cc.name)){
                                tmpName = "";
                                break;
                            }
                        }
                    } else tmpName = "";
                }while(tmpName.isEmpty());
                for(Player p:server.getCurrentWaitingRoom())
                    if(p.getName().equals(socket.getRemoteSocketAddress().toString()))
                        p.setName(tmpName);
                virtualView.notify((ClientMessage) object);
            }

            while(socket.isConnected()){
                object = in.readObject();

                if(virtualView != null && object != null)
                    virtualView.notify((ClientMessage) object);
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
