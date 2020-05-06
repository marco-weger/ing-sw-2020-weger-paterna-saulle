package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.commons.servermessages.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable {

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(ServerClientHandler.class.getName());

    /**
     * The socket
     */
    private final Socket socket;

    /**
     * The main server
     */
    private final Server server;

    /**
     * It is used to read object from client
     */
    private ObjectInputStream in;

    /**
     * It is used to write object to client
     */
    private ObjectOutputStream out;

    /**
     * The VirtualView of the match (players in the same match have same instance)
     */
    private VirtualView virtualView;

    /**
     * The name of the player
     */
    private String name;

    /**
     * Socket ping period
     */
    private final long pingPeriod;

    /**
     * Timer task to send ping to the client
     */
    private Timer ping;

    /**
     * This value is changed by turn timers, it is used to stop socket reading.
     */
    private boolean turnTimesUp;

    /**
     * True if the connection is alive
     */
    private boolean stillConnected;

    /**
     * The timer of disconnection handler
     */
    TimerDisconnection timerDisconnection;

    /**
     * @param socket connection
     * @param server the SERVER
     * @param pingPeriod period used to run ping task
     */
    public ServerClientHandler (Socket socket, Server server, int pingPeriod){
        this.socket = socket;
        this.server = server;
        this.virtualView = null;
        this.name = socket.getRemoteSocketAddress().toString();
        this.pingPeriod = pingPeriod;
        this.turnTimesUp = false;

        if(isConnected()){
            stillConnected = true;
        }
    }

    private boolean isConnected(){
        try{
            return socket.isConnected();
        } catch (Exception ignored){ return false; }
    }

    public String getName() {return name;}

    public boolean isStillConnected() {return stillConnected;}

    public void setTurnTimesUp(boolean turnTimesUp){ this.turnTimesUp = turnTimesUp; }

    public ObjectInputStream getIn(){ return in;}

    public void setStillConnected(boolean stillConnected){ this.stillConnected = stillConnected; }

    public Server getServer(){ return server; }

    public VirtualView getVirtualView(){ return virtualView; }

    public Timer getPing(){ return ping; }

    public ObjectInputStream getInputStream(){ return in; }

    public ObjectOutputStream getOutputStream(){ return out; }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * A thread is used to read object from every player. It starts immediately after the connection.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // get streams for socket output and input
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            stillConnected = true;
            out.writeObject(new PingServer(this.name)); // first ping fixes error of first connection of server
            startPing();
        } catch (IOException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        } finally {
            int go = questionName(); // 1 load - 0 question - -1 error

            // the "if" manage name setup, it's a singular part of the game because it's necessary to guarantee unique names
            if(socket.isConnected()){
                if(go == 1)
                    go = loadGame();
                else if(go == 0)
                    go = questionMode();

                if(go == -1)
                    virtualView.notify(new DisconnectionClient(this.name,true));
            }

            if(go == 1){
                ClientMessage object;
                // standard loop to read
                do{
                    try {
                        object = (ClientMessage) readFromClient();
                        if(object != null){
                            System.out.println("[RECEIVED] - " + object.toString().substring(object.toString().lastIndexOf('.')+1,
                                    object.toString().lastIndexOf('@')) + " - " + (object.name.equals("") ? "ALL" : object.name));

                            if(object instanceof ModeChoseClient){
                                System.out.println("XXXX -> "+virtualView.isEnded());
                                System.out.println("XXXX -> "+virtualView.getLosers().contains(this.getName()));
                                if(virtualView.isEnded() || virtualView.getLosers().contains(this.getName())){
                                    ((ModeChoseClient) object).sch = this;
                                    if(((ModeChoseClient) object).mode == 2){
                                        this.turnTimesUp = false;
                                        // this part set up new match
                                        if(!server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE)){
                                            server.newCurrentVirtualView2();
                                        }
                                        virtualView = server.getCurrentVirtualView2();
                                        virtualView.notify(object);
                                    }
                                    else if(((ModeChoseClient) object).mode == 3){
                                        this.turnTimesUp = false;
                                        // this part set up new match
                                        if(!server.getCurrentVirtualView3().getCurrentStatus().equals(Status.NAME_CHOICE)){
                                            server.newCurrentVirtualView3();
                                        }
                                        virtualView = server.getCurrentVirtualView3();
                                        virtualView.notify(object);
                                    }
                                }
                            }
                            else if(virtualView != null)
                                virtualView.notify(object);
                        }
                    } catch (Exception e) {
                        if(virtualView != null && !virtualView.getCurrentStatus().equals(Status.END)) // && im not a loser
                        {
                            timeOut();
                            // DEFAULT: start timer and wait for reconnection
                            // TODO: start the timer and notify all the client with the start... timers will run asynch, the main one is server
                        }
                    }
                }while(stillConnected && !turnTimesUp);
                //System.out.println("ORA HO VERAMENTE PERSO!");
            }
            while(timerDisconnection.alive) timerDisconnection.alive = false;
        }
    }

    /**
     * It asks client the username and then it checks for persistence
     * @return 1 if you need to load the lobby, 0 username is ok, -1 if errors
     */
    public int questionName(){
        Object object = null;
        int ret = 0;
        do{
            // send to client request for name an wait for answer
            if(isConnected())
                this.notify(new NameRequestServer(ret == 0));
            try {
                object = readFromClient();
                ret = 0;
            } catch (Exception ex) {
                LOGGER.log( Level.SEVERE, ex.toString(), ex );
                disconnectionHandler();
                return -1; // thread terminate
            } finally {
                if(object instanceof ConnectionClient){
                    if(((ConnectionClient) object).name.length() <= 12) {
                        ConnectionClient cc = (ConnectionClient) object;

                        // check on current vv (you cant be disconnected in the current lobby)
                        if(server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE) && server.getCurrentVirtualView2().getConnectedPlayers().containsKey(cc.name))
                            ret = -1;
                        else if(server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE) && server.getCurrentVirtualView3().getConnectedPlayers().containsKey(cc.name))
                            ret = -1;
                        // check on floating players
                        else if(server.getPendingPlayers().contains(cc.name))
                            ret = -1;
                        else {
                            for (VirtualView vv : server.getVirtualViews2()){
                                checkVirtualView(vv,cc);
                            }
                            for (VirtualView vv : server.getVirtualViews3()){
                                checkVirtualView(vv,cc);
                            }
                        }
                    } else ret = -1;
                } else ret = -1;
            }
        }while(ret == -1); // loop until the name is invalid

        this.name = ((ConnectionClient) object).name;
        if(ret==0) server.getPendingPlayers().add(this.name);
        System.out.println("---reconnection--->"+ret);
        return ret;
    }

    /**
     * It iterates over an the ArrayList to find if tmpName appear in one
     * @param vv the VirtualView
     * @param cc ClientMessage received
     * @return 1 if you need to load the lobby, 0 username is ok, -1 if duplicate
     */
    private int checkVirtualView(VirtualView vv, ClientMessage cc) {
        if(vv.getConnectedPlayers().containsKey(cc.name)){
            ServerClientHandler sch = vv.getConnectedPlayers().get(cc.name);
            if(!sch.timerDisconnection.alive){
                return -1;
            } else if(!vv.getLosers().contains(cc.name) && !vv.isEnded()){
                this.name = cc.name;
                System.out.println("[RECONNECTION USER] - " + this.getName());
                this.virtualView = vv;
                vv.getConnectedPlayers().put(cc.name, this);
                ArrayList<String> p = new ArrayList<>(vv.getConnectedPlayers().keySet());
                sch.setStillConnected(true);
                this.startPing();
                sch.timerDisconnection.ses.shutdown();
                System.out.println("[0]");
                LobbyServer ls = new LobbyServer(p);
                ls.type = 2;
                this.notify(ls);
                virtualView.notify(new ReConnectionClient(this.name));
                return 1;
            }
        }
        return 0;
    }

    /**
     * It asks to client for 2 or 3 players match
     */
    public int questionMode(){
        Object object = null;
        do{
            //mode request
            if(isConnected())
                this.notify(new ModeRequestServer());
            try {
                object = readFromClient();
            } catch (Exception ex) {
                LOGGER.log( Level.SEVERE, ex.toString(), ex );
                disconnectionHandler();
                server.getPendingPlayers().remove(this.name);
                return -1;
            } finally {
                if(object instanceof ModeChoseClient){
                    ((ModeChoseClient) object).sch = this;
                    if(((ModeChoseClient) object).mode == 2){
                        // this part set up new match
                        if(!server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE)){
                            server.newCurrentVirtualView2();
                        }
                        virtualView = server.getCurrentVirtualView2();
                        server.getPendingPlayers().remove(this.name);
                    }
                    else if(((ModeChoseClient) object).mode == 3){
                        // this part set up new match
                        if(!server.getCurrentVirtualView3().getCurrentStatus().equals(Status.NAME_CHOICE)){
                            server.newCurrentVirtualView3();
                        }
                        virtualView = server.getCurrentVirtualView3();
                        server.getPendingPlayers().remove(this.name);
                    }
                }
            }
        }while(!(object instanceof ModeChoseClient) && !turnTimesUp);
        virtualView.notify((ClientMessage) object);
        if(object != null)
            return 1;
        else return  -1;
    }

    /**
     * It loads a game if necessary
     * @return 1 if ok
     */
    public int loadGame(){
        //ClientMessage object = new ReConnectionClient(this.name,this);
        //ArrayList<String> names = new ArrayList<>();
        //for(String name : virtualView.getConnectedPlayers().keySet())
        //    names.add(name);
        //virtualView.getConnectedPlayers().keySet().addAll(Collections.singleton(name));
        //LobbyServer ls = new LobbyServer(names);
        //ls.loaded = true;
        //System.out.println("DID IT!");
        //if(isConnected())
        //    this.notify(ls);
        //virtualView.notify(object);
        //this.notify(ls);
        return 1;
    }

    /**
     * It starts when this client disconnect
     */
    protected void disconnectionHandler(){
        if(ping != null)
            ping.cancel();
        stillConnected = false;
        virtualView.getConnectedPlayers().put(this.name,null);
        virtualView.notify(new DisconnectionClient(this.name,true));
        server.getPendingPlayers().remove(this.name);
        try{
            if(socket != null && !socket.isClosed())
            {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
                System.out.println("[DISCONNECTED USER] - " + this.getName());
            }
        } catch (Exception ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }
    }

    /**
     * This method is used to write message to client
     * @param message it's an instance of a ServerMessage
     */
    protected void notify(ServerMessage message) {
        // spectator mode
        if(message instanceof SomeoneLoseServer && this.name.equals(((SomeoneLoseServer) message).player))
            virtualView.getLosers().add(this.name);

        if(message instanceof SomeoneWinServer)
            if(!this.name.equals(((SomeoneWinServer) message).player))
                virtualView.getLosers().add(this.name);

        try{
            if(stillConnected && isConnected()){
                //out.reset();
                out.writeObject(message);
                out.flush();
            }
            //else System.err.println("ERRORE GRAVE ... "+message.toString());
        } catch (Exception ex) {
            //LOGGER.log( Level.SEVERE, ex.toString(), ex );
            timeOut();
        }
    }

    /**
     * Ping task
     */
    public void startPing(){
        ping = new Timer();
        ping.scheduleAtFixedRate(new TimerPing(this), 3000, pingPeriod*1000);
    }

    /**
     * @return the read object
     * @throws IOException of socket reading
     * @throws ClassNotFoundException of socket reading
     */
    protected Object readFromClient() throws IOException, ClassNotFoundException {
        Object obj = null;
        do{
            if(in.available() == 0){
                try{
                    obj = in.readObject();
                } catch (SocketTimeoutException ex){
                    timeOut();
                }
            }
        }while ((obj instanceof PingClient || obj == null) && !turnTimesUp);

        if(obj instanceof DisconnectionClient){
            disconnectionHandler();
        }

        if(turnTimesUp){
            System.out.println(this.name+"'S TURN TIME'S UP!");
            return null;
        } else return obj;
    }

    public void timeOut(){
        if(virtualView == null){
            disconnectionHandler();
        } else if(virtualView.getCurrentStatus().equals(Status.NAME_CHOICE)){
            virtualView.getConnectedPlayers().remove(this.name);
            server.getPendingPlayers().remove(this.name);
            virtualView.notify(new DisconnectionClient(this.name,true));
            this.close();
        }
        else if(virtualView.getCurrentStatus().equals(Status.CARD_CHOICE) || virtualView.getCurrentStatus().equals(Status.WORKER_CHOICE) && stillConnected){
            virtualView.getTurn().cancel();
            virtualView.getConnectedPlayers().remove(this.name);
            server.getVirtualViews2().remove(this.getVirtualView());
            server.getVirtualViews3().remove(this.getVirtualView());
            for(ServerClientHandler sch : virtualView.getConnectedPlayers().values())
                sch.reset(virtualView.getConnectedPlayers().size() + 1);
            this.close();
        }
        else if((virtualView.getLosers().contains(this.getName()) || virtualView.isEnded()) && stillConnected){
            this.close();
        } else if(timerDisconnection == null || !timerDisconnection.alive){
            long reconnectionPeriod = 5;
            ScheduledExecutorService timeOut = Executors.newSingleThreadScheduledExecutor();
            timerDisconnection = new TimerDisconnection(this,timeOut,server.getdisconnectTimer()/reconnectionPeriod);
            timeOut.scheduleAtFixedRate(timerDisconnection, 0, reconnectionPeriod*1000, TimeUnit.MILLISECONDS);
        }
    }

    public void timesUp(){
        System.out.println("Arrivo al metodo times up");
        virtualView.notify(new DisconnectionClient(this.name,true));
    }

    public void close(){
        if(ping != null)
            ping.cancel();
        stillConnected = false;
        this.virtualView = null;
        if(timerDisconnection != null){
            timerDisconnection.alive = false;
            timerDisconnection.ses.shutdown();
        }
    }

    public void reset(int numberOfPlayer){
        ModeChoseClient object = new ModeChoseClient(name,numberOfPlayer);
        object.forced = true;
        object.sch = this;
        this.turnTimesUp = false;
        if(numberOfPlayer == 2){
            if(!server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE)){
                server.newCurrentVirtualView2();
            }
            virtualView = server.getCurrentVirtualView2();
        }
        else if(numberOfPlayer == 3){
            if(!server.getCurrentVirtualView3().getCurrentStatus().equals(Status.NAME_CHOICE)){
                server.newCurrentVirtualView3();
            }
            virtualView = server.getCurrentVirtualView3();
        }
        virtualView.notify(object);
    }

}
