package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.serverMessages.MovedServer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;

public class Athena extends Card {

    public Athena(CardName name, boolean active, boolean opponent, boolean question, Status status, VirtualView vw) {
        super(name, active, opponent, question, status ,vw);
    }

    /**
     *if Athena is active, return a list of opponent's blocked cell
     *
     * @param p list of player
     * @param b board
     * @param w worker
     * @param current status
     *
     * @return list of opponent's blocked cell
     */
    @Override
    protected ArrayList<Cell> activeBlock(ArrayList<Player> p, Board b, Worker w,  Status current) {
        if(p == null || b == null ) return new ArrayList<>(0);
        ArrayList<Cell> ret = new ArrayList<>();
        if(current == Status.QUESTION_M){
            for (Cell c : b.getField())
                if (Math.abs(c.getRow() - w.getRow()) <= 1 && Math.abs(c.getColumn() - w.getColumn()) <= 1 && c.getLevel() < 4 && c.getLevel() == w.getLevel(b) + 1 && !c.isOccupied(p))
                    ret.add(c);
        }
        return ret;
    }


    /**
     * @param p list of player
     * @param b board
     * @param to where to move
     */
    @Override
    public boolean move(ArrayList<Player> p, Board b, Cell to){
        if (!(p == null || b == null || to == null)) {
            Player current = null;
            for (Player player : p)
                if (player.getCard().getName().compareTo(this.getName()) == 0)
                    current = player;
            if (current != null) {
                if (current.getCurrentWorker() != null) {
                    ArrayList<Cell> available = checkMove(p, b);
                    for (Player player : p)
                        if (player.getCard().isOpponent() && player.getCard().isActive() && player != current)
                            available.removeAll(player.getCard().activeBlock(p, b, current.getCurrentWorker(),Status.QUESTION_M));
                    if (available.contains(to)) {
                       if(to.getLevel() == current.getCurrentWorker().getLevel(b) + 1)
                            current.getCard().setActive(true);
                        current.getCurrentWorker().move(to.getRow(), to.getColumn());
                        notifyObservers(new MovedServer(new SnapWorker(to.getRow(),to.getColumn(),current.getName(),current.getWorker1().isActive() ? 1 : 2)));
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Reset the parameter isActive of Athena, if a new round is STARTED
     * and the player with Athena Card have used the power
     * (status player == START and  isActive current player ==1 )
     *
     */
    @Override
    public void initializeTurn() {
        super.setActive(false);
    }
}


