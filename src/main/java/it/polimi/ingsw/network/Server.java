package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;

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
     * VirtualView linked of current lobby
     */
    private VirtualView currentVirtualView2;
    private VirtualView currentVirtualView3;

    /**
     * All VirtualViews instanced
     */
    private ArrayList<VirtualView> virtualViews2;
    private ArrayList<VirtualView> virtualViews3;

    public Server(int port){
        this.port = port;
        currentVirtualView2 = new VirtualView(this);
        currentVirtualView3 = new VirtualView(this);
        virtualViews2 = new ArrayList<>();
        virtualViews3 = new ArrayList<>();
        virtualViews2.add(currentVirtualView2); // add the first VirtualView
        virtualViews3.add(currentVirtualView3); // add the first VirtualView
    }

    public ArrayList<VirtualView> getVirtualViews2() { return virtualViews2; }
    public ArrayList<VirtualView> getVirtualViews3() { return virtualViews3; }

    /**
     * It iterates on all VirtualViews and all ServerClientHandlers
     * @return all players in a match
     */
    public ArrayList<String> getPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for(VirtualView vv : getVirtualViews2()){
            for(ServerClientHandler sch : vv.getConnectedPlayers()){
                players.add(sch.getName());
            }
        }
        for(VirtualView vv : getVirtualViews3()){
            for(ServerClientHandler sch : vv.getConnectedPlayers()){
                players.add(sch.getName());
            }
        }
        return players;
    }

    public VirtualView getCurrentVirtualView2(){ return currentVirtualView2; }
    public VirtualView getCurrentVirtualView3(){ return currentVirtualView3; }

    public void newCurrentVirtualView2(){
        this.currentVirtualView2=new VirtualView(this);
        virtualViews2.add(this.currentVirtualView2);
    }

    public void newCurrentVirtualView3(){
        this.currentVirtualView3=new VirtualView(this);
        virtualViews3.add(this.currentVirtualView3);
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
        System.out.println("[READY ON PORT "+port+"]");
        while (true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("[NEW USER] - " + socket.getRemoteSocketAddress().toString());

                //executor.submit(new ServerClientHandler(socket,this,currentVirtualView));
                executor.submit(new ServerClientHandler(socket,this));
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
        int port = 1234;
        Server server = new Server(port);
        // TODO: chose a singular port
        server.startServer();
    }

}
