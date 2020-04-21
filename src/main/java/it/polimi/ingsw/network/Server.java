package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.Match;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
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
    private ArrayList<VirtualView> virtualViews2;

    /**
     * All VirtualViews of 3 players mode instanced
     */
    private ArrayList<VirtualView> virtualViews3;

    /**
     * List of players who chose the name but not the mode
     */
    private ArrayList<String> pendingPlayers;

    public Server(){
        this.port = 1234;
        this.currentVirtualView2 = new VirtualView(this);
        this.currentVirtualView3 = new VirtualView(this);
        this.virtualViews2 = new ArrayList<>();
        this.virtualViews3 = new ArrayList<>();
        this.pendingPlayers = new ArrayList<>();
    }

    public ArrayList<VirtualView> getVirtualViews2() { return virtualViews2; }
    public ArrayList<VirtualView> getVirtualViews3() { return virtualViews3; }

    public ArrayList<String> getPendingPlayers() { return pendingPlayers; }

    /**
     * It iterates on all VirtualViews and all ServerClientHandlers
     * @return all players in a match
     */
    public ArrayList<String> getPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for(VirtualView vv : getVirtualViews2()){
            players.addAll(vv.getConnectedPlayers().keySet());
        }
        for(VirtualView vv : getVirtualViews3()){
            players.addAll(vv.getConnectedPlayers().keySet());
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
        ServerSocket serverSocket = null;

        boolean go;

        // if port is unavailable is start searching a free a port from port+1 and so on
        do {
            go=false;
            try{
                serverSocket = new ServerSocket(port);
            }catch (IOException e){
                if(e.getMessage().contains("Address already in use")){
                    port++;
                    go=true;
                }
                else return;
            }
        }while (go);

        loadMatch();

        this.virtualViews2.add(currentVirtualView2); // add the first VirtualView
        this.virtualViews3.add(currentVirtualView3); // add the first VirtualView

        System.out.println("[READY ON PORT "+port+"]");

        while (true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("[NEW USER] - " + socket.getRemoteSocketAddress().toString());

                //executor.submit(new ServerClientHandler(socket,this,currentVirtualView));
                executor.submit(new ServerClientHandler(socket,this));

                //saveVirtualView(virtualViews2,virtualViews3);
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
     * @param sm the message
     * @param vv the virtual view
     */
    public void sendAll(ServerMessage sm, VirtualView vv){
        for(Object sch : vv.getConnectedPlayers().values())
            ((ServerClientHandler) sch).notify(sm);
        System.out.println("[SENT] - " + sm.toString().substring(sm.toString().lastIndexOf('.')+1,sm.toString().lastIndexOf('@')) + " - ALL");
    }

    /**
     * It sends the server message to the client set in the message
     * @param sm the message
     * @param vv the virtual view
     */
    public void send(ServerMessage sm, VirtualView vv){
        if(sm!=null && vv != null){
            for(ServerClientHandler sch : vv.getConnectedPlayers().values()){
                if(sch.getName().equals(sm.name)){
                    sch.notify(sm);
                }
            }
        }
    }

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    public static void main(String[] args) {
        Server server = new Server();

        try (FileReader reader = new FileReader("resources"+File.separator+"config.json"))
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
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            // default params
            server.port = 1234;
        }

        //run server
        server.startServer();
    }

    /**
     * Loads the match from a file
     */

    public void loadMatch(){
        ObjectInputStream objIn;
        ArrayList<String> toDelete = new ArrayList<>();
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
                            } else if(((Match) obj).getStatus().equals(Status.NAME_CHOICE)) {
                                toDelete.add(fileEntry.getAbsolutePath());
                            }
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
        for(String file : toDelete)
            new File(file).delete();
    }
}
