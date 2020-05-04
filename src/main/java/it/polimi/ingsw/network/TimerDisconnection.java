package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.TimeOutServer;

import java.util.concurrent.ScheduledExecutorService;

public class TimerDisconnection implements Runnable {

    /**
     * The client
     */
    final ServerClientHandler sch;

    /**
     * Thread
     */
    final ScheduledExecutorService ses;

    /**
     * The counter of execution
     */
    int count;

    /**
     * Period for a reconnection check
     */
    long reconnectionPeriod;

    /**
     * True while the timer is running
     */
    boolean alive;

    /**
     *
     * @param sch the client
     * @param ses the thread manager
     * @param reconnectionPeriod period for a reconnection check
     */
    public TimerDisconnection(ServerClientHandler sch, ScheduledExecutorService ses, long reconnectionPeriod){
        this.sch = sch;
        this.ses = ses;
        this.reconnectionPeriod = reconnectionPeriod;
        this.count = 0;
        this.alive = true;

        System.out.println("\tTIME OUT THREAD!");
        if(sch.getPing() != null)
            sch.getPing().cancel();

        sch.setStillConnected(false);
        sch.getVirtualView().getTurn().cancel();
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
        while(this.alive) {
            if (sch.getVirtualView() == null) {
                sch.disconnectionHandler();
                this.alive = false;
                ses.shutdown();
            } else if (sch.getVirtualView().getCurrentStatus().equals(Status.NAME_CHOICE)) {
                sch.disconnectionHandler();
                this.alive = false;
                ses.shutdown();
            } else if (!sch.isStillConnected()) {
                try {
                    if (sch.getIn().available() != 0) { // if the connection is reestablished
                        sch.setStillConnected(true);
                        sch.startPing();
                        this.alive = false;
                        ses.shutdown(); // stop timer
                    } else { // send the try to all clients
                        sch.getServer().sendAll(new TimeOutServer("", sch.getName(), count, sch.getServer().getdisconnectTimer() / 15), sch.getVirtualView());
                        System.out.println(count + "/" + (sch.getServer().getdisconnectTimer() / reconnectionPeriod));
                    }
                } catch (Exception e) {
                    System.out.println("[TO] - " + e.getMessage());
                }

                if (++count > (sch.getServer().getdisconnectTimer() / reconnectionPeriod)) { // the timer's end
                    sch.disconnectionHandler();
                    this.alive = false;
                    ses.shutdown();
                }
            } else {
                sch.disconnectionHandler();
                this.alive = false;
                ses.shutdown();
            }
        }
    }
}
