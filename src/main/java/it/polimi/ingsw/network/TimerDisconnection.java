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
     * The number of try
     */
    long numberOfTry;

    /**
     * True while the timer is running
     */
    boolean alive;

    /**
     *
     * @param sch the client
     * @param ses the thread manager
     * @param numberOfTry number of try
     */
    public TimerDisconnection(ServerClientHandler sch, ScheduledExecutorService ses, long numberOfTry){
        this.sch = sch;
        this.ses = ses;
        this.numberOfTry = numberOfTry;
        this.count = 0;
        this.alive = true;

        if(sch.getPing() != null)
            sch.getPing().cancel();

        try{
            sch.getVirtualView().getTurn().cancel();
        }catch (Exception ignored){}
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
        //System.out.println(count+"/"+numberOfTry);
        if(this.alive && sch.isStillConnected() && sch.getVirtualView() != null && !sch.getVirtualView().getCurrentStatus().equals(Status.NAME_CHOICE)) {
            try {
                if (sch.getIn().available() != 0) { // if the connection is reestablished
                    sch.setStillConnected(true);
                    sch.startPing();
                    ses.shutdown(); // stop timer
                } else { // send the try to all clients
                    sch.getServer().sendAll(new TimeOutServer(sch.getName(), count, numberOfTry), sch.getVirtualView());
                }
            } catch (Exception e) {
                System.out.println("[TO] - " + e.getMessage());
            }

            if (++count > numberOfTry) { // the timer's end
                sch.disconnectionHandler();
                sch.setStillConnected(false);
                this.alive = false;
                ses.shutdown();
            }
        } else {
            sch.disconnectionHandler();
            sch.setStillConnected(false);
            this.alive = false;
            ses.shutdown();
        }
    }
}
