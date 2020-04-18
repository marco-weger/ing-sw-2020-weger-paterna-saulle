package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;


public class CLI implements ViewInterface {

    Client client;

    ArrayList<String> color;
    String colorCPU;

    private static Scanner in = new Scanner(System.in);

    private static Logger LOGGER = Logger.getLogger("CLI");

    public CLI(Client client){
        this.client = client;

        color = new ArrayList<>();
        color.add(TextFormatting.BACKGROUND_CYAN.toString() + TextFormatting.COLOR_BLACK); // lv 0
        color.add(TextFormatting.BACKGROUND_WHITE.toString() + TextFormatting.COLOR_BLACK); // lv 1
        color.add(TextFormatting.BACKGROUND_BRIGHT_BLUE.toString() + TextFormatting.COLOR_BLACK); // lv 2
        color.add(TextFormatting.BACKGROUND_WHITE.toString() + TextFormatting.COLOR_BLACK); // lv 3
        color.add(TextFormatting.BACKGROUND_BLUE.toString() + TextFormatting.COLOR_BLACK); // lv 4

        colorCPU = TextFormatting.COLOR_CYAN.toString();
    }

    public void displayFirstWindow() {
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
    public void handleMessage(CheckMoveServer message) {
        clear();
        println("Cells available for move: ");
        if(message.name.equals(client.getUsername())){
            for (int i=0; 0<=i && i<message.sc.size(); i++) {
                println(""+message.sc.get(i).row + TextFormatting.SEPARATOR + message.sc.get(i).column);
            }
            //whit out [] parentheses!!
            print("Type [ROW" + TextFormatting.SEPARATOR +"COLUMN] where you want to move " + TextFormatting.input() );
            String movingCell = in.nextLine();
            while(!(movingCell.contains(TextFormatting.SEPARATOR.toString()) && (int) movingCell.charAt(0) <= 4 && (int) movingCell.charAt(2) <= 4)){
                println("Please insert a valid pair!");
                for (int i=0; 0<=i && i<message.sc.size(); i++) {
                    println(""+message.sc.get(i).row + TextFormatting.SEPARATOR + message.sc.get(i).column);
                }
                print("Type [ROW" + TextFormatting.SEPARATOR +"COLUMN] where you want to move " + TextFormatting.input() );
                movingCell = in.nextLine();
            }
            String[] s = movingCell.split(TextFormatting.SEPARATOR.toString());
            String movingRow = s[0];
            String movingColumn = s[1];
            client.sendMessage(new MoveClient(client.getUsername(), movingRow.charAt(0), movingColumn.charAt(0)));
        }
    }

    @Override
    public void handleMessage(CheckBuildServer message) {
        clear();
        println("Cells available for build: ");
        if(message.name.equals(client.getUsername())){
            for (int i=0; 0<=i && i<message.sc.size(); i++) {
                println(""+message.sc.get(i).row + TextFormatting.SEPARATOR + message.sc.get(i).column);
            }
            //whit out [] parentheses!!
            print("Type [ROW" + TextFormatting.SEPARATOR +"COLUMN] where you want to build: " + TextFormatting.input() );
            String buildingCell = in.nextLine();
            while(!(buildingCell.contains(TextFormatting.SEPARATOR.toString()) && (int)buildingCell.charAt(0) <= 4 && (int)buildingCell.charAt(2) <= 4)) {
                println("Please insert a valid pair!");
                for (int i=0; 0<=i && i<message.sc.size(); i++) {
                    println(""+message.sc.get(i).row + TextFormatting.SEPARATOR + message.sc.get(i).column);
                }
                print("Type [ROW" + TextFormatting.SEPARATOR +"COLUMN] where you want to build: " + TextFormatting.input() );
                buildingCell = in.nextLine();
            }
            String[] s = buildingCell.split(TextFormatting.SEPARATOR.toString());
            String buildingRow = s[0];
            String buildingColumn = s[1];
            client.sendMessage(new MoveClient(client.getUsername(), buildingRow.charAt(0), buildingColumn.charAt(0)));
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) { // tested
        getPlayerbyName(message.player).card = message.cardName;
        clear();
        printTitle();
        printLobby(false);
        // playersCard.set(players.indexOf(message.player),message.cardName); // TODO test this part
    }

    @Override
    public void handleMessage(WorkerChosenServer message) {
        clear();
        client.getWorkers().add(new SnapWorker(message.x,message.y,message.player,message.worker));
        printTitle();
        printTable();

        if(message.player.equals(this.client.getUsername())){
            println("Worker chosen correctly!");
            if(message.worker==1){
                boolean go;
                String coord = "ABCDE";
                int x;
                String y;
                do{
                    print("TYPE THE POSITION OF SECOND WORKER [x-y]" + TextFormatting.input());
                    print("TYPE THE POSITION OF SECOND WORKER [x-y]" + TextFormatting.input());
                    String tmp = in.nextLine();
                    String[] tmps = tmp.split("-");
                    try{
                        x = Integer.parseInt(tmps[0]);
                        y = tmps[1];
                        go = x < 1 || x > 6 || (y.isEmpty()) || !(coord.contains(y));
                        for(SnapWorker sw : client.getWorkers()){
                            if (sw.row == x-1 && sw.column == coord.indexOf(y)) {
                                go = false;
                                break;
                            }
                        }
                        if(!go)
                            client.sendMessage(new WorkerInitializeClient(client.getUsername(),x,coord.indexOf(y)));
                    }
                    catch(Exception e){go = true;}
                }while(go);
            }
        }
        else{
            for(int i=0;i<client.getPlayers().size();i++){
                if(client.getPlayers().get(i).name.equals(message.player) &&
                        client.getPlayers().get((i+1)%client.getPlayers().size()).name.equals(client.getUsername()) &&
                        message.worker == 2){
                    boolean go;
                    int x;
                    String coord = "ABCDE";
                    String y = " ";
                    do{
                        print("TYPE THE POSITION OF FIRST WORKER [x-y]" + TextFormatting.input());
                        String tmp = in.nextLine();
                        String[] tmps = tmp.split("-");
                        try{
                            x = Integer.parseInt(tmps[0]);
                            y = tmps[1];
                            go = x < 1 || x > 6 || (y.isEmpty()) || !(coord.contains(y));
                            for(SnapWorker sw : client.getWorkers()){
                                if (sw.row == x-1 && sw.column == coord.indexOf(y)) {
                                    go = false;
                                    break;
                                }
                            }
                            if(!go)
                                client.sendMessage(new WorkerInitializeClient(client.getUsername(),x-1,coord.indexOf(y)));
                        }
                        catch(Exception e){go = true; System.err.println("MANNAGGIA OH"); }
                    }while(go);
                }
            }
        }
    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {
        clear();
        do {
            println("Want to use the Ability of your God [YES/NO] " + TextFormatting.input());
            String answer = in.nextLine();
            if (answer.toUpperCase().equals("YES")) {
                AnswerAbilityClient mex = new AnswerAbilityClient(client.getUsername(), true, Status.CHOSEN);
                client.sendMessage(mex);
                break;
            }
            if (answer.toUpperCase().equals("NO")) {
                AnswerAbilityClient mex = new AnswerAbilityClient(client.getUsername(), false, Status.CHOSEN);
                client.sendMessage(mex);
                break;
            }
        }while(true);
    }

    @Override
    public void handleMessage(CurrentStatusServer message) {
        // TODO when WORKER_CHOSE and my turn the client must validate the position!!!

         println(message.status.toString()+"-"+message.player);

         if(message.player.equals(client.getUsername())){
             println("IT'S YOUR TURN!");
             if(message.status.equals(Status.WORKER_CHOICE))
             {
                 clear();
                 printTitle();
                 printTable();
                 boolean go;
                 int x;
                 String coord = "ABCDE";
                 String y = " ";
                 do{
                     print("TYPE THE POSITION OF FIRST WORKER [x-y]" + TextFormatting.input());
                     String tmp = in.nextLine();
                     String[] tmps = tmp.split("-");
                     try{
                         x = Integer.parseInt(tmps[0]);
                         y = tmps[1];
                         go = x < 1 || x > 6 || (y.isEmpty()) || !(coord.contains(y));
                         for(SnapWorker sw : client.getWorkers()){
                             if (sw.row == x-1 && sw.column == coord.indexOf(y)) {
                                 go = false;
                                 break;
                             }
                         }
                         if(!go)
                             client.sendMessage(new WorkerInitializeClient(client.getUsername(),x-1,coord.indexOf(y)));
                     }
                     catch(Exception e){go = true; System.err.println(e.getMessage());}
                 }while(go);

             }
             clear();
             printTitle();
             printTable();
             boolean go;
             int x;
             String y;
             String coord = "ABCDE";
             int num = 0;
             if(message.status.equals(Status.START)) {
                 do {
                     print("CHOSE THE WORKER [x-y]" + TextFormatting.input());
                     String tmp = in.nextLine();
                     String[] tmps = tmp.split("-");
                     try {
                         x = Integer.parseInt(tmps[0]);
                         y = tmps[1];
                         go = x < 1 || x > 6 || (y.isEmpty()) || !(coord.contains(y));
                         for(SnapWorker sw : client.getWorkers()){
                             if (sw.row == x-1 && sw.column == coord.indexOf(y)) {
                                 go = false;
                                 break;
                             }
                         }
                         if (!go)
                             client.sendMessage(new WorkerChoseClient(client.getUsername(),num));
                     } catch (Exception e) {
                         go = true;
                     }
                 } while (go);
             }


             }
         }


    @Override
    public void handleMessage(SomeoneLoseServer message) {
        clear();
        if(this.client.getUsername().equals(message.name)){
            print(TextFormatting.loser()
                    +
                    "                                                     888                                              \n" +
                    "                                                     888                                              \n" +
                    "                                                     888                                              \n" +
                    "  d8b d8b d8b       888  888  .d88b.  888  888       888  .d88b.  .d8888b   .d88b.        d8b d8b d8b \n" +
                    "  Y8P Y8P Y8P       888  888 d88\"\"88b 888  888       888 d88\"\"88b 88K      d8P  Y8b       Y8P Y8P Y8P \n" +
                    "                    888  888 888  888 888  888       888 888  888 \"Y8888b. 88888888                   \n" +
                    "  d8b d8b d8b       Y88b 888 Y88..88P Y88b 888       888 Y88..88P      X88 Y8b.           d8b d8b d8b \n" +
                    "  Y8P Y8P Y8P        \"Y88888  \"Y88P\"   \"Y88888       888  \"Y88P\"   88888P'  \"Y8888        Y8P Y8P Y8P \n" +
                    "                         888                                                                          \n" +
                    "                    Y8b d88P                                                                          \n" +
                    "                     \"Y88P\"                                                                           \n"
            );
        }
        else
            println(TextFormatting.loser() + message.name + "have lost");

    }

    @Override
    public void handleMessage(AvailableCardServer message) { //tested
        if(message.cardName.size() == 0){
            // im the challenger
            ArrayList<CardName> chosen = new ArrayList<>();

            println(colorCPU+"You are the challenger! Chose "+client.getPlayers().size()+" card from:");
            printCard(new ArrayList<>(Arrays.asList(CardName.values())));

            // first
            CardName read;
            do{
                print(colorCPU+"Type the first card [uppercase name] " + TextFormatting.input());

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null);
            chosen.add(read);

            // second
            do{
                print(colorCPU+"Type the second card [uppercase name] " + TextFormatting.input());

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null || chosen.contains(read));
            chosen.add(read);

            // third
            if(client.getPlayers().size()==3){
                do{
                    print(colorCPU+"Type the third card [uppercase name] " + TextFormatting.input());
                    String name = in.nextLine();
                    try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                    catch(Exception ex){read = null;}
                }while(read == null || chosen.contains(read));
                chosen.add(read);
            }

            client.sendMessage(new ChallengerChoseClient(client.getUsername(), chosen));
            print(colorCPU+"Waiting for opponent's choice...");
        }
        else {
            println(colorCPU+"The challenger has chosen! Select your card:");
            printCard(message.cardName);

            // first
            CardName read;
            do{
                print(colorCPU+"Type the chosen one [uppercase name] " + TextFormatting.input());

                String name = in.nextLine();
                try{
                    read=Enum.valueOf(CardName.class,name.toUpperCase());
                    if(!message.cardName.contains(read))
                        read = null;
                }
                catch(Exception ex){read = null;}
            }while(read == null);
            client.sendMessage(new PlayerChoseClient(client.getUsername(), read));
            print(colorCPU+"Waiting for opponent's choice...");
        }
    }

    @Override
    public void handleMessage(SomeoneWinServer message) {
        clear();
        if(this.client.getUsername() .equals(message.name)) {
            print(TextFormatting.winner()
                    +
                    "                                               Y88b   d88P  .d88888b.  888     888       888       888 8888888 888b    888                                                      \n" +
                    "        o            o            o             Y88b d88P  d88P' 'Y88b 888     888       888   o   888   888   8888b   888             o            o            o              \n" +
                    "       d8b          d8b          d8b             Y88o88P   888     888 888     888       888  d8b  888   888   88888b  888            d8b          d8b          d8b             \n" +
                    "      d888b        d888b        d888b             Y888P    888     888 888     888       888 d888b 888   888   888Y88b 888           d888b        d888b        d888b            \n" +
                    "  'Y888888888P''Y888888888P''Y888888888P'          888     888     888 888     888       888d88888b888   888   888 Y88b888       'Y888888888P''Y888888888P''Y888888888P'        \n" +
                    "    'Y88888P'    'Y88888P''    'Y88888P'           888     888     888 888     888       88888P Y88888   888   888  Y88888         'Y88888P'    'Y88888P'    'Y88888P'          \n" +
                    "    d88P'Y88b    d88P'Y88b    d88P'Y88b            888     Y88b. .d88P Y88b. .d88P       8888P   Y8888   888   888   Y8888         d88P'Y88b    d88P'Y88b    d88P'Y88b          \n" +
                    "   dP'     'Yb  dP'     'Yb  dP'     'Yb           888      'Y88888P'   'Y88888P'        888P     Y888 8888888 888    Y888        dP'     'Yb  dP'     'Yb  dP'     'Yb         \n"
            );
        }
        else{
            print(TextFormatting.loser()
                    +
                    "                                                     888                                              \n" +
                    "                                                     888                                              \n" +
                    "                                                     888                                              \n" +
                    "  d8b d8b d8b       888  888  .d88b.  888  888       888  .d88b.  .d8888b   .d88b.        d8b d8b d8b \n" +
                    "  Y8P Y8P Y8P       888  888 d88\"\"88b 888  888       888 d88\"\"88b 88K      d8P  Y8b       Y8P Y8P Y8P \n" +
                    "                    888  888 888  888 888  888       888 888  888 \"Y8888b. 88888888                   \n" +
                    "  d8b d8b d8b       Y88b 888 Y88..88P Y88b 888       888 Y88..88P      X88 Y8b.           d8b d8b d8b \n" +
                    "  Y8P Y8P Y8P        \"Y88888  \"Y88P\"   \"Y88888       888  \"Y88P\"   88888P'  \"Y8888        Y8P Y8P Y8P \n" +
                    "                         888                                                                          \n" +
                    "                    Y8b d88P                                                                          \n" +
                    "                     \"Y88P\"                                                                           \n"
            );

            }
        }



    @Override
    public void handleMessage(NameRequestServer message) {
        clear();
        printTitle();
        do{
            if(message.isFirstTime)
                print(colorCPU + "Type your username (max 12 characters)" + TextFormatting.input());
            else
                print(colorCPU + "The chosen one is not allowed, type new username (max 12 characters)" + TextFormatting.input());
            this.client.setUsername(in.nextLine());
            message.isFirstTime = false;
        }while (this.client.getUsername().isEmpty() || this.client.getUsername().length() > 12);
        client.sendMessage(new ConnectionClient(this.client.getUsername()));
    }

    @Override
    public void handleMessage(LobbyServer message) {
        clear();
        printTitle();
        client.resetPlayers(); // = new ArrayList<>();
        for(String s : message.players)
            client.getPlayers().add(new SnapPlayer(s,client.getMyCode(),client.getPlayers().size()));
        printLobby(message.loaded);
        System.out.flush();
    }

    @Override
    public void handleMessage(ModeRequestServer message) {
        int mode;
        do{
            print(colorCPU + "Chose game mode (2 or 3 players) [2/3] " + TextFormatting.input());
            String stringMode = in.nextLine();
            try
            {
                mode = Integer.parseInt(stringMode);
            }catch(Exception e)
            {
                mode = 0;
            }
        }while (mode != 2 && mode != 3);
        client.sendMessage(new ModeChoseClient(this.client.getUsername(),mode));
    }

    @Override
    public void handleMessage(BuiltServer message) {

    }

    @Override
    public void handleMessage(MovedServer message) {

    }

    public void clear(){
        for(int i=0;i<70;i++)
            println("");
    }

    public void printTitle(){
        println(color.get(0)+"                             ____    _    _   _ _____ ___  ____  ___ _   _ ___                             " + TextFormatting.RESET);
        println(color.get(0)+"                            / ___|  / \\  | \\ | |_   _/ _ \\|  _ \\|_ _| \\ | |_ _|                            " + TextFormatting.RESET);
        println(color.get(0)+"                            \\___ \\ / _ \\ |  \\| | | || | | | |_) || ||  \\| || |                             " + TextFormatting.RESET);
        println(color.get(0)+"                             ___) / ___ \\| |\\  | | || |_| |  _ < | || |\\  || |                             " + TextFormatting.RESET);
        println(color.get(0)+"                            |____/_/   \\_\\_| \\_| |_| \\___/|_| \\_\\___|_| \\_|___|                            " + TextFormatting.RESET);
        //char[] charArray = new char[107];
        //Arrays.fill(charArray, ' ');
        //println(color.get(0)+new String(charArray)+TextFormatting.RESET);

        // TODO decide if use the separator
        //println(color.get(0)+"·················•·················•·················•·················•·················•·················"+TextFormatting.RESET);
        println(color.get(0)+"                                                                                                           "+TextFormatting.RESET);

        //println(color.get(0)+new String(charArray)+TextFormatting.RESET);


        /*
        try {
            TimeUnit.MILLISECONDS.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */
    }

    public static void println(String string){
        System.out.println(string);
        System.out.flush();
    }
    public static void print(String string){
        System.out.print(string);
        System.out.flush();
    }

    public void printTable(){
        String[] print = new String[26];

        for(int i=0;i<print.length;i++)
            print[i] = color.get(0) + ((i-3)%5 == 0 ? (i-3)/5+1 : " ")+" ";

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
                print[shift + 1] += printSymbol("│") + color.get(cell.level-1) + "          " + cell.level + TextFormatting.RESET;
                print[shift + 2] += printSymbol("│") + color.get(cell.level-1) + "   " +color.get(cell.level)+ "     "+color.get(cell.level-1)+"   " + TextFormatting.RESET;
                print[shift + 3] += printSymbol("│") + color.get(cell.level-1) + "   " +color.get(cell.level)+ "     "+color.get(cell.level-1)+"   " + TextFormatting.RESET;
                print[shift + 4] += printSymbol("│") + color.get(cell.level-1) + "           " + TextFormatting.RESET;
            }
            else {
                print[shift + 1] += printSymbol("│") + color.get(cell.level) + "          " + cell.level + TextFormatting.RESET;
                print[shift + 2] += printSymbol("│") + color.get(cell.level) + "   " + printUserSymbol(cell)+ color.get(cell.level) + "   " + TextFormatting.RESET;
                print[shift + 3] += printSymbol("│") + color.get(cell.level) + "   " + printUserSymbol(cell)+ color.get(cell.level) + "   " + TextFormatting.RESET;
                print[shift + 4] += printSymbol("│") + color.get(cell.level) + "           " + TextFormatting.RESET;
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
        tmp[0]=(color.get(0)+"        A           B           C           D           E      "+TextFormatting.RESET);
        System.arraycopy(print, 0, tmp, 1, print.length);
        print = tmp;

        print = printPlayers(print);

        for (String s : print) println(s + TextFormatting.RESET);
    }

    public String[] printPlayers(String[] print){
        for(int i=0;i<print.length;i++)
            print[i] += color.get(0) + "      ";

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
                            StringBuilder tmp = new StringBuilder(toPrint[w]);
                            for(int j=0;j<(36-toPrint[w].length())/2;j++)
                                tmp.insert(0, " ");
                            for(int j=0;j<(36-toPrint[w].length())/2;j++)
                                tmp.append(" ");
                            toPrint[w] = tmp.toString();
                            if(toPrint[w].length()%2!=0) toPrint[w] += " ";
                        }
                    }
                    if(toPrint[w].length()<36){
                        StringBuilder tmp = new StringBuilder(toPrint[w]);
                        for(int j=0;j<(36-toPrint[w].length())/2;j++)
                            tmp.insert(0, " ");
                        for(int j=0;j<(36-toPrint[w].length())/2;j++)
                            tmp.append(" ");
                        toPrint[w] = tmp.toString();
                        if(toPrint[w].length()%2!=0) toPrint[w] += " ";
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
                print[(2-i)*factor+j] += color.get(0)+"                                      "+TextFormatting.RESET;

        return print;
    }

    public String printUserSymbol(SnapCell cell){
        for(SnapWorker sw : client.getWorkers()){
            if(sw.row == cell.row && sw.column == cell.column){
                return TextFormatting.BOLD + getPlayerbyName(sw.name).color + " "
                        + getPlayerbyName(sw.name).symbol + getPlayerbyName(sw.name).symbol + getPlayerbyName(sw.name).symbol + " " + TextFormatting.RESET;
            }
        }
        return "     ";
    }
    public String printSymbol(String symbol){
        return color.get(0) + symbol + TextFormatting.RESET;
    }
    public SnapPlayer getPlayerbyName(String name){
        for(SnapPlayer p : client.getPlayers())
            if(p.name.equals(name))
                return p;
        return null;
    }

    public void printLobby(Boolean loaded){
        String[] toPrint = new String[7];
        Arrays.fill(toPrint, "");

        toPrint[0] = color.get(0)+"╭────────────────────────────────────╮";
        toPrint[1] = color.get(0)+"│               LOBBY                │";
        if(loaded)
            toPrint[1] += "      Match resumed... Keep waiting for other reconnection!";

        toPrint[2] = color.get(0)+"├────────────────────────────────────┤";

        StringBuilder tmp;

        for(int i=0;i<3; i++){
            if(i<client.getPlayers().size()){
                toPrint[i+3] = client.getPlayers().get(i).symbol +"  "+client.getPlayers().get(i).name+"  "+client.getPlayers().get(i).symbol;

                if(client.getPlayers().get(i).card != null)
                    toPrint[i+3] += "  "+client.getPlayers().get(i).card.name()+"  "+client.getPlayers().get(i).symbol;
            }
        }

        for(int i=0;i<3; i++){
            tmp = new StringBuilder(toPrint[3+i]);
            for(int j=0;j<(36-toPrint[3+i].length())/2;j++)
                tmp.insert(0, " ");
            for(int j=0;j<(36-toPrint[3+i].length())/2;j++)
                tmp.append(" ");
            toPrint[3+i] = tmp.toString();
            if(toPrint[3+i].length()%2!=0) toPrint[3+i] += " ";
        }

        toPrint[3] = color.get(0)+"│"+toPrint[3]+"│"+"      While waiting I advise you to read the rules:";
        toPrint[4] = color.get(0)+"│"+toPrint[4]+"│"+"      www.ultraboardgames.com/santorini/game-rules.php";
        toPrint[5] = color.get(0)+"│"+toPrint[5]+"│"+"      There is an easter egg, try to find it!";
        toPrint[6] = color.get(0)+"╰────────────────────────────────────╯";

        for (String s : toPrint){
            tmp = new StringBuilder(s);
            while(tmp.length() < 117)
                tmp.append(" ");
            println(tmp.toString()+TextFormatting.RESET);
        }

        println(color.get(0)+"·················•·················•·················•·················•·················•·················"+TextFormatting.RESET);
    }

    public static void printCard(ArrayList<CardName> toPrint){
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
