package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.clientMessages.ReadyClient;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class CLI implements ViewInterface, Runnable {

    Client client;
    String username;

    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    private ArrayList<String> players;

    private static Logger LOGGER = Logger.getLogger("CLI");
    Thread askSomething;
    boolean interrupted;

    public CLI(Client client){
        this.client = client;
        this.username = "";
        this.askSomething = new Thread();
        this.interrupted=false;
    }

    // TODO: could be an interface of ViewInterface
    public void displayFirstWindow() {
        // TODO get from config file
        String ip = "127.0.0.1";
        int port = 1234;
        while(!client.connect(ip, port))
        {
            System.out.println("Server unreachable!");
            System.out.print("Type new ip address" + TextFormatting.getInputLine());
            System.out.flush();
            ip = new Scanner(System.in).nextLine();
            System.out.print("Type new port" + TextFormatting.getInputLine());
            System.out.flush();
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

            out.println("YOU ARE THE CHALLENGER! CHOSE "+this.players.size()+" CARD FROM:");
            for(CardName cn : CardName.values())
                out.println("- "+cn.name().toUpperCase()+" - "+cn.getDescription());
            out.flush();

            // first
            CardName read;
            do{
                out.print("TYPE THE FIRST CARD" + TextFormatting.INPUT);
                out.flush();

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null);
            chosen.add(read);

            // second
            do{
                out.print("TYPE THE SECOND CARD" + TextFormatting.INPUT);
                out.flush();

                String name = in.nextLine();
                try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                catch(Exception ex){read = null;}
            }while(read == null || chosen.contains(read));
            chosen.add(read);

            // third
            if(this.players.size()==3){
                do{
                    out.print("TYPE THE THIRD CARD" + TextFormatting.INPUT);
                    out.flush();
                    String name = in.nextLine();
                    try{read=Enum.valueOf(CardName.class,name.toUpperCase());}
                    catch(Exception ex){read = null;}
                }while(read == null || chosen.contains(read));
                chosen.add(read);
            }

            System.out.println("SEND DECISION");
            out.flush();
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
                out.print("TYPE YOUR USERNAME" + TextFormatting.INPUT);
            else
                out.print("THE CHOSEN ONE IS NOT ALLOWED, TYPE YOUR USERNAME" + TextFormatting.getInputLine());
            out.flush();
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
        out.println("CURRENT LOBBY");
        for (String name:this.players)
            out.println("- "+name);

        if(!interrupted)
            askIfReady();
    }

    public void askIfReady(){
        // FIXME
        // 1) prima opzione decidere che per avviare la partita serve sempre ol "pronto" di tutti
        // 2) Passare parametro al thread
        this.interrupted=false;
        askSomething = new Thread(this);
        askSomething.start();
    }

    public void clear(){
        while(askSomething.isAlive()){
            this.interrupted=true;
        }
        for(int i=0;i<40;i++)
            out.println();
    }

    public static void printTitle(){
        out.print( TextFormatting.initialize()
                + "                          ad88888ba        db        888b      88 888888888888 ,ad8888ba,   88888888ba  88 888b      88 88                           \n" +
                "                         d8\"     \"8b      d88b       8888b     88      88     d8\"'    `\"8b  88      \"8b 88 8888b     88 88                           \n" +
                "                         Y8,             d8'`8b      88 `8b    88      88    d8'        `8b 88      ,8P 88 88 `8b    88 88                           \n" +
                "                         `Y8aaaaa,      d8'  `8b     88  `8b   88      88    88          88 88aaaaaa8P' 88 88  `8b   88 88                           \n" +
                "                           `\"\"\"\"\"8b,   d8YaaaaY8b    88   `8b  88      88    88          88 88\"\"\"\"88'   88 88   `8b  88 88                           \n" +
                "                                 `8b  d8\"\"\"\"\"\"\"\"8b   88    `8b 88      88    Y8,        ,8P 88    `8b   88 88    `8b 88 88                           \n" +
                "                         Y8a     a8P d8'        `8b  88     `8888      88     Y8a.    .a8P  88     `8b  88 88     `8888 88                           \n" +
                "                          \"Y88888P\" d8'          `8b 88      `888      88      `\"Y8888Y\"'   88      `8b 88 88      `888 88                           \n"
        );
        out.flush();

        /*
        try {
            TimeUnit.MILLISECONDS.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        String read;
        out.print("TYPE \"READY\" WHEN YOU ARE" + TextFormatting.INPUT);
        out.flush();
        while (!interrupted) {
            try {
                //Scanner s = new Scanner(System.in);
                //s.hasNext()
                if(System.in.available() > 0){
                //if(s.hasNext()){
                    // new Scanner(System.in).nextLine();
                    read = new Scanner(System.in).nextLine(); //in.nextLine();
                    if(read.equalsIgnoreCase("READY")) {
                        client.sendMessage(new ReadyClient(username));
                        interrupted = true;
                        break;
                    }
                    else{
                        out.print("TYPE \"READY\" WHEN YOU ARE" + TextFormatting.INPUT);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                interrupted = true;
            }
        }
    }
}
