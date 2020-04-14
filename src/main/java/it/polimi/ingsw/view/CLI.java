package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.clientMessages.ModeChoseClient;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class CLI implements ViewInterface {

    Client client;
    String username;

    private static Scanner in = new Scanner(System.in);

    private ArrayList<String> players;

    private static Logger LOGGER = Logger.getLogger("CLI");

    public CLI(Client client){
        this.client = client;
        this.username = "";
    }

    // TODO: could be an interface of ViewInterface
    public void displayFirstWindow() {
        // TODO get from config file
        String ip = "127.0.0.1";
        int port = 1234;
        while(!client.connect(ip, port))
        {
            println("Server unreachable!");
            print("Type new ip address" + TextFormatting.getInputLine());
            ip = new Scanner(System.in).nextLine();
            print("Type new port" + TextFormatting.getInputLine());
            try {
                port = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException nfe) { port = 1234; }
        }
    }

    @Override
    public void handleMessage(CheckMoveServer message) {

    }

    @Override
    public void handleMessage(CheckBuildServer message) {

    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {

    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {
        clear();
        printTitle();
        // System.out.println("CURRENT STATUS IS " + message.status.toString());
    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {
        clear();
        printTitle();
        if(message.cardName.size() == 0){
            // im the challenger
            ArrayList<CardName> chosen = new ArrayList<>();

            println("YOU ARE THE CHALLENGER! CHOSE "+this.players.size()+" CARD FROM:");
            for(CardName cn : CardName.values())
                println("- "+cn.name().toUpperCase()+" - "+cn.getDescription());

            // first
            CardName read;
            do{
                print("TYPE THE FIRST CARD" + TextFormatting.INPUT);

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null);
            chosen.add(read);

            // second
            do{
                print("TYPE THE SECOND CARD" + TextFormatting.INPUT);

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null || chosen.contains(read));
            chosen.add(read);

            // third
            if(this.players.size()==3){
                do{
                    print("TYPE THE THIRD CARD" + TextFormatting.INPUT);
                    String name = in.nextLine();
                    try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                    catch(Exception ex){read = null;}
                }while(read == null || chosen.contains(read));
                chosen.add(read);
            }

            println("SEND DECISION");
        }
    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void handleMessage(NameRequestServer message) {
        clear();
        printTitle();

        do{
            if(message.isFirstTime)
                print("TYPE YOUR USERNAME" + TextFormatting.INPUT);
            else
                print("THE CHOSEN ONE IS NOT ALLOWED, TYPE YOUR USERNAME" + TextFormatting.getInputLine());
            this.username = in.nextLine();
            message.isFirstTime = false;
        }while (this.username.isEmpty());

        client.sendMessage(new ConnectionClient(this.username));
    }

    @Override
    public void handleMessage(LobbyServer message) {
        clear();
        printTitle();

        this.players=message.players;
        println("CURRENT LOBBY");
        for (String name:this.players)
            println("- "+name);
    }

    @Override
    public void handleMessage(ModeRequestServer message) {
        clear();
        printTitle();

        int mode;
        do{
            print("CHOSE GAME MODE (2 OR 3 PLAYERS) [CLI/GUI]" + TextFormatting.INPUT);
            String stringMode = in.nextLine();
            try
            {
                mode = Integer.parseInt(stringMode);
            }catch(Exception e)
            {
                mode = 0;
            }
        }while (mode != 2 && mode != 3);

        client.sendMessage(new ModeChoseClient(this.username,mode));
    }

    public void clear(){
        for(int i=0;i<40;i++)
            println("");
    }

    public static void printTitle(){
        print( TextFormatting.initialize()
                + "                          ad88888ba        db        888b      88 888888888888 ,ad8888ba,   88888888ba  88 888b      88 88                           \n" +
                "                         d8\"     \"8b      d88b       8888b     88      88     d8\"'    `\"8b  88      \"8b 88 8888b     88 88                           \n" +
                "                         Y8,             d8'`8b      88 `8b    88      88    d8'        `8b 88      ,8P 88 88 `8b    88 88                           \n" +
                "                         `Y8aaaaa,      d8'  `8b     88  `8b   88      88    88          88 88aaaaaa8P' 88 88  `8b   88 88                           \n" +
                "                           `\"\"\"\"\"8b,   d8YaaaaY8b    88   `8b  88      88    88          88 88\"\"\"\"88'   88 88   `8b  88 88                           \n" +
                "                                 `8b  d8\"\"\"\"\"\"\"\"8b   88    `8b 88      88    Y8,        ,8P 88    `8b   88 88    `8b 88 88                           \n" +
                "                         Y8a     a8P d8'        `8b  88     `8888      88     Y8a.    .a8P  88     `8b  88 88     `8888 88                           \n" +
                "                          \"Y88888P\" d8'          `8b 88      `888      88      `\"Y8888Y\"'   88      `8b 88 88      `888 88                           \n"
        );

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
}
