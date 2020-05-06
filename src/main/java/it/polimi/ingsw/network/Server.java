package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.PingServer;
import it.polimi.ingsw.model.Match;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    /**
     * The socket port
     */
    private int port;

    /**
     * VirtualView linked of current lobby (2 players)
     */
    private VirtualView currentVirtualView2;

    /**
     * VirtualView linked of current lobby (3 players)
     */
    private VirtualView currentVirtualView3;

    /**
     * All VirtualViews of 2 players mode instanced
     */
    private final ArrayList<VirtualView> virtualViews2;

    /**
     * All VirtualViews of 3 players mode instanced
     */
    private final ArrayList<VirtualView> virtualViews3;

    /**
     * List of players who chose the name but not the mode
     */
    private final ArrayList<String> pendingPlayers;

    /**
     * Socket timeout for reading call
     */
    private int timeoutSocket;

    /**
     * Socket ping period
     */
    private int pingPeriod;

    /**
     * Complete turn timer
     */
    private long turnTimer;

    /**
     * Timer before lose after a disconnection
     */
    private int disconnectTimer;

    /**
     * It assigns default value at configurable vars
     */
    public Server(){
        this.virtualViews2 = new ArrayList<>();
        this.virtualViews3 = new ArrayList<>();
        this.pendingPlayers = new ArrayList<>();

        // DEFAULT VALUE
        this.port = 1234;
        this.pingPeriod = 2;
        this.timeoutSocket = 5;
        this.turnTimer = 180;
        this.disconnectTimer = 60;
    }

    /**
     * @return a list of 2 players virtual view
     */
    public List<VirtualView> getVirtualViews2() { return virtualViews2; }

    /**
     * @return a list of 3 players virtual view
     */
    public List<VirtualView> getVirtualViews3() { return virtualViews3; }

    /**
     * @return a list of player who hasn not choose the mode
     */
    public List<String> getPendingPlayers() { return pendingPlayers; }

    /**
     * @return the current virtual view of 2 players match
     */
    public VirtualView getCurrentVirtualView2(){ return currentVirtualView2; }

    /**
     * @return the current virtual view of 3 players match
     */
    public VirtualView getCurrentVirtualView3(){ return currentVirtualView3; }

    /**
     * @return the timer of current turn
     */
    public long getTurnTimer(){ return turnTimer; }

    /**
     * @return the timer of disconnection during a game
     */
    public int getdisconnectTimer(){ return disconnectTimer; }


    /**
     * It iterates on all VirtualViews and all ServerClientHandlers
     * @return all players in a match
     */
    public List<String> getPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for(VirtualView vv : getVirtualViews2()){
            players.addAll(vv.getConnectedPlayers().keySet());
        }
        for(VirtualView vv : getVirtualViews3()){
            players.addAll(vv.getConnectedPlayers().keySet());
        }
        return players;
    }

    /**
     * It creates a new lobby for 2 players match
     */
    public void newCurrentVirtualView2(){
        this.currentVirtualView2=new VirtualView(this);
        virtualViews2.add(this.currentVirtualView2);
    }

    /**
     * It creates a new lobby for 3 players match
     */
    public void newCurrentVirtualView3(){
        this.currentVirtualView3=new VirtualView(this);
        virtualViews3.add(this.currentVirtualView3);
    }

    /**
     * Main server: it accepts connection and start the client handler (ServerClientHandler)
     */
    public void startServer(){
        boolean go;

        // if port is unavailable is start searching a free a port from port+1 and so on
        ServerSocket serverSocket = null;
        do {
            go=false;
            try{
                serverSocket = new ServerSocket(port);
                serverSocket.close();
            }catch (IOException e){
                if(e.getMessage().contains("Address already in use")){
                    port++;
                    go=true;
                }
                else return;
            }
        }while (go);

        // TODO use this function
        // loadMatch();

        this.virtualViews2.add(currentVirtualView2); // add the first VirtualView
        this.virtualViews3.add(currentVirtualView3); // add the first VirtualView

        System.out.println("[SOCKET TIMEOUT] - "+timeoutSocket + "s");
        System.out.println("[PING PERIOD] - "+pingPeriod + "s");
        System.out.println("[PORT] - "+port);

        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Socket socket;
            while (true){
                try{
                    socket = serverSocket.accept();
                    socket.setSoTimeout(timeoutSocket*1000);
                    System.out.println("[NEW USER] - " + socket.getRemoteSocketAddress().toString());

                    executor.submit(new ServerClientHandler(socket,this, pingPeriod));
                }catch(IOException e){
                    System.err.println("[START_SERVER] - "+e.getMessage());
                    break;
                }
            }
        }
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("[START_SERVER] - "+e.getMessage());
        }
    }

    /**
     * It sends the server message to all clients
     * @param sm the message
     * @param vv the virtual view
     */
    public void sendAll(ServerMessage sm, VirtualView vv){
        for(Object sch : vv.getConnectedPlayers().values())
            if(sch != null)
                if(((ServerClientHandler) sch).isStillConnected())
                    ((ServerClientHandler) sch).notify(sm);
        System.out.println("[SENT] - " + sm.toString().substring(sm.toString().lastIndexOf('.')+1,sm.toString().lastIndexOf('@')) + " - ALL");
    }

    /**
     * It sends the server message to the client set in the message
     * @param sm the message
     * @param vv the virtual view
     */
    public void send(ServerMessage sm, VirtualView vv){
        if(sm!=null && vv != null)
            for(ServerClientHandler sch : vv.getConnectedPlayers().values())
                if(sch.getName().equals(sm.name) && sch.isStillConnected())
                    sch.notify(sm);
        if(!(sm instanceof PingServer))
            System.out.println("[SENT] - " + Objects.requireNonNull(sm).toString().substring(sm.toString().lastIndexOf('.')+1,sm.toString().lastIndexOf('@')) + " - " + sm.name);
    }

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    public static void main(String[] args) {
        Server server = new Server();

        try (FileReader reader = new FileReader("resources"+ File.separator+"config.json"))
        {
            // json read
            JSONParser jsonParser = new JSONParser();
            JSONObject config;

            //Read JSON file
            Object obj = jsonParser.parse(reader);
            config = (JSONObject) obj;

            if(config != null) {
                if (config.containsKey("port"))
                    server.port = Integer.parseInt(config.get("port").toString());
                if(config.containsKey("pingPeriod"))
                    server.pingPeriod = Integer.parseInt(config.get("pingPeriod").toString());
                if(config.containsKey("timeoutSocket"))
                    server.timeoutSocket = Integer.parseInt(config.get("timeoutSocket").toString());
                if(config.containsKey("turnTimer"))
                    server.turnTimer = Integer.parseInt(config.get("turnTimer").toString());
                if(config.containsKey("disconnectTimer"))
                    server.disconnectTimer = Integer.parseInt(config.get("disconnectTimer").toString());
            }
        } catch (Exception e) {
            server.port = 1234;
        }

        //run server
        server.newCurrentVirtualView2();
        server.newCurrentVirtualView3();
        server.startServer();
    }

    /**
     * Loads the match from a file
     */
    public void loadMatch(){
        ObjectInputStream objIn;
        //ArrayList<String> toDelete = new ArrayList<>();
        // TODO check for not load ended match
        try {
            if(new File("resources"+File.separator+"saved-match").exists()){
                for (final File fileEntry : Objects.requireNonNull(new File("resources"+File.separator+"saved-match").listFiles())) {
                    if (!fileEntry.isDirectory()) {
                        objIn = new ObjectInputStream(new FileInputStream(fileEntry.getAbsolutePath()));
                        Object obj = objIn.readObject();
                        if(obj instanceof Match) {
                            if (!((Match) obj).getStatus().equals(Status.NAME_CHOICE) && !((Match) obj).getStatus().equals(Status.END)){
                                if (((Match) obj).getPlayers().size() + ((Match) obj).getLosers().size() == 2) {
                                    Object sm = objIn.readObject();
                                    if(sm instanceof ServerMessage)
                                        virtualViews2.add(new VirtualView(this, (Match) obj, (ServerMessage) sm));
                                } else if (((Match) obj).getPlayers().size() + ((Match) obj).getLosers().size() == 3) {
                                    Object sm = objIn.readObject();
                                    if(sm instanceof ServerMessage)
                                        virtualViews3.add(new VirtualView(this, (Match) obj, (ServerMessage) sm));
                                }
                            } //else if(((Match) obj).getStatus().equals(Status.NAME_CHOICE)) {
                                //toDelete.add(fileEntry.getAbsolutePath());
                            //}
                        }
                        objIn.close();
                    }
                }
            }
            //System.out.println("[SAVED DIR] - "+"saved-match/");
            System.out.println("[2 PLAYERS] - "+virtualViews2.size()+" loaded");
            System.out.println("[3 PLAYERS] - "+virtualViews3.size()+" loaded");
        } catch (IOException | ClassNotFoundException e) {
            //System.out.println("[SAVED DIR] - "+"saved-match");
            System.out.println("[2 PLAYERS MATCH] - "+virtualViews2.size()+" match loaded");
            System.out.println("[3 PLAYERS MATCH] - "+virtualViews3.size()+" match loaded");
        }
        // I think we should omit this part... Method who save the match jumps yet not started
        //for(String file : toDelete)
        //    new File(file).delete();
    }

}
