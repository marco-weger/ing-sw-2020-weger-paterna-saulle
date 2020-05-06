package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.*;
import it.polimi.ingsw.commons.clientmessages.DisconnectionClient;
import it.polimi.ingsw.commons.servermessages.BuiltServer;
import it.polimi.ingsw.commons.servermessages.CurrentStatusServer;
import it.polimi.ingsw.commons.servermessages.MovedServer;
import it.polimi.ingsw.commons.servermessages.PingServer;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.view.ViewInterface;
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

    private ViewInterface view;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String username;
    protected List<SnapCell> board;
    protected List<SnapWorker> workers;
    protected List<SnapPlayer> players;

    private long pingPeriod;
    private int timeoutSocket;
    private Timer ping;
    private boolean continueReading = true;

    private static final BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    String ip;
    int port;

    public Client(){
        // DEFAULT
        this.pingPeriod = 5;
        this.board = new ArrayList<>();
        this.workers=new ArrayList<>();
        this.players = new ArrayList<>();
        resetMatch();
    }

    public void resetMatch(){
        this.board.clear();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++)
                board.add(new SnapCell(i,j,0));
        }
        this.workers.clear();
        this.players.clear();
    }

    public List<SnapPlayer> getPlayers(){ return players; }

    public ViewInterface getView() {
        return view;
    }

    public void setView(ViewInterface view) {
        this.view = view;
    }

    public List<SnapCell> getBoard() {
        return board;
    }

    public List<SnapWorker> getWorkers() {
        return workers;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public void setPort(int port){
        this.port=port;
    }

    public String getUsername(){ return this.username; }

    public void setUsername(String username){ this.username=username; }

    public boolean getContinueReading(){ return continueReading; }

    public void setBoard(List<SnapCell> board){
        this.board = board;
    }

    public void setWorkers(List<SnapWorker> workers){
        this.workers = workers;
    }

    public SnapPlayer getMyPlayer(){
        for(SnapPlayer sp : getPlayers())
            if(sp.name.equals(getUsername()))
                return sp;
        return null;
    }

    public void removeWorkers(List<SnapWorker> snapWorkers){
        for(SnapWorker sw : snapWorkers)
            this.workers.remove(sw);
    }

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

    public void setPlayersBySnap(List<SnapPlayer> players){
        this.players = players;
    }

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

    public static void main(String[] args) {

        LOGGER.log( Level.FINE, "processing {0} entries in loop", "START" );

        Client client = new Client();
        readParams(client);
        String version;
        boolean go;

        do{
            if(args.length == 1){
                version = args[0];
                args = new String[0];
            } else {
                System.out.print("Choose the version [CLI/GUI] > ");
                System.out.flush();

                try {
                    version = read.readLine();
                } catch (IOException ex) {
                    LOGGER.log( Level.SEVERE, ex.toString(), ex );
                    version = "err";
                }
            }
            go = false;

            if(version.equalsIgnoreCase("CLI")){
                System.out.print("Connection to the server...");
                client.setView(new CLI(client,getRandomSymbol()));
                client.getView().displayFirstWindow();
            }
            else if(version.equalsIgnoreCase("GUI")){
                System.out.println("RUN GUI...");
                new Thread(() -> Application.launch(GUI.class)).start();
                //GUI gui = new GUI(client); //GUI.waitForGUI();
                GUI gui = GUI.waitForGUI(client);
                client.setView(gui);
                //startUpTest.printSomething();

                //client.setView(new GUI(client));
                //new Thread(() -> Application.launch(GUI.class)).start();
                //GUI startUpTest = GUI.waitForStartUpTest();
                //client.getView().displayFirstWindow();
                //GUI gui = new GUI(); // .start()
                //new Thread(() -> gui.).start();
                //GUI startUpTest = GUI.waitForStartUpTest();
                //startUpTest.printSomething();
            }
            else go = true;
        }while(go);
    }

    public void startPing(){
        ping = new Timer();
        ping.scheduleAtFixedRate(new TimerPing(this), 0, pingPeriod*1000);
    }

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
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
            return false;
        }
    }

    @Override
    public void run() {
        try {
            Thread handler = null;
            while (socket.isConnected() && in != null) {
                ServerMessage msg = (ServerMessage) readFromServer();

                if(msg != null){
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
                        view.statusHandler((CurrentStatusServer) msg);
                    }


                    continueReading = false;
                    try {
                        if (handler != null)
                            handler.join();
                    } catch (Exception ignored){}
                    continueReading = true;
                    handler = new Thread(() -> msg.accept(view));
                    handler.start();
                }
            }
        }
        catch (Exception ex){
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
            System.exit(0);
        }
    }

    public void sendMessage(ClientMessage msg){
        try {
            //out.reset();
            out.writeObject(msg);
            out.flush();
            if(msg instanceof DisconnectionClient){
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
            System.exit(0);
        }
    }

    protected Object readFromServer() {
        Object obj = null;
        do{
            try{
                obj = in.readObject();
            } catch (Exception ex){
                LOGGER.log( Level.SEVERE, ex.toString(), ex );
                view.close(true);
                System.exit(-1);
            }
        }while (obj instanceof PingServer || !(obj instanceof ServerMessage));
        return obj;
    }

    public void disconnectionHandler(){
        try{
            ping.cancel();
        } catch (Exception ignored){}
    }
}
