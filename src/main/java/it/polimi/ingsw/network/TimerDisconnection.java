package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.TimeOutServer;

import java.util.concurrent.ScheduledExecutorService;

public class TimerDisconnection implements Runnable {

    ServerClientHandler sch;
    ScheduledExecutorService ses;
    int count, reconnectionPeriod;

    public TimerDisconnection(ServerClientHandler sch, ScheduledExecutorService ses, int reconnectionPeriod){
        this.sch = sch;
        this.ses = ses;
        this.reconnectionPeriod = reconnectionPeriod;
        this.count = 0;

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
        if(sch.getVirtualView() == null){
            sch.disconnectionHandler();
            ses.shutdown();
        }
        else if(sch.getVirtualView().getCurrentStatus().equals(Status.NAME_CHOICE)){
            sch.disconnectionHandler();
            ses.shutdown();
        }
        else if(!sch.isStillConnected()){
            try {
                if(sch.getIn().available() != 0){
                    sch.setStillConnected(true);
                    sch.startPing();
                    ses.shutdown();
                }
                sch.getServer().sendAll(new TimeOutServer("", sch.getName(),count, sch.getServer().getdisconnectTimer()/15),sch.getVirtualView());
                //Thread.sleep(reconnectionPeriod*1000);
                System.out.println(count+"/"+(sch.getServer().getdisconnectTimer()/reconnectionPeriod));
            } catch (Exception e) {
                System.out.println("[TO] - "+e.getMessage());
            }

            if(++count>(sch.getServer().getdisconnectTimer()/reconnectionPeriod)){
                sch.disconnectionHandler();
                ses.shutdown();
            }
        }
        else {
            sch.disconnectionHandler();
            ses.shutdown();
        }
    }
}
