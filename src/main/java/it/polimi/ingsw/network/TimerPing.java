package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.serverMessages.PingServer;

import java.util.TimerTask;

public class TimerPing extends TimerTask {

    /**
     * The client to be pinged
     */
    ServerClientHandler sch;

    /**
     * @param sch the client
     */
    public TimerPing(ServerClientHandler sch){
        this.sch = sch;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        new Thread(() -> sch.notify(new PingServer(sch.getName()))).start();
    }
}
