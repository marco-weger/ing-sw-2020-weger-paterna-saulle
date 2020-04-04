package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Controller implements Observer, ClientMessageHandler {

    /**
     * The MODEL
     * The CONTROLLER is the only one allowed to modify the MODEL
     */
    private Match match;
    private VirtualView virtualView;

    public Controller(Match match, VirtualView virtualView) {
        this.match = match;
        this.virtualView = virtualView;
    }

    @Override
    public void update(Object arg){
        if( ! (arg instanceof ClientMessage))
            throw new RuntimeException("This must be an ClientMessage object");
        ClientMessage cm = (ClientMessage) arg;
        cm.Accept(this);

        // TODO: not sure this is the best way to call correct method... test!
        // new Thread(() -> cm.Accept(this)).start();
    }

    @Override
    public void handleMessage(ConnectionClient message) {

    }

    @Override
    public void handleMessage(DisconnectionClient event) {

    }

    @Override
    public void handleMessage(ReConnectionClient message) {

    }

    @Override
    public void handleMessage(Ping event) {

    }

    @Override
    public void handleMessage(ChallengerChoseClient message) {
        if(match.getStatus().compareTo(Status.CARD_CHOICE) == 0
                && message.name.equals(match.getPlayers().get(0).getName())
                && message.c.size() == match.getPlayers().size()
                && match.getSelectedCard().size() == 0
        ){
            ArrayList<CardName> tmp = new ArrayList<>();
            for(int i = 0; i < match.getPlayers().size(); i++)
                tmp.add(message.c.get(i));

            match.setSelectedCards(tmp);
        }
    }

    @Override
    public void handleMessage(PlayerChoseClient message) {
        String who = "";
        for(Player p:match.getPlayers())
            if(p.getCard() == null)
                who = p.getName();

        if(match.getStatus().compareTo(Status.CARD_CHOICE) == 0
                && message.name.equals(who)
                && match.getSelectedCard().contains(message.c)
        )
        {
            match.getSelectedCard().remove(message.c);
            for (Player p:match.getPlayers()){
                if(p.getName().equals(who)){
                    p.setCard(message.c);
                }
            }
            if(match.getSelectedCard().size() == 1){
                // TODO: operatione to inizialize firs player
                match.setStatus(Status.WORKER_CHOICE);
            }
            else {
                match.setSelectedCards(match.getSelectedCard());
            }
        }
    }

    @Override
    public void handleMessage(WorkerInizializeClient message) {
        Player selected = null;
        for(Player p:match.getPlayers()){
            if(p.getName().equals(message.name)){
                selected = p;
                break;
            }
            else if(p.getWorker1() == null || p.getWorker2() == null){
                break;
            }
        }

        if(selected != null){
            if(selected.getWorker1() == null)
                selected.setWorker1(new Worker(message.x,message.y));
            else
                selected.setWorker2(new Worker(message.x,message.y));

            if(match.getPlayers().get(match.getPlayers().size()-1).getWorker2() != null)
                startTurn();
        }
    }

    @Override
    public void handleMessage(WorkerChoseClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name) && match.getStatus().compareTo(Status.START) == 0){
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
            match.getCurrentPlayer().setCurrentWorker(message.worker);

            if(match.getCurrentPlayer().getCard().isQuestion()
                    && match.getCurrentPlayer().getCard().getStatus().compareTo(match.getStatus()) == 0
                    && match.getCurrentPlayer().getCard().activable(match.getPlayers(),match.getBoard())
            )
            {
                match.getCurrentPlayer().doQuestion();
            }
            else{
                match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                match.getCurrentPlayer().getCard().getCheckMove(match.getPlayers(),match.getBoard());
            }
        }
    }

    @Override
    public void handleMessage(AnswerAbilityClient message) {
        if(match.getCurrentPlayer().getName().compareTo(message.name) == 0
                && match.getStatus().compareTo(message.type) == 0
        ){
            match.getCurrentPlayer().getCard().setActive(message.ability);
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
            if(match.getStatus().compareTo(Status.QUESTION_M) == 0){
                match.getCurrentPlayer().getCard().getCheckMove(match.getPlayers(),match.getBoard());
            }
            else if(match.getStatus().compareTo(Status.QUESTION_B) == 0){
                match.getCurrentPlayer().getCard().getCheckBuild(match.getPlayers(),match.getBoard());
            }
        }
    }

    @Override
    public void handleMessage(MoveClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name)
            && match.getStatus().compareTo(Status.QUESTION_M) == 0
        ){
            Cell from = match.getBoard().getCell(match.getCurrentPlayer().getCurrentWorker().getRow(),match.getCurrentPlayer().getCurrentWorker().getRow());
            if(match.getCurrentPlayer().getCard().move(match.getPlayers(),match.getBoard(),match.getBoard().getCell(message.x,message.y))){

                if(match.getCurrentPlayer().getCard().checkWin(from,match.getBoard().getCell(message.x,message.y))){
                    endGame(match.getCurrentPlayer());
                }

                match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                if(match.getCurrentPlayer().getCard().isQuestion()
                        && match.getCurrentPlayer().getCard().getStatus().compareTo(match.getStatus()) == 0
                        && match.getCurrentPlayer().getCard().activable(match.getPlayers(),match.getBoard())
                ){
                    match.getCurrentPlayer().doQuestion();
                }
                else
                {
                    match.getCurrentPlayer().getCard().getCheckBuild(match.getPlayers(),match.getBoard());
                    match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                }
            }
        }
    }

    @Override
    public void handleMessage(BuildClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name)
                && match.getStatus().compareTo(Status.QUESTION_B) == 0
        ){
            if(match.getCurrentPlayer().getCard().build(match.getPlayers(),match.getBoard(),match.getBoard().getCell(message.x,message.y))){
                match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                if(match.getStatus().equals(Status.END))
                {
                    startTurn();
                }
            }
        }
    }

    public void endGame(Player winner){
        for(Player p:match.getPlayers())
            if(p.getName().compareTo(winner.getName()) != 0)
                match.setLosers(p);
        match.setEnded(true);
        match.setStatus(Status.END);
    }

    public void inizializeMatch() {
        // TODO: valuteremo più avanti se usarlo
    }

    public void startTurn(){
        match.setNextPlayer();

        match.getCurrentPlayer().getCard().inizializeTurn();
        if(match.checkCurrentPlayerWin())
            endGame(match.getCurrentPlayer());
        else{
            if(match.getCurrentPlayer().getCard().hasLost(match.getPlayers(),match.getBoard())){
                startTurn();
            }
            else match.setStatus(Status.START);
        }
    }


}
