package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static Logger LOGGER = Logger.getLogger("Server");
    private int port;

    private ArrayList<Player> currentWaitingRoom;
    private VirtualView currentVirtualView;
    private Map<String, VirtualView> virtualViews = new HashMap<>();

    public Server(int port){
        this.port = port;
        currentWaitingRoom = new ArrayList<>();
    }

    public ArrayList<Player> getCurrentWaitingRoom() {
        return currentWaitingRoom;
    }

    public void setCurrentWaitingRoom(ArrayList<Player> currentWaitingRoom) {
        this.currentWaitingRoom = currentWaitingRoom;
    }

    public Map<String,VirtualView> getVirtualViews(){
        return virtualViews;
    }

    public boolean isInWaitingRoom(String name)
    {
        for(Player p:currentWaitingRoom)
            if(p.getName().equals(name))
                return true;
        return false;
    }

    public void startServer() throws IOException {
        //It creates threads when necessary, otherwise it re-uses existing one when possible
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            LOGGER.log(Level.WARNING, e.getMessage());
            return;
        }
        System.out.println("Server ready!");
        while (true){
            try{
                Socket socket = serverSocket.accept();
                executor.submit(new ServerClientHandler(socket,this));
            }catch(IOException e){
                LOGGER.log(Level.WARNING, e.getMessage());
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    public void sendAll(ServerMessage s, VirtualView vv){
        for(Object sch : vv.getConnectedPlayers().values())
            ((ServerClientHandler) sch).notify(s);
    }

    /**
     * It sends the server message to the client set in the message
     * @param s the message
     * @param vv the virtual view
     */
    public void send(ServerMessage s, VirtualView vv){
        vv.getConnectedPlayers().get(s.name+s.ip).notify(s);
    }

    public void addPlayer(ConnectionClient cc)
    {
        if(this.currentWaitingRoom.size() == 0)
            currentVirtualView = new VirtualView(this);
        this.currentWaitingRoom.add(new Player(cc.name, cc.ip ,currentVirtualView));
        currentVirtualView.notify(cc);
        virtualViews.put(cc.name+cc.ip,currentVirtualView);

        if(this.currentWaitingRoom.size() == 3)
        {
            //currentVirtualView = null;
            currentWaitingRoom = new ArrayList<>();
        }
        else if(currentWaitingRoom.size() == 2){
            // TODO run timer
        }
    }

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    // @SuppressWarnings("squid:S106")
    public static void main(String[] args) {
        Server server = new Server(1234);
        try {
            // TODO: chose a singular port
            server.startServer();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
