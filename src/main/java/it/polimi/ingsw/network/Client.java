package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.*;
import it.polimi.ingsw.commons.clientmessages.DisconnectionClient;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * View instance CLI or GUI
     */
    private ViewInterface view;

    /**
     * The socket used to server connection
     */
    private Socket socket;

    /**
     * Stream used to read
     */
    private ObjectInputStream in;

    /**
     * Stream used to write
     */
    private ObjectOutputStream out;

    /**
     * Current board situation
     */
    protected List<SnapCell> board;

    /**
     * Current workers position
     */
    protected List<SnapWorker> workers;

    /**
     * Current players
     */
    protected List<SnapPlayer> players;

    /**
     * My username
     */
    private String username;

    /**
     * This flag is used to manage the persistence
     */
    private boolean mustPrint = false;

    /**
     * The period of ping scheduled task
     */
    private long pingPeriod;

    /**
     * Socket timeout parameter
     */
    private int timeoutSocket;

    /**
     * Timer task
     */
    private Timer ping;

    /**
     * This flag is used to block the user input in the CLI when necessary
     */
    private boolean continueReading = true;

    /**
     * Player of current turn
     */
    private String currentPlayer;

    /**
     * Thread used to run View method
     */
    private Thread handler = null;

    //private static final BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    /**
     * The ip address
     */
    private String ip;

    /**
     * The socket port
     */
    private int port;

    /**
     * In initialize all vars with default value
     */
    public Client(){
        this.pingPeriod = 5;
        this.board = new ArrayList<>();
        this.workers=new ArrayList<>();
        this.players = new ArrayList<>();
        resetMatch();
    }

    /**
     * In initialize board, workers and players
     */
    public void resetMatch(){
        this.board.clear();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++)
                board.add(new SnapCell(i,j,0));
        }
        this.workers.clear();
        this.players.clear();
    }

    /**
     * List of players
     * @return
     */
    public List<SnapPlayer> getPlayers(){ return players; }

    /**
     * The view CLI or GUI
     * @return
     */
    public ViewInterface getView() { return view; }

    /**
     * The view CLI or GUI
     * @param view
     */
    public void setView(ViewInterface view) { this.view = view; }

    /**
     * The flag is used to print the board after a persistence event
     * @param mustPrint
     */
    public void setMustPrint(boolean mustPrint){ this.mustPrint=mustPrint; }

    /**
     * The board getter
     * @return
     */
    public List<SnapCell> getBoard() { return board; }

    /**
     * The workers getter
     * @return
     */
    public List<SnapWorker> getWorkers() { return workers; }

    /**
     * The ip setter
     * @param ip
     */
    public void setIp(String ip){ this.ip = ip; }

    /**
     * The port setter
     * @param port
     */
    public void setPort(int port){ this.port=port; }

    /**
     * My username getter
     * @return
     */
    public String getUsername(){ return this.username; }

    /**
     * My username setter
     * @param username
     */
    public void setUsername(String username){ this.username=username; }

    /**
     * The continueReading getter
     * @return
     */
    public boolean getContinueReading(){ return continueReading; }

    /**
     * The board setter
     * @param board
     */
    public void setBoard(List<SnapCell> board){  this.board = board; }

    /**
     * Workers setter
     * @param workers
     */
    public void setWorkers(List<SnapWorker> workers){ this.workers = workers; }

    /**
     * The current player getter
     * @return
     */
    public String getCurrentPlayer(){ return currentPlayer; }

    /**
     * The current player setter
     * @param currentPlayer
     */
    public void setCurrentPlayer(String currentPlayer){ this.currentPlayer=currentPlayer; }

    /**
     * It returns my player from players collection
     * @return
     */
    public SnapPlayer getMyPlayer(){
        for(SnapPlayer sp : getPlayers())
            if(sp.name.equals(getUsername()))
                return sp;
        return null;
    }

    /**
     * It removes a worker from collection
     * @param snapWorkers
     */
    public void removeWorkers(List<SnapWorker> snapWorkers){
        for(SnapWorker sw : snapWorkers)
            this.workers.remove(sw);
    }

    /**
     * It sets all the player of the collection
     * @param names
     */
    public void setPlayersByString(List<String> names){
        try{
            this.players = new ArrayList<>();
            for (String name : names) {
                this.players.add(new SnapPlayer(name));
            }
        }catch (Exception e){
            System.exit(-1);
        }
    }

    /**
     * It sets all the player of the collection
     * @param players
     */
    public void setPlayersBySnap(List<SnapPlayer> players){ this.players = players; }

    /**
     * It generates a 3 unique random chars string from "ΓΔΘΛΠΣΦΨΩ"
     * @return
     */
    public static String getRandomSymbol(){
        final String GREEK = "ΓΔΘΛΠΣΦΨΩ";
        try {
            StringBuilder ret = new StringBuilder();
            String tmp;
            while(ret.length()<3){
                tmp = ""+GREEK.charAt(Math.abs(new Random().nextInt(GREEK.length())));
                if (!ret.toString().contains(tmp))
                    ret.append(tmp);
            }
            return ret.toString();
        } catch (Exception ex){
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
            return "*@%";
        }
    }

    /**
     * Main method, it basically starts the GUI, excepted if first element of args is "CLI"
     * @param args
     */
    public static void main(String[] args) {
        String version = "GUI";
        //String version = "CLI";
        boolean go;

        if(args.length == 1){
            version = args[0];
            args = new String[0];
        }

        if(version.equalsIgnoreCase("CLI")){
            System.out.print("Connection to the server...");
            Client client = new Client();
            readParams(client);
            client.setView(new CLI(client,getRandomSymbol()));
            client.getView().displayFirstWindow();
        }
        else if(version.equalsIgnoreCase("GUI")){
            Application.launch(GUI.class, args);
        } else System.exit(-1);
    }

    /**
     * It start ping task
     */
    public void startPing(){
        ping = new Timer();
        ping.scheduleAtFixedRate(new TimerPing(this), 0, pingPeriod*1000);
    }

    /**
     * It reads params from JSON config file
     * @param client
     */
    public static void readParams(Client client){
        try (FileReader reader = new FileReader("resources"+File.separator+"config.json"))
        {
            // json read
            JSONParser jsonParser = new JSONParser();
            JSONObject config;

            //Read JSON file
            Object obj = jsonParser.parse(reader);
            config = (JSONObject) obj;

            if(config != null){
                if (config.containsKey("port"))
                    client.setPort(Integer.parseInt(config.get("port").toString()));
                if (config.containsKey("ip"))
                    client.setIp(config.get("ip").toString());
                if (config.containsKey("pingPeriod"))
                    client.pingPeriod = Integer.parseInt(config.get("pingPeriod").toString());
                if (config.containsKey("timeoutSocket"))
                    client.timeoutSocket = Integer.parseInt(config.get("timeoutSocket").toString());
            }
        } catch (Exception e) {
            // default params
            client.setPort(1234);
            client.setIp("127.0.0.1");
        }
    }

    /**
     * It connects to server if possible
     * @return
     */
    public boolean connect() {
        try {
            socket = new Socket(ip,port);
            socket.setSoTimeout(timeoutSocket*1000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            startPing();
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(this);
            return true;
        } catch (IOException ex) {
            LOGGER.log( Level.CONFIG, "Default address not valid (port:ip)", ex );
            return false;
        }
    }

    /**
     * The server task
     */
    @Override
    public void run() {
        try {
            while (socket.isConnected() && in != null) {
                ServerMessage msg = (ServerMessage) readFromServer();
                if(msg != null){
                    // System.out.println(msg.toString());
                    if(msg instanceof MovedServer){
                        for(SnapWorker worker : getWorkers()){
                            if(worker.name.equals(((MovedServer) msg).sw.name) && worker.n == ((MovedServer) msg).sw.n){
                                worker.row = ((MovedServer) msg).sw.row;
                                worker.column = ((MovedServer) msg).sw.column;
                            }
                        }
                    }else if(msg instanceof BuiltServer){
                        for(SnapCell cell : getBoard()){
                            if(cell.toString().equals(((BuiltServer) msg).sc.toString())){
                                cell.level = ((BuiltServer) msg).sc.level;
                            }
                        }
                    }else if(msg instanceof CurrentStatusServer){
                        if(((CurrentStatusServer) msg).status.equals(Status.START))
                            currentPlayer = ((CurrentStatusServer) msg).player;
                        view.statusHandler((CurrentStatusServer) msg);
                    }

                    if(mustPrint && !(msg instanceof ReConnectionServer)){
                        view.displayBoard();
                        mustPrint = false;
                    }

                    continueReading = false;
                    try {
                        if (handler != null)
                            handler.join();
                    } catch (Exception ignored){}
                    continueReading = true;
                    if(msg instanceof ReConnectionServer || msg instanceof EasterEggServer){
                        msg.accept(view);
                    } else {
                        handler = new Thread(() -> msg.accept(view));
                        handler.start();
                    }
                }
            }
        }
        catch (Exception ex){
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
            System.exit(-1);
        }
    }

    /**
     * It resumes card choice after the easter egg
     * @param acs
     */
    public void runAfterEasterEgg(AvailableCardServer acs){
        continueReading = false;
        try {
            if (handler != null)
                handler.join();
        } catch (Exception ignored){}
        continueReading = true;
        handler = new Thread(() -> acs.accept(view));
        handler.start();
    }

    /**
     * It sends message to server
     * @param msg
     */
    public void sendMessage(ClientMessage msg){
        if(out != null){
            try {
                out.writeObject(msg);
                out.flush();
                if(msg instanceof DisconnectionClient){
                    in.close();
                    out.close();
                    socket.close();
                }
            } catch (Exception ex) {
                //LOGGER.log(Level.WARNING, "Can't send " + msg.toString(), ex);
                view.close(true);
            }
        }
    }

    /**
     * It reads message from server
     * @return
     */
    protected Object readFromServer() {
        Object obj = null;
        do{
            try{
                obj = in.readObject();
            } catch (Exception ex){
                //LOGGER.log(Level.WARNING, "Can't read ServerMessage", ex);
                obj = null;
                view.close(true);
            }
        }while (obj != null && (obj instanceof PingServer || !(obj instanceof ServerMessage)));
        return obj;
    }

    /**
     * It runs when there is a disconnection and stop the ping task
     */
    public void disconnectionHandler(){
        try{
            ping.cancel();
        } catch (Exception ignored){}
    }
}
