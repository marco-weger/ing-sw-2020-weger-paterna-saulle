package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.SnapPlayer;
import it.polimi.ingsw.view.TextFormatting;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
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

    private static String GREEK = "ΓΔΘΛΠΣΦΨΩ";

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

    public void resetPlayers(){ players = new ArrayList<>(); }

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

    public char getMyCode(){
        char c;
        boolean go = false;
        do{
            c = GREEK.charAt(Math.abs(new Random().nextInt(GREEK.length())));
            for(SnapPlayer p : players){
                if (p.symbol == c) {
                    go = true;
                    break;
                }
            }
        }while(go);
        return c;
    }

    public static void main(String[] args){
        Client client = new Client();

        readParams(client);

        String version;
        do{
            System.out.print("Choose the version [CLI/GUI]" + TextFormatting.getInputLine());
            System.out.flush();
            version = new Scanner(System.in).nextLine();

            if(version.equals("CLI")){
                CLI view = new CLI(client);
                client.setView(view);
                view.displayFirstWindow();
                //temporary printCLI
                view.printTable();
            }
            else if(version.equals("GUI")){
                // TODO run gui
                System.out.println("RUN GUI...");
            }
        }while(!version.equals("CLI") && !version.equals("GUI"));
    }

    public static void readParams(Client client){
        // json read
        JSONParser jsonParser = new JSONParser();
        JSONObject config;

        try (FileReader reader = new FileReader(Objects.requireNonNull(client.getClass().getClassLoader().getResource("config.json")).getFile()))
        {
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
                // TODO: if modified, board.print etc. necessity of messages that notify the update
                System.out.println(msg.toString());
                msg.accept(view);
            }
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("ERROR! SERVER UNAVAILABLE...");
            System.exit(0);
        }
    }

    public void sendMessage(ClientMessage msg){
        try {
            out.reset();
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("ERROR! SERVER UNAVAILABLE...");
            System.exit(0);
        }
    }
}
