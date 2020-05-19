package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI implements ViewInterface {

    /**
     * Client with socket connection
     */
    final Client client;

    /**
     * Color used to print the board
     */
    protected static final String[] color = new String[]{
        TextFormatting.BACKGROUND_CYAN.toString() + TextFormatting.COLOR_BLACK, // lv 0
        TextFormatting.BACKGROUND_BRIGHT_WHITE.toString() + TextFormatting.COLOR_BLACK, // lv 1
        TextFormatting.BACKGROUND_WHITE.toString() + TextFormatting.COLOR_BLACK, // lv 2
        TextFormatting.BACKGROUND_BRIGHT_BLUE.toString() + TextFormatting.COLOR_BLACK, // lv 3
        TextFormatting.BACKGROUND_BLUE.toString() + TextFormatting.COLOR_BLACK // lv 4
    };

    /**
     * Color of cpu message
     */
    private static final String COLOR_CPU = TextFormatting.COLOR_CYAN.toString();

    /**
     * Random symbols for every match
     */
    private final String symbols;

    /**
     * Buffer used to get message from user
     */
    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private String currentPlayer = "";

    public CLI(Client client, String symbols){
        this.client = client;
        this.symbols = symbols;
    }

    @Override
    public void displayFirstWindow() { // tested
        while(!client.connect())
        {
            println(TextFormatting.RESET+"Server unreachable!");
            print(TextFormatting.RESET+"Type new ip address " + TextFormatting.input());
            client.setIp(new Scanner(System.in).nextLine());
            print(TextFormatting.RESET+"Type new port " + TextFormatting.input());
            try {
                client.setPort(Integer.parseInt(new Scanner(System.in).nextLine()));
            } catch (NumberFormatException nfe) { client.setPort(1234); }
        }
    }

    @Override
    public void handleMessage(CheckMoveServer message) { // tested
        if(message.name.equals(client.getUsername())){
            print(TextFormatting.RESET+"Cell Available: ");
            for(SnapCell cell : message.sc) {
                print(TextFormatting.COLOR_RED +
                        "(" + (cell.row + 1) + "-");
                if (cell.column == 0) {
                    print(TextFormatting.COLOR_RED + "A");
                } else if (cell.column == 1) {
                    print(TextFormatting.COLOR_RED + "B");
                } else if (cell.column == 2) {
                    print(TextFormatting.COLOR_RED + "C");

                } else if (cell.column == 3) {
                    print(TextFormatting.COLOR_RED + "D");

                } else if (cell.column == 4) {
                    print(TextFormatting.COLOR_RED + "E");
                }
                print(TextFormatting.COLOR_RED + ") ");
            }
            println("");
            boolean go = true;
            do{
                print(COLOR_CPU +"Type cell where you want to move [x-y] " + TextFormatting.input());
                SnapCell movingCell = readCell();
                if(movingCell!=null){
                    for(SnapCell cell : message.sc){
                       if(cell.toString().equals(movingCell.toString())){
                           client.sendMessage(new MoveClient(client.getUsername(), movingCell.row, movingCell.column));
                           go = false;
                       }
                    }
                }
                if(go)
                    print(COLOR_CPU +"Please insert a valid cell! ");
            }while(go && client.getContinueReading());
        }
    }

    @Override
    public void handleMessage(CheckBuildServer message) { // tested
        if(message.name.equals(client.getUsername())){
            print(TextFormatting.RESET+"Cell Available: ");
            for(SnapCell cell : message.sc) {
                print(TextFormatting.COLOR_RED +
                        "(" + (cell.row + 1) + "-");
                if (cell.column == 0) {
                    print(TextFormatting.COLOR_RED + "A");
                } else if (cell.column == 1) {
                    print(TextFormatting.COLOR_RED + "B");
                } else if (cell.column == 2) {
                    print(TextFormatting.COLOR_RED + "C");

                } else if (cell.column == 3) {
                    print(TextFormatting.COLOR_RED + "D");

                } else if (cell.column == 4) {
                    print(TextFormatting.COLOR_RED + "E");
                }
                print(TextFormatting.COLOR_RED + ") ");
            }
            println("");
            boolean go = true;
            do{
                print(COLOR_CPU +"Type cell where you want to build [x-y] " + TextFormatting.input());
                SnapCell movingCell = readCell();
                if (movingCell != null) {
                    for(SnapCell cell : message.sc){
                        if(cell.toString().equals(movingCell.toString())){
                            client.sendMessage(new BuildClient(client.getUsername(), movingCell.row, movingCell.column));
                            go = false;
                        }
                    }
                }
                if(go)
                    print(COLOR_CPU +"Please insert a valid cell! ");
            }while(go && client.getContinueReading());
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) { // tested
        getPlayerByName(message.player).card = message.cardName;
        clear();
        printTitle();
        printLobby(0);
    }

    @Override
    public void handleMessage(WorkerChosenServer message) { // tested
        clear();
        client.getWorkers().add(new SnapWorker(message.x,message.y,message.player,message.worker));
        printTitle();
        printTable();

        if(message.player.equals(this.client.getUsername())){
            if(message.worker==1){
                // second worker
                SnapCell c;
                do {
                    print(COLOR_CPU +"Type the position of second worker [x-y] " + TextFormatting.input());
                    c = getWorkerCell();
                }while(c == null && client.getContinueReading());
                if (c != null)
                    client.sendMessage(new WorkerInitializeClient(client.getUsername(),c.row,c.column));
            }
        }
        else{
            for(int i=0;i<client.getPlayers().size()-1;i++){
                if(client.getPlayers().get(i).name.equals(message.player) &&
                        client.getPlayers().get(i+1).name.equals(client.getUsername()) &&
                        message.worker == 2)
                {
                    // first worker
                    SnapCell c;
                    do {
                        print(COLOR_CPU +"Type the position of first worker [x-y] " + TextFormatting.input());
                        c = getWorkerCell();
                    }while(c == null && client.getContinueReading());
                    if (c != null)
                        client.sendMessage(new WorkerInitializeClient(client.getUsername(),c.row,c.column));
                }
            }
        }
    }

    private SnapCell getWorkerCell() {
        SnapCell c;
        c = readCell();
        if(c!=null){
            for(SnapWorker sw : client.getWorkers()){
                if (sw.row == c.row && sw.column == c.column) {
                    c = null;
                    break;
                }
            }
        }
        return c;
    }

    @Override
    public void handleMessage(QuestionAbilityServer message) { // TODO test
        do {
            clearLine();
            println(COLOR_CPU +"Type if you want to use the Ability of your God [YES/Y] or not [NO/N] " + TextFormatting.input());
            String answer;
            answer = read();
            if (answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("NO") || answer.equalsIgnoreCase("N")) {
                client.sendMessage(new AnswerAbilityClient(client.getUsername(), answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y"), message.status));
                break;
            }
        }while(client.getContinueReading());
    }

    @Override
    public void handleMessage(CurrentStatusServer message) {
        if(!client.getMyPlayer().loser && message.player.equals(client.getUsername())){
            switch(message.status){
                case WORKER_CHOICE:
                    clear();
                    printTitle();
                    printTable();
                    SnapCell c;
                    do { // first worker of the match
                        clearLine();
                        print(COLOR_CPU + "Type the position of first worker [x-y] " + TextFormatting.input());
                        c = getWorkerCell();
                    }while(c == null && client.getContinueReading());
                    if (c != null)
                        client.sendMessage(new WorkerInitializeClient(client.getUsername(),c.row,c.column));
                    break;
                case START:
                    println(COLOR_CPU +"It's your turn!"+TextFormatting.RESET);
                    println(TextFormatting.COLOR_YELLOW + "TIMER: " + (message.timer == 0 ? "inf." : message.timer)+TextFormatting.RESET);
                    boolean go = true;
                    clearLine();
                    print(COLOR_CPU +"Choose the worker to play with [x-y] " + TextFormatting.input());
                    do {
                        c = readCell();
                        if(c != null){
                            for(SnapWorker sw : client.getWorkers()){
                                if (sw.row == c.row && sw.column == c.column && sw.name.equals(client.getUsername()) && (message.worker1 && sw.n == 1 || message.worker2 && sw.n == 2)){
                                    client.sendMessage(new WorkerChoseClient(client.getUsername(),sw.n));
                                    go = false;
                                    break;
                                }
                            }
                        }
                        if(c != null && go){
                            clearLine();
                            print(COLOR_CPU +"Selected worker isn't valid, choose the worker to play with [x-y] " + TextFormatting.input());
                        }
                    } while(go && client.getContinueReading());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void handleMessage(SomeoneLoseServer message) { // TODO test
        for(SnapPlayer sp : client.getPlayers())
            if(sp.name.equals(message.player))
                sp.loser = true;

        ArrayList<SnapWorker> toDelete = new ArrayList<>();
        for(SnapWorker sw : client.getWorkers())
            if(sw.name.equals(message.player))
                toDelete.add(sw);
        client.removeWorkers(toDelete);


        if(this.client.getUsername().equals(message.player)){
            clear();
            printLose(message.isTimesUp);
        }
        else{
            clear();
            printTitle();
            printTable();
            println(TextFormatting.loser() + message.player + " has lost!" + TextFormatting.RESET);
        }
    }

    @Override
    public void handleMessage(AvailableCardServer message) { // tested
        if(message.cardName.isEmpty()){
            // im the challenger
            ArrayList<CardName> chosen = new ArrayList<>();

            println(COLOR_CPU +"You are the challenger! Choose "+client.getPlayers().size()+" card from:");
            printCard(new ArrayList<>(Arrays.asList(CardName.values())));

            // first
            CardName read;

            read = null;
            while(read == null && client.getContinueReading()){
                clearLine();
                print(COLOR_CPU +"Type the first card [name] " + TextFormatting.input());

                read = getCardName();
            }
            chosen.add(read);

            // second
            read = null;
            while((read == null || chosen.contains(read)) && client.getContinueReading()){
                clearLine();
                print(COLOR_CPU +"Type the second card [name] " + TextFormatting.input());
                read = getCardName();
            }
            chosen.add(read);

            // third
            if(client.getPlayers().size()==3){
                read = null;
                while((read == null || chosen.contains(read)) && client.getContinueReading()){
                    clearLine();
                    print(COLOR_CPU +"Type the third card [name] " + TextFormatting.input());
                    read = getCardName();
                }
                chosen.add(read);
            }

            if(client.getContinueReading())
                client.sendMessage(new ChallengerChoseClient(client.getUsername(), chosen));
        }
        else {
            println(COLOR_CPU +"The challenger has chosen! Select your card:");
            printCard(message.cardName);
            // first
            CardName read;
            do{
                clearLine();
                print(COLOR_CPU +"Type the chosen one [name] " + TextFormatting.input());
                String name;
                name = read();
                startEasterEgg(name);
                try{
                    read=Enum.valueOf(CardName.class,name.toUpperCase());
                    if(!message.cardName.contains(read))
                        read = null;
                }
                catch(Exception ex){read = null;}
            }while(read == null && client.getContinueReading());
            if(client.getContinueReading())
                client.sendMessage(new PlayerChoseClient(client.getUsername(), read));
        }
        print(COLOR_CPU +"Waiting for opponent's choice...");
    }

    private CardName getCardName() {
        CardName read;
        String name;
        name = read();
        startEasterEgg(name);
        try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
        catch(Exception ex){read = null;}
        return read;
    }

    @Override
    public void handleMessage(SomeoneWinServer message) { // TODO test
        clear();
        if(this.client.getUsername().equals(message.player))
            printWin();
        else {
            clear();
            printLose(message.isTimesUp);
        }
    }

    @Override
    public void handleMessage(NameRequestServer message) { // tested
        clear();
        printTitle();
        do{
            if(message.isFirstTime){
                clearLine();
                print(COLOR_CPU + "Type your username (max 12 characters) " + TextFormatting.input());
            }
            else{
                clearLine();
                print(COLOR_CPU + "The chosen one is not allowed, type new username (max 12 characters) " + TextFormatting.input());
            }
            String username = read();
            print(COLOR_CPU + "Validating username... " + TextFormatting.RESET);
            this.client.setUsername(username);
            message.isFirstTime = false;
        }while (this.client.getUsername().isEmpty() || this.client.getUsername().length() > 12 || this.client.getUsername().matches("^\\s*$") && client.getContinueReading());
        client.sendMessage(new ConnectionClient(this.client.getUsername()));
    }

    @Override
    public void handleMessage(LobbyServer message) { // tested
        clear();
        printTitle();

        try{
            client.setPlayersByString(message.players);
            for(int i=0;i<client.getPlayers().size();i++){
                client.getPlayers().get(i).symbol = symbols.charAt(i)+"";
                if(i==0)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_RED.toString()+TextFormatting.COLOR_BLACK);
                else if(i==1)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_YELLOW.toString()+TextFormatting.COLOR_BLACK);
                else if(i==2)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_PURPLE.toString()+TextFormatting.COLOR_BLACK);
            }
            printLobby(0);
        }catch (Exception e){
            println(e.getMessage());
        }
    }

    @Override
    public void handleMessage(ModeRequestServer message) { // tested
        int mode;
        do{
            clearLine();
            print(COLOR_CPU + "Choose game mode (2 or 3 players) [2/3] " + TextFormatting.input());
            try
            {
                String stringMode = read();
                mode = Integer.parseInt(stringMode);
            }catch(Exception e)
            {
                mode = 0;
            }
        }while (mode != 2 && mode != 3 && client.getContinueReading());
        client.sendMessage(new ModeChoseClient(this.client.getUsername(),mode));
    }

    @Override
    public void handleMessage(BuiltServer message) { // tested
        clear();
        printTitle();
        printTable();
    }

    @Override
    public void handleMessage(MovedServer message) { // tested
        clear();
        printTitle();
        printTable();
    }

    @Override
    public void handleMessage(PingServer pingServer) {

    }

    @Override
    public void handleMessage(TimeOutServer message) {
        if(message.n == 0 && !client.getMyPlayer().loser){
            clear();
            printTitle();
            printTable();
        }
        if(!client.getMyPlayer().loser) println(TextFormatting.UNDERLINE.toString() + TextFormatting.COLOR_RED + message.player + " has a network problem... Reconnection test " + (message.n+1) + " of " + (message.of+1) + TextFormatting.RESET);
    }

    @Override
    public void handleMessage(ReConnectionServer message) {
        if(message.player.equals(client.getUsername())){
            client.setMustPrint(true);
            client.setBoard(message.board);
            client.setWorkers(message.workers);
            client.setPlayersBySnap(message.players);
            for(int i=0;i<client.getPlayers().size();i++){
                client.getPlayers().get(i).symbol = symbols.charAt(i)+"";
                if(i==0)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_RED.toString()+TextFormatting.COLOR_BLACK);
                else if(i==1)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_YELLOW.toString()+TextFormatting.COLOR_BLACK);
                else if(i==2)
                    client.getPlayers().get(i).color = (TextFormatting.BACKGROUND_BRIGHT_PURPLE.toString()+TextFormatting.COLOR_BLACK);
            }
            this.currentPlayer=message.currentPlayer;
            clear();
            printTitle();
            printLobby(message.type);
        } else System.out.println(COLOR_CPU + message.player+" IS BACK!" + TextFormatting.RESET);

        //lobby refused
        boolean go = true;
        while(go && client.getContinueReading()){
            go = false;
            print(COLOR_CPU + "Type [2/3] if you want to refuse saved lobby and start a new game " + TextFormatting.input());
            String text = read();
            if(text.equalsIgnoreCase("2") || text.equalsIgnoreCase("3")){
                client.setMustPrint(false);
                client.resetMatch();
                ModeChoseClient mcc = new ModeChoseClient(client.getUsername(),Integer.parseInt(text));
                mcc.refused = true;
                client.sendMessage(mcc);
            } else go = true;
        }
    }

    public void endMatch(){
        boolean go = true;
        while(go && client.getContinueReading()){
            go = false;
            print(COLOR_CPU + "Type [2/3] if you want to start a new game, [QUIT] if you want to close the game " + TextFormatting.input());
            String text = read();
            if(text.equalsIgnoreCase("2") || text.equalsIgnoreCase("3")){
                println(COLOR_CPU + "MUST IMPLEMENT NEW LOBBY!" + TextFormatting.RESET);
                client.resetMatch();
                client.sendMessage(new ModeChoseClient(client.getUsername(),Integer.parseInt(text)));
            } else if(text.equalsIgnoreCase("QUIT")){
                close(false);
            } else go = true;
        }
    }

    // ********************************************************************************************************* //

    public void clear(){
        for(int i=0;i<60;i++)
            println("");
    }

    public void startEasterEgg(String name){
        if(name.equalsIgnoreCase("NIKE")){
            println("PRINT EASTER EGG!");
        }
    }

    public void printLose(boolean isTimesUp){
        if(isTimesUp){
            println(TextFormatting.loser()+ "              88888888888 d8b                       d8b                                               "+TextFormatting.RESET);
            println(TextFormatting.loser()+ "              88888888888 Y8P                       88P                                               "+TextFormatting.RESET);
            println(TextFormatting.loser()+ "                  888                               8P                                                "+TextFormatting.RESET);
            println(TextFormatting.loser()+ "  d8b d8b d8b     888     888 88888b.d88b.   .d88b. \"  .d8888b       888  888 88888b.     d8b d8b d8b "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "  Y8P Y8P Y8P     888     888 888 \"888 \"88b d8P  Y8b   88K           888  888 888 \"88b    Y8P Y8P Y8P "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "                  888     888 888  888  888 88888888   \"Y8888b.      888  888 888  888                "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "  d8b d8b d8b     888     888 888  888  888 Y8b.            X88      Y88b 888 888 d88P    d8b d8b d8b "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "  Y8P Y8P Y8P     888     888 888  888  888  \"Y8888     88888P'       \"Y88888 88888P\"     Y8P Y8P Y8P "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "                                                                              888                     "+ TextFormatting.RESET);

            println(TextFormatting.loser()+ "  d8b d8b d8b                                        888                      888         d8b d8b d8b "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "  Y8P Y8P Y8P                                        888                      888         Y8P Y8P Y8P "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "                                                     888                                              "+ TextFormatting.RESET);
        } else {
            println(TextFormatting.loser()+ "                                                     888                                              "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "                                                     888                                              "+ TextFormatting.RESET);
            println(TextFormatting.loser()+ "                                                     888                                              "+ TextFormatting.RESET);
        }
        println(TextFormatting.loser()+ "  d8b d8b d8b       888  888  .d88b.  888  888       888  .d88b.  .d8888b   .d88b.        d8b d8b d8b "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "  Y8P Y8P Y8P       888  888 d88\"\"88b 888  888       888 d88\"\"88b 88K      d8P  Y8b       Y8P Y8P Y8P "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "                    888  888 888  888 888  888       888 888  888 \"Y8888b. 88888888                   "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "  d8b d8b d8b       Y88b 888 Y88..88P Y88b 888       888 Y88..88P      X88 Y8b.           d8b d8b d8b "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "  Y8P Y8P Y8P        \"Y88888  \"Y88P\"   \"Y88888       888  \"Y88P\"   88888P'  \"Y8888        Y8P Y8P Y8P "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "                         888                                                                          "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "                    Y8b d88P                                                                          "+ TextFormatting.RESET);
        println(TextFormatting.loser()+ "                     \"Y88P\"                                                                           "+ TextFormatting.RESET);
        endMatch();
    }

    public void printWin(){
        println(TextFormatting.winner()+ "                Y88b   d88P  .d88888b.  888     888       888       888 8888888 888b    888                 " + TextFormatting.RESET);
        println(TextFormatting.winner()+ "      o          Y88b d88P  d88P' 'Y88b 888     888       888   o   888   888   8888b   888          o      " + TextFormatting.RESET);
        println(TextFormatting.winner()+ "     d8b          Y88o88P   888     888 888     888       888  d8b  888   888   88888b  888         d8b     " + TextFormatting.RESET);
        println(TextFormatting.winner()+ "    d888b          Y888P    888     888 888     888       888 d888b 888   888   888Y88b 888        d888b    " + TextFormatting.RESET);
        println(TextFormatting.winner()+ "'Y888888888P'       888     888     888 888     888       888d88888b888   888   888 Y88b888    'Y888888888P'" + TextFormatting.RESET);
        println(TextFormatting.winner()+ "   'Y88888P'        888     888     888 888     888       88888P Y88888   888   888  Y88888      'Y88888P'  " + TextFormatting.RESET);
        println(TextFormatting.winner()+ "  d88P'Y88b         888     Y88b. .d88P Y88b. .d88P       8888P   Y8888   888   888   Y8888      d88P'Y88b  " + TextFormatting.RESET);
        println(TextFormatting.winner()+ " dP'     'Yb        888      'Y88888P'   'Y88888P'        888P     Y888 8888888 888    Y888     dP'     'Yb " + TextFormatting.RESET);
        endMatch();
    }

    public void printTitle(){
        println(color[0]+"                             ____    _    _   _ _____ ___  ____  ___ _   _ ___                             " + TextFormatting.RESET);
        println(color[0]+"                            / ___|  / \\  | \\ | |_   _/ _ \\|  _ \\|_ _| \\ | |_ _|                            " + TextFormatting.RESET);
        println(color[0]+"                            \\___ \\ / _ \\ |  \\| | | || | | | |_) || ||  \\| || |                             " + TextFormatting.RESET);
        println(color[0]+"                             ___) / ___ \\| |\\  | | || |_| |  _ < | || |\\  || |                             " + TextFormatting.RESET);
        println(color[0]+"                            |____/_/   \\_\\_| \\_| |_| \\___/|_| \\_\\___|_| \\_|___|                            " + TextFormatting.RESET);
        println(color[0]+"                                                                                                           "+TextFormatting.RESET);
    }

    public static void println(String string){
        System.out.println(string);
        System.out.flush();
    }
    public static void print(String string){
        System.out.print(string);
        System.out.flush();
    }

    @Override
    public void statusHandler(CurrentStatusServer message){
        if(client.getMyPlayer() != null && !client.getMyPlayer().loser && !message.player.equals(client.getUsername())){
            if (message.status == Status.CARD_CHOICE) {
                println(COLOR_CPU +"Waiting for opponent's choice..." + TextFormatting.RESET);
            } else this.currentPlayer=message.player;
        }
    }

    @Override
    public void close(boolean isError) {
        client.sendMessage(new DisconnectionClient(client.getUsername(),isError));
        if(isError)
            println(COLOR_CPU + "A network problem was encountered, you have been disconnected!" + TextFormatting.RESET);
        else println(COLOR_CPU + "Thank you for playing Santorini!" + TextFormatting.RESET);
        System.exit(0);
    }

    @Override
    public void displayBoard() {
        clear();
        printTitle();
        printTable();
    }

    public void clearLine(){
        try{
            while (System.in.available() > 0)
                in.readLine();
        }catch (Exception ignored){}
    }

    public String read(){
        String tmp = "";
        do{
            try {
                if(System.in.available() > 0)
                   tmp = in.readLine();
            } catch (IOException e) {
                System.out.println("[READ] - "+e.toString());
                tmp = "";
            }
        } while(tmp.isEmpty() && client.getContinueReading());
        return tmp;
    }

    public SnapCell readCell(){
        boolean go;
        int x;
        int y;
        try{
            String tmp = read();
            tmp = tmp.toUpperCase();
            String[] tmps = tmp.split("-");
            x = Integer.parseInt(tmps[0]) -1;
            y = "ABCDE".contains(tmps[1]) && tmps[1].length() == 1 ? "ABCDE".indexOf(tmps[1]) : -1;
            go = x < 0 || x > 4 || y < 0;
            if(!go)
                return new SnapCell(x,y,-1);
            return null;
        }
        catch(Exception e){return null;}
    }

    public void printTable(){
        String[] print = new String[26];

        for(int i=0;i<print.length;i++)
            print[i] = color[0] + ((i-3)%5 == 0 ? (i-3)/5+1 : " ")+" ";

        for(SnapCell cell : client.getBoard())
        {
            int shift = cell.row*5;
            if(cell.row == 0 && cell.column == 0)
                print[shift] += printSymbol("╭");
            else if(cell.column == 0)
                print[shift] += printSymbol("├");
            else if(cell.row == 0)
                print[shift] += printSymbol("┬");
            else print[shift] += printSymbol("┼");
            print[shift]+= printSymbol("───────────");

            if(cell.level == 4){
                print[shift + 1] += printSymbol("│") + color[cell.level-1] + "          4" + TextFormatting.RESET;
                print[shift + 2] += printSymbol("│") + color[cell.level-1] + "   " +color[cell.level]+ "     "+color[cell.level-1]+"   " + TextFormatting.RESET;
                print[shift + 3] += printSymbol("│") + color[cell.level-1] + "   " +color[cell.level]+ "     "+color[cell.level-1]+"   " + TextFormatting.RESET;
                print[shift + 4] += printSymbol("│") + color[cell.level-1] + "           " + TextFormatting.RESET;
            }
            else {
                print[shift + 1] += printSymbol("│") + color[cell.level] + "          " + (cell.level == 0 ? " " : cell.level) + TextFormatting.RESET;
                print[shift + 2] += printSymbol("│") + color[cell.level] + "   " + printUserSymbol(cell)+ color[cell.level] + "   " + TextFormatting.RESET;
                print[shift + 3] += printSymbol("│") + color[cell.level] + "   " + printUserSymbol(cell)+ color[cell.level] + "   " + TextFormatting.RESET;
                print[shift + 4] += printSymbol("│") + color[cell.level] + "           " + TextFormatting.RESET;
            }
            if(cell.column == 4){
                if(cell.row == 0)
                    print[shift]+=printSymbol("╮");
                else print[shift]+=printSymbol("┤");
                print[shift+1]+=printSymbol("│");
                print[shift+2]+=printSymbol("│");
                print[shift+3]+=printSymbol("│");
                print[shift+4]+=printSymbol("│");
            }
        }
        print[25]+=printSymbol("╰───────────┴───────────┴───────────┴───────────┴───────────╯");

        String[] tmp = new String[27];

        // add header
        tmp[0]=(color[0]+"        A           B           C           D           E      "+TextFormatting.RESET);
        System.arraycopy(print, 0, tmp, 1, print.length);
        print = tmp;

        print = printPlayers(print);

        for (String s : print) println(s + TextFormatting.RESET);


        if(!client.getMyPlayer().loser && !currentPlayer.equals(client.getUsername())){
            println(COLOR_CPU +"Opponent turn, " + currentPlayer + " is playing..." + TextFormatting.RESET);
        }

        if(client.getMyPlayer().loser)
            endMatch();
    }

    public String[] printPlayers(String[] print){
        for(int i=0;i<print.length;i++)
            print[i] += color[0] + "      ";

        int factor = 9;
        for(int i=0;i<client.getPlayers().size();i++){
            String name = client.getPlayers().get(i).symbol +"  "+client.getPlayers().get(i).name+"  "
                    +client.getPlayers().get(i).symbol;

            if(client.getPlayers().get(i).card != null)
                name += "  "+client.getPlayers().get(i).card.name()+"  "+client.getPlayers().get(i).symbol;

            print[factor*i] += client.getPlayers().get(i).color+"╭────────────────────────────────────╮"+TextFormatting.RESET;

            print[factor*i+1] += client.getPlayers().get(i).color+"│";

            for(int k=0;k<(36-(name).length())/2;k++)
                print[factor*i+1] += " ";
            print[factor*i+1] += name;
            for(int k=0;k<(36-(name).length())/2;k++)
                print[factor*i+1] += " ";

            print[factor*i+1] += name.length()%2 == 1 ? " │" : "│";
            print[factor*i+1] += TextFormatting.RESET;
            print[factor*i+2] += client.getPlayers().get(i).color+"├────────────────────────────────────┤"+TextFormatting.RESET;

            if(client.getPlayers().get(i).card == null) {
                print[factor*i+3] += client.getPlayers().get(i).color+"│                                    │"+TextFormatting.RESET;
                print[factor*i+4] += client.getPlayers().get(i).color+"│                                    │"+TextFormatting.RESET;
                print[factor*i+5] += client.getPlayers().get(i).color+"│           GOD NOT CHOSEN           │"+TextFormatting.RESET;
                print[factor*i+6] += client.getPlayers().get(i).color+"│                                    │"+TextFormatting.RESET;
                print[factor*i+7] += client.getPlayers().get(i).color+"│                                    │"+TextFormatting.RESET;
                print[factor*i+8] += client.getPlayers().get(i).color+"╰────────────────────────────────────╯"+TextFormatting.RESET;
            }
            else if (client.getPlayers().get(i).loser){
                print[factor*i+3] += client.getPlayers().get(i).color+"│        _                           │" +TextFormatting.RESET;
                print[factor*i+4] += client.getPlayers().get(i).color+"│       | | ___  ___  ___ _ __       │"+TextFormatting.RESET;
                print[factor*i+5] += client.getPlayers().get(i).color+"│       | |/ _ \\/ __|/ _ \\ '__|      │"+TextFormatting.RESET;
                print[factor*i+6] += client.getPlayers().get(i).color+"│       | | (_) \\__ \\  __/ |         │"+TextFormatting.RESET;
                print[factor*i+7] += client.getPlayers().get(i).color+"│       |_|\\___/|___/\\___|_|         │"+TextFormatting.RESET;
                print[factor*i+8] += client.getPlayers().get(i).color+"╰────────────────────────────────────╯"+TextFormatting.RESET;
            }
            else {

                String descr = client.getPlayers().get(i).card.getDescription();
                String[] descrs = descr.split(" ");
                int k = 0;
                String[] toPrint = new String[5];
                Arrays.fill(toPrint, "");

                for(int w=0;w<toPrint.length;w++){
                    while((toPrint[w]).length() < 32 && k<descrs.length){
                        if((toPrint[w]+descrs[k]).length() < 32)
                        {
                            toPrint[w] += descrs[k++]+" ";
                        }
                        else
                        {
                            printCardDescription(toPrint, w);
                        }
                    }
                    if(toPrint[w].length()<36){
                        printCardDescription(toPrint, w);
                    }
                }

                print[factor*i+3] += client.getPlayers().get(i).color+"│"+toPrint[0]+"│"+TextFormatting.RESET;
                print[factor*i+4] += client.getPlayers().get(i).color+"│"+toPrint[1]+"│"+TextFormatting.RESET;
                print[factor*i+5] += client.getPlayers().get(i).color+"│"+toPrint[2]+"│"+TextFormatting.RESET;
                print[factor*i+6] += client.getPlayers().get(i).color+"│"+toPrint[3]+"│"+TextFormatting.RESET;
                print[factor*i+7] += client.getPlayers().get(i).color+"│"+toPrint[4]+"│"+TextFormatting.RESET;
                print[factor*i+8] += client.getPlayers().get(i).color+"╰────────────────────────────────────╯"+TextFormatting.RESET;
            }
        }

        for(int i=0;i<3-client.getPlayers().size();i++)
            for(int j=0;j<9;j++)
                print[(2-i)*factor+j] += color[0]+"                                      "+TextFormatting.RESET;

        return print;
    }

    private void printCardDescription(String[] toPrint, int w) {
        StringBuilder tmp = new StringBuilder(toPrint[w]);
        for(int j=0;j<(36-toPrint[w].length())/2;j++)
            tmp.insert(0, " ");
        for(int j=0;j<(36-toPrint[w].length())/2;j++)
            tmp.append(" ");
        toPrint[w] = tmp.toString();
        if(toPrint[w].length()%2!=0) toPrint[w] += " ";
    }

    public String printUserSymbol(SnapCell cell){
        for(SnapWorker sw : client.getWorkers()){
            if(sw.row == cell.row && sw.column == cell.column){
                return getPlayerByName(sw.name).color + " "
                        + getPlayerByName(sw.name).symbol + getPlayerByName(sw.name).symbol + getPlayerByName(sw.name).symbol + " " + TextFormatting.RESET;
            }
        }
        return "     ";
    }
    public String printSymbol(String symbol){
        return color[0] + symbol + TextFormatting.RESET;
    }
    public SnapPlayer getPlayerByName(String name){
        for(SnapPlayer p : client.getPlayers())
            if(p.name.equals(name))
                return p;
        return null;
    }

    public void printLobby(int type){
        try {
            String[] toPrint = new String[7];
            Arrays.fill(toPrint, "");

            toPrint[0] = color[0] + "╭────────────────────────────────────╮";
            toPrint[1] = color[0] + "│               LOBBY                │";
            if (type == 2)
                toPrint[1] += "      Match resumed... Keep waiting for other reconnection!";
            else if(type == 1)
                toPrint[1] += "      A player had got a network problem, you are in a new lobby!";

            toPrint[2] = color[0] + "├────────────────────────────────────┤";

            StringBuilder tmp;

            for (int i = 0; i < 3; i++) {
                if (i < client.getPlayers().size()) {
                    toPrint[i + 3] = client.getPlayers().get(i).symbol + "  " + client.getPlayers().get(i).name + "  " + client.getPlayers().get(i).symbol;

                    if (client.getPlayers().get(i).card != null)
                        toPrint[i + 3] += "  " + client.getPlayers().get(i).card.name() + "  " + client.getPlayers().get(i).symbol;
                }
            }

            for (int i = 0; i < 3; i++) {
                tmp = new StringBuilder(toPrint[3 + i]);
                for (int j = 0; j < (36 - toPrint[3 + i].length()) / 2; j++)
                    tmp.insert(0, " ");
                for (int j = 0; j < (36 - toPrint[3 + i].length()) / 2; j++)
                    tmp.append(" ");
                toPrint[3 + i] = tmp.toString();
                if (toPrint[3 + i].length() % 2 != 0) toPrint[3 + i] += " ";
            }

            toPrint[3] = color[0] + "│" + toPrint[3] + "│" + "      While waiting I advise you to read the rules:";
            toPrint[4] = color[0] + "│" + toPrint[4] + "│" + "      www.ultraboardgames.com/santorini/game-rules.php";
            toPrint[5] = color[0] + "│" + toPrint[5] + "│";
            toPrint[6] = color[0] + "╰────────────────────────────────────╯";

            for (String s : toPrint) {
                tmp = new StringBuilder(s);
                while (tmp.length() < 117)
                    tmp.append(" ");
                println(tmp.toString() + TextFormatting.RESET);
            }
        } catch (Exception ex){ println(ex.getMessage());}
    }

    public static void printCard(List<CardName> toPrint){
        StringBuilder out;
        int max = 0;
        for(CardName cn : CardName.values())
            max = Math.max(max,cn.name().length());
        for(CardName cn : CardName.values()) {
            if(toPrint.contains(cn)){
                String[] divided = cn.getDescription().split(" ");
                out = new StringBuilder(cn.name().toUpperCase() + " | ");
                for(int i=cn.name().length();i<max;i++)
                    out = new StringBuilder(" ").append(out);

                int i=0;
                do {
                    while (out.length() < 100 && i<divided.length) {
                        if (out.length() + divided[i].length() <= 100){
                            out.append(divided[i++]).append(" ");
                        } else {
                            while(out.length() != 100)
                                out.append(" ");
                        }
                    }
                    println(out.toString());
                    out = new StringBuilder(" │ ");
                    for(int k=0;k<max;k++)
                        out = new StringBuilder(" ").append(out);
                } while (i<divided.length);
            }
        }
    }
}
