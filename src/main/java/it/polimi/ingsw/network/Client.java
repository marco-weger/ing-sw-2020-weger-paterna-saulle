package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.*;
import it.polimi.ingsw.commons.serverMessages.BuiltServer;
import it.polimi.ingsw.commons.serverMessages.CurrentStatusServer;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.commons.serverMessages.PingServer;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.GUI.TEST;
import it.polimi.ingsw.view.SnapPlayer;
import it.polimi.ingsw.view.TextFormatting;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable{

    private ViewInterface view;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String username;
    protected ArrayList<SnapCell> board;
    protected ArrayList<SnapWorker> workers;
    protected ArrayList<SnapPlayer> players;

    private int pingPeriod;
    private int timeoutSocket;
    private Timer ping;
    private boolean continueReading = true;

    private static final BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    String ip;
    int port;

    public Client(){
        this.board = new ArrayList<>();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++)
                board.add(new SnapCell(i,j,0));
        }
        this.workers=new ArrayList<>();
        this.players = new ArrayList<>();

        // DEFAULT
        this.pingPeriod = 5;
    }

    public ArrayList<SnapPlayer> getPlayers(){ return players; }

    public ViewInterface getView() {
        return view;
    }

    public void setView(ViewInterface view) {
        this.view = view;
    }

    public ArrayList<SnapCell> getBoard() {
        return board;
    }

    public ArrayList<SnapWorker> getWorkers() {
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

    public SnapPlayer getMyPlayer(){
        for(SnapPlayer sp : getPlayers())
            if(sp.name.equals(getUsername()))
                return sp;
        return null;
    }

    public void removeWorkers(ArrayList<SnapWorker> snapWorkers){
        for(SnapWorker sw : snapWorkers)
            this.workers.remove(sw);
    }

    public void setPlayers(ArrayList<String> names){
        try{
            this.players = new ArrayList<>();
            for (String name : names) {
                this.players.add(new SnapPlayer(name));
            }
        }catch (Exception e){
            System.exit(-1);
        }
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
            System.out.println(ex.toString());
            return "*@%";
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        readParams(client);
        String version;
        boolean go;

        do{
            if(args.length == 1){
                version = args[0];
                args = new String[0];
            } else {
                System.out.print(TextFormatting.RESET + "Choose the version [CLI/GUI] " + TextFormatting.input());
                System.out.flush();

                try {
                    //while(System.in.available() <= 0);
                    version = read.readLine();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.flush();
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
                // TODO run gui
                System.out.println("RUN GUI...");
                System.out.flush();
                new Thread(() -> Application.launch(TEST.class)).start();
                TEST startUpTest = TEST.waitForStartUpTest();
                startUpTest.printSomething();
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
        } catch (IOException e) {
            System.out.println("eeee_"+e.getMessage());
            return false;
        }
    }

    @Override
    public void run() {
        try {
            Thread handler = null;
            while (socket.isConnected() && in != null) {
                ServerMessage msg = (ServerMessage) readFromServer();
                //System.out.println(TextFormatting.COLOR_YELLOW+msg.toString()+TextFormatting.RESET);

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
        catch (Exception e){
            System.out.println("RUNNING ERROR... "+e.getMessage());
            System.exit(0);
        }
    }

    public void sendMessage(ClientMessage msg){
        try {
            out.reset();
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("SERVER UNAVAILABLE...");
            System.out.flush();
            System.exit(0);
        }
    }

    protected Object readFromServer() {
        Object obj = null;
        do{
            try{
                obj = in.readObject();
            } catch (Exception ex){
                System.out.println("XXX");
                view.close();
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
