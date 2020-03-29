package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Match;

public class Controller implements Observer {

    /**
     * The MODEL
     * The CONTROLLER is the only one allowed to modify the MODEL
     */
    private Match match;

    // TODO: VIRTUALVIEW, TURN

    public Controller(Match match){
        this.match = match;
    }

    @Override
    public void notifyObserver(Object arg){

        /*
        if( ! (arg instanceof AnswerEvent))
            throw new RuntimeException("This must be an AnswerEvent object");

        AnswerEvent answerEvent = (AnswerEvent) arg;

        answerEvent.acceptEventHandler(this);
         */

    }


    //public void inizializeMatch();

    //public void cardChoicheHandler():

    //public void setupWorkerHandler();
}
