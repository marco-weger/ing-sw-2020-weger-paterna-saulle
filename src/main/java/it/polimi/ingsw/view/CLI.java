package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CLI implements ViewInterface {

    Client client;

    ArrayList<String> color;
    String colorCPU;

    public CLI(Client client){
        this.client = client;

        color = new ArrayList<>();
        color.add(TextFormatting.BACKGROUND_CYAN.toString() + TextFormatting.COLOR_BLACK); // lv 0
        color.add(TextFormatting.BACKGROUND_BRIGHT_WHITE.toString() + TextFormatting.COLOR_BLACK); // lv 1
        color.add(TextFormatting.BACKGROUND_WHITE.toString() + TextFormatting.COLOR_BLACK); // lv 2
        color.add(TextFormatting.BACKGROUND_BRIGHT_BLUE.toString() + TextFormatting.COLOR_BLACK); // lv 3
        color.add(TextFormatting.BACKGROUND_BLUE.toString() + TextFormatting.COLOR_BLACK); // lv 4

        colorCPU = TextFormatting.COLOR_CYAN.toString();
    }

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
            for(SnapCell cell : message.sc)
                print(TextFormatting.COLOR_RED+
                        "("+cell.row + "-" + cell.column+") "+TextFormatting.RESET);
            println("END.");
            boolean go = true;
            do{
                print(colorCPU+"Type cell where you want to move [x-y] " + TextFormatting.input());
                SnapCell movingCell = readCell();
                for(SnapCell cell : message.sc){
                   if(cell.toString().equals(movingCell.toString())){
                       client.sendMessage(new MoveClient(client.getUsername(), movingCell.row, movingCell.column));
                       go = false;
                   }
                }
                if(go)
                    print(colorCPU+"Please insert a valid cell! ");
            }while(go);
        }
    }

    @Override
    public void handleMessage(CheckBuildServer message) { // tested
        if(message.name.equals(client.getUsername())){
            for(SnapCell cell : message.sc)
                print(TextFormatting.COLOR_RED+
                        "("+cell.row + "-" + cell.column+") "+TextFormatting.RESET);
            println("END.");
            boolean go = true;
            do{
                print(colorCPU+"Type cell where you want to build [x-y] " + TextFormatting.input());
                SnapCell movingCell = readCell();
                for(SnapCell cell : message.sc){
                    if(cell.toString().equals(movingCell.toString())){
                        client.sendMessage(new BuildClient(client.getUsername(), movingCell.row, movingCell.column));
                        go = false;
                    }
                }
                if(go)
                    print(colorCPU+"Please insert a valid cell! ");
            }while(go);
        }
    }

    @Override
    public void handleMessage(CardChosenServer message) { // tested
        getPlayerbyName(message.player).card = message.cardName;
        clear();
        printTitle();
        printLobby(false);
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
                    print(colorCPU+"Type the position of second worker [x-y] " + TextFormatting.input());
                    c = readCell();
                    for(SnapWorker sw : client.getWorkers()){
                        if (sw.row == c.row && sw.column == c.column) {
                            c = null;
                            break;
                        }
                    }
                }while(c == null);
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
                        print(colorCPU+"Type the position of first worker [x-y] " + TextFormatting.input());
                        c = readCell();
                        for(SnapWorker sw : client.getWorkers()){
                            if (sw.row == c.row && sw.column == c.column) {
                                c = null;
                                break;
                            }
                        }
                    }while(c == null);
                    client.sendMessage(new WorkerInitializeClient(client.getUsername(),c.row,c.column));
                }
            }
        }
    }

    @Override
    public void handleMessage(QuestionAbilityServer message) { // TODO test
        do {
            println(colorCPU+"Do you want to use the Ability of your God? [YES/NO] " + TextFormatting.input());
            String answer = read();
            if (answer.toUpperCase().equals("YES")) {
                client.sendMessage(new AnswerAbilityClient(client.getUsername(), true, message.status));
                break;
            }
            if (answer.toUpperCase().equals("NO")) {
                client.sendMessage(new AnswerAbilityClient(client.getUsername(), false, message.status));
                break;
            }
        }while(true);
        println(colorCPU+"Request Send!..... loading.... " + TextFormatting.input());

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {
        // TODO when WORKER_CHOSE and my turn the client must validate the position!!!
         if(message.player.equals(client.getUsername())){
             println(colorCPU+"It's your turn!"+TextFormatting.RESET);
             SnapCell c;
             switch(message.status){
                 case WORKER_CHOICE:
                     clear();
                     printTitle();
                     printTable();
                     do { // first worker of the match
                         print(colorCPU + "Type the position of first worker [x-y] " + TextFormatting.input());
                         c = readCell();
                         for(SnapWorker sw : client.getWorkers()){
                             if (sw.row == c.row && sw.column == c.column) {
                                 c = null;
                                 break;
                             }
                         }
                     }while(c == null);
                     client.sendMessage(new WorkerInitializeClient(client.getUsername(),c.row,c.column));
                     break;
                 case START:
                     clear();
                     printTitle();
                     printTable();
                     boolean go = true;
                     do {
                         print(colorCPU+"Chose the worker to play with [x-y] " + TextFormatting.input());
                         c = readCell();
                         if(c != null){
                             for(SnapWorker sw : client.getWorkers()){
                                 if (sw.row == c.row && sw.column == c.column && sw.name.equals(client.getUsername())){
                                     client.sendMessage(new WorkerChoseClient(client.getUsername(),sw.n));
                                     go = false;
                                     break;
                                 }
                             }
                         }
                     } while(go);
                     break;
                 /*
                 case MOVED:
                     clear();
                     printTitle();
                     printTable();
                     break;
                  */
                 case QUESTION_M:
                     break;
                 default:
                     println(TextFormatting.COLOR_RED.toString() + message.status + " - " + message.player + TextFormatting.RESET);
                     break;
             }
         }
         else {
             switch(message.status){
                 case CARD_CHOICE:
                     println(colorCPU+"Waiting for opponent's choice..."+ TextFormatting.RESET);
                     break;
                 default:
                     println(colorCPU+"Opponent turn, "+message.name+" is playing..."+ TextFormatting.RESET);
                     break;
             }
         }
    }

    @Override
    public void handleMessage(SomeoneLoseServer message) { // TODO test
        clear();
        if(this.client.getUsername().equals(message.player)){
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
            println(TextFormatting.loser() + message.name + "has lost");

    }

    @Override
    public void handleMessage(AvailableCardServer message) { // tested
        if(message.cardName.size() == 0){
            // im the challenger
            ArrayList<CardName> chosen = new ArrayList<>();

            println(colorCPU+"You are the challenger! Chose "+client.getPlayers().size()+" card from:");
            printCard(new ArrayList<>(Arrays.asList(CardName.values())));

            // first
            CardName read;
            do{
                print(colorCPU+"Type the first card [name] " + TextFormatting.input());

                String name = read();
                startEasterEgg(name);
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null);
            chosen.add(read);

            // second
            do{
                print(colorCPU+"Type the second card [name] " + TextFormatting.input());
                String name = read();
                startEasterEgg(name);
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null || chosen.contains(read));
            chosen.add(read);

            // third
            if(client.getPlayers().size()==3){
                do{
                    print(colorCPU+"Type the third card [name] " + TextFormatting.input());
                    String name = read();
                    startEasterEgg(name);
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
                print(colorCPU+"Type the chosen one [name] " + TextFormatting.input());
                String name = read();
                startEasterEgg(name);
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
    public void handleMessage(SomeoneWinServer message) { // TODO test
        clear();
        if(this.client.getUsername().equals(message.player)) {
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
    public void handleMessage(NameRequestServer message) { // tested
        clear();
        printTitle();
        do{
            if(message.isFirstTime)
                print(colorCPU + "Type your username (max 12 characters) " + TextFormatting.input());
            else
                print(colorCPU + "The chosen one is not allowed, type new username (max 12 characters) " + TextFormatting.input());
            this.client.setUsername(read());
            message.isFirstTime = false;
        }while (this.client.getUsername().isEmpty() || this.client.getUsername().length() > 12);
        client.sendMessage(new ConnectionClient(this.client.getUsername()));
    }

    @Override
    public void handleMessage(LobbyServer message) { // tested
        clear();
        printTitle();
        try{
            client.resetPlayers();
            for(String s : message.players)
                client.getPlayers().add(new SnapPlayer(s,client.getMyCode(),client.getPlayers().size()));
            printLobby(message.loaded);
        }catch (Exception e){println(e.getMessage());}
    }

    @Override
    public void handleMessage(ModeRequestServer message) { // tested
        int mode;
        do{
            print(colorCPU + "Chose game mode (2 or 3 players) [2/3] " + TextFormatting.input());
            String stringMode = read();
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

    public void clear(){
        for(int i=0;i<60;i++)
            println("");
    }

    // ********************************************************************************************************* //

    public void startEasterEgg(String name){
        if(name.toUpperCase().equals("NIKE")){
            println("PRINT EASTER EGG!");
        }
    }

    public void printTitle(){
        println(color.get(0)+"                             ____    _    _   _ _____ ___  ____  ___ _   _ ___                             " + TextFormatting.RESET);
        println(color.get(0)+"                            / ___|  / \\  | \\ | |_   _/ _ \\|  _ \\|_ _| \\ | |_ _|                            " + TextFormatting.RESET);
        println(color.get(0)+"                            \\___ \\ / _ \\ |  \\| | | || | | | |_) || ||  \\| || |                             " + TextFormatting.RESET);
        println(color.get(0)+"                             ___) / ___ \\| |\\  | | || |_| |  _ < | || |\\  || |                             " + TextFormatting.RESET);
        println(color.get(0)+"                            |____/_/   \\_\\_| \\_| |_| \\___/|_| \\_\\___|_| \\_|___|                            " + TextFormatting.RESET);

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
    public String read(){
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine().toUpperCase();
        } catch (IOException e) {
            //e.printStackTrace();
            return "";
        }
    }
    public SnapCell readCell(){
        boolean go;
        int x, y;
        try{
            String tmp = read();
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
                print[shift + 1] += printSymbol("│") + color.get(cell.level-1) + "          4" + TextFormatting.RESET;
                print[shift + 2] += printSymbol("│") + color.get(cell.level-1) + "   " +color.get(cell.level)+ "     "+color.get(cell.level-1)+"   " + TextFormatting.RESET;
                print[shift + 3] += printSymbol("│") + color.get(cell.level-1) + "   " +color.get(cell.level)+ "     "+color.get(cell.level-1)+"   " + TextFormatting.RESET;
                print[shift + 4] += printSymbol("│") + color.get(cell.level-1) + "           " + TextFormatting.RESET;
            }
            else {
                print[shift + 1] += printSymbol("│") + color.get(cell.level) + "          " + (cell.level == 0 ? " " : cell.level) + TextFormatting.RESET;
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

    public String[] printPlayers(@NotNull String[] print){
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
                return getPlayerbyName(sw.name).color + " "
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

        //println(color.get(0)+"·················•·················•·················•·················•·················•·················"+TextFormatting.RESET);
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
