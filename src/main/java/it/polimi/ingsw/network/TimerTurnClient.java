package it.polimi.ingsw.network;

import it.polimi.ingsw.view.gui.GUI;
import javafx.scene.control.Button;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

public class TimerTurnClient extends TimerTask {

    /**
     * The client
     */
    private final GUI gui;

    /**
     * The number of try
     */
    private long times;

    /**
     * The times counter
     */
    private long count;

    /**
     * The task
     */
    public ScheduledExecutorService ses;

    /**
     * It sets the task
     * @param gui
     * @param ses
     * @param times
     */
    public TimerTurnClient(GUI gui, ScheduledExecutorService ses, long times){
        //System.out.println("[TIMER]");
        this.gui = gui;
        this.times = times;
        this.ses = ses;
        this.count = 0;
        gui.setTimerText(times);
    }

    /**
     * The timer task runs every 1 second and send the countdown to the client
     */
    @Override
    public void run() {
        gui.setTimerText(times-++count);
        if(times==count){
            gui.unShowTimer();
            ses.shutdown();
            this.cancel();
        }
    }
}