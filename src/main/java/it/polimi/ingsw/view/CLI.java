package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.network.Client;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLI implements ViewInterface {

    Client client;
    String username;

    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    private static Logger LOGGER = Logger.getLogger("CLI");

    public CLI(Client client){
        this.client = client;
        this.username = "";
    }

    // TODO: could be an interface of ViewInterface
    public void displayFirstWindow() {
        try {
            out.println("TEST");
            TimeUnit.MILLISECONDS.sleep(1200);
            out.println("SANTORINI");
            TimeUnit.MILLISECONDS.sleep(1200);
            out.println("FIRST RUN");
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }

        out.print("● TYPE YOUR USERNAME\n► ");
        out.flush();
        this.username = in.nextLine();

        while (this.username.isEmpty() || this.username.matches("^\\s*$")){
            out.print("● THE CHOSEN ONE IS NOT ALLOWED, TYPE YOUR USERNAME\n►");
            this.username = in.nextLine();
        }

        out.println("● CURRENT LOBBY");
        out.println("● "+this.username);

        out.println("\nWaiting other players...\n\n");

        if(client.connect())
            in.nextLine();
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

    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {

    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void handleMessage(NameRequestServer message) {
        //ConnectionClient cm = new ConnectionClient(this.username);
        //cm.name = this.username;
        //System.out.println(cm.name);
        client.sendMessage(new ConnectionClient(this.username));
    }

    @Override
    public void handleMessage(OpponentConnection message) {

    }
}
