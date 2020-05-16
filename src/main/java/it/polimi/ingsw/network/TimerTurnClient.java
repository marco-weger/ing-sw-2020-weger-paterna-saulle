package it.polimi.ingsw.network;

import it.polimi.ingsw.view.gui.GUI;
import javafx.scene.control.Button;

import java.util.TimerTask;

public class TimerTurnClient extends TimerTask {

    /**
     * The client
     */
    final GUI gui;

    long times;

    long count;

    public TimerTurnClient(GUI gui, long times){
        System.out.println("[TIMER]");
        this.gui = gui;
        this.times = times;
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
            this.cancel();
        }
    }
}