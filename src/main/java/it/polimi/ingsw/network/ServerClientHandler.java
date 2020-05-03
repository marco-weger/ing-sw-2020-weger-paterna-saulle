package it.polimi.ingsw.network;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.commons.Status;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerClientHandler implements Runnable {

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
    private final int pingPeriod;

    /**
     * Timer task to send ping to the client
     */
    private Timer ping;

    /**
     * This value is changed by turn timers, it is used to stop socket reading.
     */
    private final boolean turnTimesUp;

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

    public void setTurnTimesUp(boolean stillConnected){ this.stillConnected = stillConnected; }

    public ObjectInputStream getIn(){ return in;}

    public void setStillConnected(boolean stillConnected){ this.stillConnected = stillConnected; }

    public Server getServer(){ return server; }

    public VirtualView getVirtualView(){ return virtualView; }

    public Timer getPing(){ return ping; }

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
        } catch (IOException e) {
            System.err.println("XX - "+e.getMessage());
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
                try {
                    ClientMessage object;
                    // standard loop to read
                    do{
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
                                        // this part set up new match
                                        if(!server.getCurrentVirtualView2().getCurrentStatus().equals(Status.NAME_CHOICE)){
                                            server.newCurrentVirtualView2();
                                        }
                                        virtualView = server.getCurrentVirtualView2();
                                        virtualView.notify(object);
                                    }
                                    else if(((ModeChoseClient) object).mode == 3){
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
                        } else virtualView.notify(new DisconnectionClient(this.name,true));
                    }while(socket.isConnected() && object != null);

                    timeOut();
                } catch (Exception e) {
                    if(!virtualView.getCurrentStatus().equals(Status.END)) // && im not a loser
                    {
                        timeOut();
                        //System.out.println("[MANAGE DISCONNECTION......]");
                        // DEFAULT: start timer and wait for reconnection
                        // TODO: start the timer and notify all the client with the start... timers will run asynch, the main one is server
                    }
                }
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
            } catch (Exception e) {
                disconnectionHandler();
                return -1; // thread terminate
            } finally {
                if(object instanceof ConnectionClient){
                    if(((ConnectionClient) object).name.length() <= 12) {
                        ConnectionClient cc = (ConnectionClient) object;

                        if(ret == 0){
                            for (VirtualView vv : server.getVirtualViews2()){
                                if(vv.getConnectedPlayers().containsKey(cc.name)){
                                    System.out.println("FIND!");
                                    ServerClientHandler sch = vv.getConnectedPlayers().get(cc.name);
                                    if(sch.isStillConnected()){
                                        ret = -1;
                                        break;
                                    } else if(!vv.getLosers().contains(cc.name) && !vv.isEnded()){

                                        // TODO !!!

                                        ret = 1;
                                        /*
                                        try{
                                            sch.disconnectionHandler();
                                        } catch (Exception e){ System.err.println(e.getMessage()); }

                                         */
                                        System.out.println("[RECONNECTION USER] - " + this.getName());

                                        //vv.getConnectedPlayers().put(cc.name, this);

                                        ArrayList<String> p = new ArrayList<>(vv.getConnectedPlayers().keySet());
                                        this.notify(new LobbyServer(p));
                                        vv.notify(new ReConnectionClient(cc.name));

                                        this.name = cc.name;
                                    }
                                    /*
                                    if(!sch.isStillConnected() && ){

                                        server.getCurrentVirtualView2().getConnectedPlayers().put(cc.name, this);
                                        ArrayList<String> p = new ArrayList<>();
                                        for(String n : server.getCurrentVirtualView2().getConnectedPlayers().keySet())
                                            p.add(n);
                                        this.notify(new LobbyServer(p));
                                        server.getCurrentVirtualView2().notify(new ReConnectionClient(cc.name));

                                        this.name = cc.name;
                                    }
                                    */
                                }
                            }
                        }
                        /*
                        if(ret == 0){
                            for (VirtualView vv : server.getVirtualViews2()){
                                ret = checkVirtualView(cc, vv);
                                if (ret != 0) break;
                            }
                        }*/
                        // check on current vv (you cant be disconnected in the current lobby)
                        if(ret == 0)
                            if (server.getCurrentVirtualView2().getConnectedPlayers().containsKey(cc.name))
                                ret = -1;
                        if(ret == 0)
                            if (server.getCurrentVirtualView3().getConnectedPlayers().containsKey(cc.name))
                                ret = -1;
                        // check on floating players
                        if(ret == 0)
                            if (server.getPendingPlayers().contains(cc.name))
                                ret = -1;
                    } else ret = -1;
                } else ret = -1;
            }
        }while(ret == -1); // loop until the name is invalid

        this.name = ((ConnectionClient) object).name;
        if(ret==0) server.getPendingPlayers().add(this.name);
        System.out.println("--->"+ret);
        return ret;
    }

    /*
     * It iterates over an the ArrayList to find if tmpName appear in one
     * @param cc ClientMessage received
     * @param vv the VirtualView
     * @return 1 if you need to load the lobby, 0 username is ok, -1 if duplicate
     */
    /*
    private int checkVirtualView(ConnectionClient cc, VirtualView vv) {
        if (vv.getConnectedPlayers().containsKey(cc.name)) {
            if (vv.getConnectedPlayers().get(cc.name) == null && !vv.getLosers().contains(cc.name)) {
                System.out.println("MUST LOAD THE MATCH...");
                virtualView = vv;
                return 1;
            } else { // its a duplicate or a loser
                return -1;
            }
        }
        return 0;
    }
    */

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
            } catch (Exception e) {
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
        stillConnected = false;
        virtualView.getConnectedPlayers().put(this.name,null);
        virtualView.notify(new DisconnectionClient(this.name,true));
        server.getPendingPlayers().remove(this.name);
        if(ping != null)
            ping.cancel();
        try{
            if(socket != null)
            {
                if(!socket.isClosed())
                {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                    System.out.println("[DISCONNECTED USER] - " + this.getName());
                }
            }
        } catch (Exception ex) {
            System.err.println("[MUST HAND] - "+ex.getMessage());
        }
    }

    /**
     * This method is used to write message to client
     * @param message it's an instance of a ServerMessage
     */
    protected void notify(ServerMessage message) {
        // spectator mode
        if(message instanceof SomeoneLoseServer)
            if(this.name.equals(((SomeoneLoseServer) message).player))
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
        } catch (Exception e) {
            System.err.println("[WRITE] - " + message.toString() + " - " + e.getMessage());
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
                    /*
                    System.out.println("......"+ex.getMessage());
                    //socket.close();

                    //Thread.currentThread().interrupt();
                    obj = null;
                    //return null;
                     */
                }
            }
        }while ((obj instanceof PingClient || obj == null) && !turnTimesUp);
        //}while ((obj instanceof PingClient || obj == null) && !turnTimesUp);

        if(turnTimesUp){
            System.out.println(this.name+"'S TURN TIME'S UP!");
            return null;
        } else return obj;
    }

    public void timeOut(){
        if(virtualView == null){
            disconnectionHandler();
        }
        if(virtualView.getCurrentStatus().equals(Status.NAME_CHOICE)){
            virtualView.getConnectedPlayers().remove(this.name);
            virtualView.notify(new DisconnectionClient(this.name,true));
        }
        if(timerDisconnection == null || !timerDisconnection.alive){
            int reconnectionPeriod = 5;
            ScheduledExecutorService timeOut = Executors.newSingleThreadScheduledExecutor();
            timerDisconnection = new TimerDisconnection(this,timeOut,reconnectionPeriod);
            timeOut.scheduleAtFixedRate(timerDisconnection, 0, reconnectionPeriod*1000, TimeUnit.MILLISECONDS);
        }
    }
}
