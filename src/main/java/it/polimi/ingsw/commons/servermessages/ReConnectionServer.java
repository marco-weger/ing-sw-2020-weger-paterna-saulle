package it.polimi.ingsw.commons.servermessages;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

public class ReConnectionServer extends ServerMessage {
    @Override
    public void accept(ViewInterface vi) {vi.handleMessage(this);}

    public List<SnapCell> board;
    public List<SnapPlayer> players;
    public List<SnapWorker> workers;
    public String player;
    public String currentPlayer;
    public int type;

    public ReConnectionServer(String player, int type) {
        super();
        this.player=player;
        this.type=type;
    }
}
