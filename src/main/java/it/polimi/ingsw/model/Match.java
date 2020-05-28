package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.commons.SnapPlayer;

import java.io.*;
import java.util.*;

public class Match extends Observable implements Serializable {

    /**
     * Unique id to discriminate the match
     */
    private final int id;

    /**
     * The board
     */
    private Board board;

    /**
     * List of players (2 or 3)
     */
    private List<Player> players;

    /**
     * List of losers
     */
    private final List<Player> losers;

    /**
     * Status of the match
     */
    private Status status;

    /**
     * True if the match is ended
     */
    private boolean ended;

    /**
     * This list is used during cards matching
     */
    private ArrayList<CardName> selectedCard;

    /**
     * The match is initialized with these;
     * - BOARD: 5x5 cell board with all cells at 0 level
     * - ENDED: false
     * - STATUS: CARD_CHOICE
     * - PLAYERS: empty list
     * - SELECTEDCARD: empty list
     * - LOSERS: empty list
     * @param id the unique match id
     */
    public Match(int id, VirtualView vv){
        this.id=id;
        this.board=new Board();
        this.ended=false;
        this.status=Status.NAME_CHOICE;
        this.players = new ArrayList<>();
        this.losers = new ArrayList<>();
        this.selectedCard = new ArrayList<>();
        if(vv != null)
            this.addObserver(vv);
    }

    public int getId() { return id; }

    public boolean isEnded() { return ended; }

    public void setEnded(boolean ended) { this.ended = ended; }

    public Status getStatus() { return status; }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<CardName> getSelectedCard(){ return selectedCard; }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getLosers() {
        return losers;
    }


    /**
     * When the status changes the view will be notified with current one and current player
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;

        if(status.equals(Status.WORKER_CHOICE) && getCurrentPlayer() == null)
            players.get(0).setCurrent(true);

        if(getCurrentPlayer() != null){
            CurrentStatusServer message = new CurrentStatusServer(getCurrentPlayer().getName(),status);
            if (status.equals(Status.START)){
                getCurrentPlayer().setCurrentWorker(1);
                message.worker1 = !getCurrentPlayer().getCard().checkMove(players,board).isEmpty();
                getCurrentPlayer().setCurrentWorker(2);
                message.worker2 = !getCurrentPlayer().getCard().checkMove(players,board).isEmpty();
                getCurrentPlayer().setCurrentWorker(0);
            }
            if(status.getNotify())
                notifyObservers(message);
        }
        else
            notifyObservers(new CurrentStatusServer("",status));

        if(status.equals(Status.CARD_CHOICE)){
            notifyObservers(new AvailableCardServer(this.players.get(0).getName(),new ArrayList<>()));
            //notifyObservers(new AvailableCardServer(new ArrayList<>()));
        }

    }


    /**
     * A method that add a player into the Loser List and remove it from the Active Player List
     * @param p select a player
     */
    public void setLosers(List<Player> p, boolean isTimesUp) {
        if(p.size() == 1 && p.get(0).isCurrent()){
            players.get((players.indexOf(getCurrentPlayer())+1)%(players.size())).setCurrent(true);
        }
        getLosers().addAll(p);
        getPlayers().removeAll(p);
        if(players.size() > 1)
            for (Player player : p)
                notifyObservers(new SomeoneLoseServer(player.getName(),isTimesUp));
        else notifyObservers(new SomeoneWinServer(players.get(0).getName(),isTimesUp));

        if(players.size() == 1){
            setEnded(true);
            setStatus(Status.END);
        }
    }


    /**
     * A method that pick the card from the deck and notify the third player to choose.
     * @param selectedCard the list of card selected
     */
    public void setSelectedCards(List<CardName> selectedCard){
        this.selectedCard = new ArrayList<>();
        this.selectedCard.addAll(selectedCard);
        int i = this.getSelectedCard().size()-1;
        notifyObservers(new AvailableCardServer(this.getPlayers().get(i).getName(), selectedCard));
    }


    /**
     * Current player getter
     */
    public Player getCurrentPlayer(){
        for(Player p:players)
            if(p.isCurrent())
                return p;
        return null;
    }


    /**
     * This method set next player, disable all workers and set START status
     */
    public void setNextPlayer() {
        if(getCurrentPlayer() != null){
            int i = players.indexOf(getCurrentPlayer());
            getCurrentPlayer().setCurrent(false);
            if (i < players.size()-1) {
                players.get(i + 1).setCurrent(true);
            } else {
                players.get(0).setCurrent(true);
            }
        }
        else{
            players.get(0).setCurrent(true);
        }
        for(Player p:players){
            p.getWorker1().setActive(false);
            p.getWorker2().setActive(false);
        }
        status=Status.START;
    }


