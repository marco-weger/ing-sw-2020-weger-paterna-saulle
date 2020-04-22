package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.serverMessages.BuiltServer;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.SnapPlayer;
import it.polimi.ingsw.view.TextFormatting;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Client implements Runnable{

    private CLI view;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String username;
    protected ArrayList<SnapCell> board;
    protected ArrayList<SnapWorker> workers;
    protected ArrayList<SnapPlayer> players;

    String ip;
    int port;

    private static Logger LOGGER = Logger.getLogger("Client");

    public Client(){
        this.board = new ArrayList<>();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++)
                board.add(new SnapCell(i,j,0));
        }
        this.workers=new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public ArrayList<SnapPlayer> getPlayers(){ return players; }

    public CLI getView() {
        return view;
    }

    public void setView(CLI view) {
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
        //System.out.println("START SETT");
        try{
            this.players = new ArrayList<>();
            for (String name : names) {
                this.players.add(new SnapPlayer(name));
                //System.out.println("\t"+this.players.get(i).symbol + " - " + this.players.get(i).name);
            }

        }catch (Exception e){
            System.exit(-1);
        }
        //System.out.println("END SETT...");
    }

    public static void main(String[] args){
        Client client = new Client();

        readParams(client);

        String version;
        do{
            System.out.print(TextFormatting.RESET + "Choose the version [CLI/GUI] " + TextFormatting.input() );
            try {
                version = new BufferedReader(new InputStreamReader(System.in)).readLine();
            } catch (IOException e) {
                version = "";
            }

            if(version.equalsIgnoreCase("CLI")){
                CLI view = new CLI(client);
                client.setView(view);
                view.displayFirstWindow();
            }
            else if(version.equalsIgnoreCase("GUI")){
                // TODO run gui
                System.out.println("RUN GUI...");
            }
        }while(!version.equals("CLI") && !version.equals("GUI"));
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
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(this);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected() && in != null) {
                ServerMessage msg = (ServerMessage) in.readObject();
                System.out.println(msg.toString());
                if(msg instanceof MovedServer){
                    //System.out.println(TextFormatting.COLOR_RED.toString() + ((MovedServer) msg).sw.row + " - " + ((MovedServer) msg).sw.column + TextFormatting.RESET);
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
                }
                msg.accept(view);
            }
            System.out.println("ERROR! END CYCLE...");
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("R ERROR! SERVER UNAVAILABLE...");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void sendMessage(ClientMessage msg){
        try {
            out.reset();
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("W ERROR! SERVER UNAVAILABLE...");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
