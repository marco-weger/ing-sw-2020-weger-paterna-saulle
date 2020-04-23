package it.polimi.ingsw.network;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.clientMessages.ModeChoseClient;
import it.polimi.ingsw.commons.clientMessages.ReConnectionClient;
import it.polimi.ingsw.commons.serverMessages.LobbyServer;
import it.polimi.ingsw.commons.serverMessages.ModeRequestServer;
import it.polimi.ingsw.commons.serverMessages.NameRequestServer;
import it.polimi.ingsw.commons.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ServerClientHandler implements Runnable {

    /**
     * The socket
     */
    private Socket socket;

    /**
     * The main server
     */
    private Server server;

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

    public ServerClientHandler (Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        this.virtualView = null;
        this.name = socket.getRemoteSocketAddress().toString();
    }

    public String getName() {return name;}

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
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            // the "if" manage name setup, it's a singular part of the game because it's necessary to guarantee unique names
            if(socket.isConnected()){
                if(questionName()){
                    loadGame();
                } else questionMode();
            }

            try {
                Object object;
                // standard loop to read
                while(socket.isConnected()){
                    object = in.readObject();
                    if(object instanceof ClientMessage)
                        System.out.println("[RECEIVED] - " + object.toString().substring(object.toString().lastIndexOf('.')+1,
                                object.toString().lastIndexOf('@')) + " - " + (((ClientMessage) object).name.equals("") ? "ALL" : ((ClientMessage) object).name));

                    if(virtualView != null && object != null)
                        virtualView.notify((ClientMessage) object);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[DISCONNECTED USER] - " + socket.getRemoteSocketAddress().toString());
                if(!virtualView.getCurrentStatus().equals(Status.END)) // && im not a loser
                {
                    System.out.println("[MANAGE DISCONNECTION......]");
                    // DEFAULT: start timer and wait for reconnection
                    // TODO: start the timer and notify all the client with the start... timers will run asynch, the main one is server
                }
            }
        }
    }

    /**
     * It asks client the username and then it checks for persistence
     * @return true if you need to load the lobby
     */
    public boolean questionName(){
        Object object = null;
        String tmpName = "FIRST";
        boolean load = false;
        do{
            // send to client request for name an wait for answer
            this.notify(new NameRequestServer(tmpName.equals("FIRST")));
            try {
                object = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                if(object instanceof ConnectionClient){
                    if(((ConnectionClient) object).name.length() <= 12) {
                        // complete the message with ip address and ServerClientHandler
                        ConnectionClient cc = (ConnectionClient) object;
                        tmpName = cc.name;

                        // check if a player with same name exists
                        for (VirtualView vv : server.getVirtualViews2()) {
                            if (vv.getConnectedPlayers().containsKey(cc.name)) {
                                if (vv.getConnectedPlayers().get(cc.name) == null) {
                                    // TODO if you are a loser you go watching
                                    System.out.println("MUST LOAD THE MATCH...");
                                    load = true;
                                    virtualView = vv;
                                } else { // its a duplicate
                                    tmpName = "";
                                    break;
                                }
                            }
                        }
                        for (VirtualView vv : server.getVirtualViews3()) {
                            if (vv.getConnectedPlayers().containsKey(cc.name)) {
                                if (vv.getConnectedPlayers().get(cc.name) == null) {
                                    // TODO if you are a loser you go watching
                                    System.out.println("MUST LOAD THE MATCH...");
                                    load = true;
                                    virtualView = vv;
                                } else { // its a duplicate
                                    tmpName = "";
                                    break;
                                }
                            }
                        }
                        // check on current vv (you cant be disconnected in the current lobby)
                        if (server.getCurrentVirtualView2().getConnectedPlayers().containsKey(cc.name)) {
                            tmpName = "";
                        }
                        if (server.getCurrentVirtualView3().getConnectedPlayers().containsKey(cc.name)) {
                            tmpName = "";
                        }
                        // check on floating players
                        if (server.getPendingPlayers().contains(cc.name)) {
                            tmpName = "";
                        }
                    } else tmpName = "";
                } else tmpName = "";
            }
        }while(tmpName.isEmpty()); // loop until the name is invalid

        server.getPendingPlayers().add(tmpName);
        this.name=tmpName;
        return load;
    }

    /**
     * It asks to client for 2 or 3 players match
     */
    public void questionMode(){
        Object object = null;
        do{
            //mode request
            this.notify(new ModeRequestServer());
            try {
                object = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                server.getPendingPlayers().remove(this.name);
                System.out.println(e.getMessage());
                return;
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
        }while(!(object instanceof ModeChoseClient));
        virtualView.notify((ClientMessage) object);
    }

    public void loadGame(){
        ClientMessage object = new ReConnectionClient(this.name,this);
        ArrayList<String> names = new ArrayList<>();
        //for(String name : virtualView.getConnectedPlayers().keySet())
        //    names.add(name);
        virtualView.getConnectedPlayers().keySet().addAll(Collections.singleton(name));
        LobbyServer ls = new LobbyServer(names);
        ls.loaded = true;
        this.notify(ls);
        virtualView.notify(object);
    }

    /**
     * This method is used to write message to client
     * @param message it's an instance of a ServerMessage
     */
    protected void notify(ServerMessage message) {
        try{
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
