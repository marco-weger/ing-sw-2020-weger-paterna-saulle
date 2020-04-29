package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.clientMessages.PingClient;
import it.polimi.ingsw.commons.serverMessages.PingServer;

import java.util.TimerTask;

public class TimerPing extends TimerTask {

    /**
     * The client to be pinged
     */
    ServerClientHandler sch;
    Client c;

    /**
     * @param sch the client
     */
    public TimerPing(ServerClientHandler sch){ this.sch = sch; }
    public TimerPing(Client c){
        this.c = c;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        if(sch != null) new Thread(() -> sch.notify(new PingServer(sch.getName()))).start();
        else if(c != null) new Thread(() -> c.sendMessage(new PingClient(c.getUsername()))).start();
    }
}
