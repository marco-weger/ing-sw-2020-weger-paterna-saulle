package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.model.Status;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static Logger LOGGER = Logger.getLogger("Server");

    /**
     * The socket port
     */
    private int port;

    /**
     * A counter of ready player (a ready player has correctly set username and is ready to start the match)
     */
    private int ready;

    /**
     * VirtualView linked of current lobby
     */
    private VirtualView currentVirtualView;

    /**
     * All VirtualViews instanced
     */
    private ArrayList<VirtualView> virtualViews;

    public Server(int port){
        this.port = port;
        currentVirtualView = new VirtualView(this);
        virtualViews = new ArrayList<>();
        virtualViews.add(currentVirtualView); // add the first VirtualView
        ready = 0;
    }

    public ArrayList<VirtualView> getVirtualViews() { return virtualViews; }

    /**
     * It iterates on all VirtualViews and all ServerClientHandlers
     * @return all players in a match
     */
    public ArrayList<String> getPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for(VirtualView vv : getVirtualViews()){
            for(ServerClientHandler sch : vv.getConnectedPlayers()){
                players.add(sch.getName());
            }
        }
        return players;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    /**
     * Main server: it accepts connection and start the client handler (ServerClientHandler)
     */
    public void startServer(){
        //It creates threads when necessary, otherwise it re-uses existing one when possible
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            LOGGER.log(Level.WARNING, e.getMessage());
            return;
        }
        System.out.println("[READY]");
        while (true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("[NEW USER] - " + socket.getRemoteSocketAddress().toString());

                // this part set up new match
                if(!currentVirtualView.getCurrentStatus().equals(Status.NAME_CHOICE)){
                    currentVirtualView = new VirtualView(this);
                    virtualViews.add(currentVirtualView);
                    ready = 0;
                }

                executor.submit(new ServerClientHandler(socket,this,currentVirtualView));
            }catch(IOException e){
                LOGGER.log(Level.WARNING, e.getMessage());
                break;
            }
        }
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * It sends the server message to all clients
     * @param s the message
     * @param vv the virtual view
     */
    public void sendAll(ServerMessage s, VirtualView vv){
        for(Object sch : vv.getConnectedPlayers())
            ((ServerClientHandler) sch).notify(s);
    }

    /**
     * It sends the server message to the client set in the message
     * @param s the message
     * @param vv the virtual view
     */
    public void send(ServerMessage s, VirtualView vv){
        if(s!=null && vv != null)
            for(ServerClientHandler sch : vv.getConnectedPlayers())
                if(sch.getName().equals(s.name))
                    sch.notify(s);
    }

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    public static void main(String[] args) {
        Server server = new Server(1234);
        // TODO: chose a singular port
        server.startServer();
    }

}