    /**
     * @return It verifies if the current player win for other players defeat
     */
    public boolean checkCurrentPlayerWin() {
        if(players.size() == 1){
            notifyObservers(new SomeoneWinServer(players.get(0).getName(),false));
            return true;
        }
        return false;
    }


    /**
     * When a player is added to the list players
     * it notifies the Lobby
     */

    public void addPlayer(Player p, boolean forced){
        getPlayers().add(p);
        ArrayList<String> names = new ArrayList<>();
        for(Player player:players)
            names.add(player.getName());
        LobbyServer ls = new LobbyServer(names);
        ls.forced = forced;
        notifyObservers(ls);
    }


    /**
     * When a player is removed to the list players
     * on the Initial phase, it notifies the Lobby
     */
    public void removePlayer(String name){
        if(getStatus().equals(Status.NAME_CHOICE)){
            for(int i = 0; i<getPlayers().size(); i++)
                if(getPlayers().get(i).getName().equals(name))
                    getPlayers().remove(getPlayers().get(i));

            ArrayList<String> names = new ArrayList<>();
            for(Player player:players)
                names.add(player.getName());
            notifyObservers(new LobbyServer(names));
        }
    }


    /**
     * Saving the match in file for server persistence purpose
     */
    public void saveToFile(ServerMessage sm) {
        FileOutputStream out;
        ObjectOutputStream objOut;

        if(!new File("resources" +File.separatorChar+"saved-match").exists() && !new File("resources" +File.separatorChar+"saved-match").mkdir())
            return;

        try {

            String path = System.getProperty("user.dir")+File.separatorChar+"resources" +File.separatorChar+"saved-match" + File.separatorChar + String.format("%07d" , this.getId())+".santorini";
            File f = new File(path);
            out = new FileOutputStream(f);
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(this);
            objOut.flush();
            objOut.writeObject(sm);
            objOut.flush();
            objOut.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Re-connect the player to the match
     * @param name
     */
    public void playerReConnection(String name, int type){
        ReConnectionServer rcs = new ReConnectionServer(name, type);
        rcs.board = new ArrayList<>();
        for(Cell c : getBoard().getField())
            rcs.board.add(new SnapCell(c.getRow(),c.getColumn(),c.getLevel()));
        rcs.players = new ArrayList<>();
        rcs.workers = new ArrayList<>();
        SnapPlayer snap;
        for(Player p : getPlayers()){
            snap = new SnapPlayer(p.getName());
            snap.card = p.getCard().getName();
            rcs.players.add(snap);
            rcs.workers.add(new SnapWorker(p.getWorker1().getRow(),p.getWorker1().getColumn(),p.getName(),1));
            rcs.workers.add(new SnapWorker(p.getWorker2().getRow(),p.getWorker2().getColumn(),p.getName(),2));
        }
        rcs.currentPlayer = getCurrentPlayer().getName();
        this.notifyObservers(rcs);
    }

    public void activeEasterEgg(String name){
        HashMap<String, Integer> win = new HashMap<>();
        ObjectInputStream objIn;
        try {
            if(new File("resources"+File.separator+"saved-match").exists()){
                for (final File fileEntry : Objects.requireNonNull(new File("resources"+File.separator+"saved-match").listFiles())) {
                    if (!fileEntry.isDirectory() && getExtensionByStringHandling(fileEntry.getName()).get().equals("santorini")) {
                        objIn = new ObjectInputStream(new FileInputStream(fileEntry.getAbsolutePath()));
                        Object obj = objIn.readObject();
                        if(obj instanceof Match) {
                            if (((Match) obj).getStatus().equals(Status.END)){
                                ((Match) obj).getPlayers().removeAll(((Match) obj).getLosers());
                                if(((Match) obj).getPlayers().size()==1)
                                    if(win.containsKey(((Match) obj).getPlayers().get(0).getName()))
                                        win.put(((Match) obj).getPlayers().get(0).getName(),win.get(((Match) obj).getPlayers().get(0).getName())+1);
                                    else win.put(((Match) obj).getPlayers().get(0).getName(),1);
                            }
                        }
                        objIn.close();
                    }
                }
            }
            this.notifyObservers(new EasterEggServer(name,win));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Ref. https://www.baeldung.com/java-file-extension
     * @param filename the file
     * @return file extension, if exists
     */
    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}

