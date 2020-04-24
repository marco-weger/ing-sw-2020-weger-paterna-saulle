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
            int go = questionName(); // 1 load - 0 question - -1 error

            // the "if" manage name setup, it's a singular part of the game because it's necessary to guarantee unique names
            if(socket.isConnected()){
                if(go == 1)
                    go = loadGame();
                else if(go == 0)
                    go = questionMode();
            }

            if(go == 1){
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
                } catch (Exception e) {
                    printDisconnection();
                    if(!virtualView.getCurrentStatus().equals(Status.END)) // && im not a loser
                    {
                        System.out.println("[MANAGE DISCONNECTION......]");
                        // DEFAULT: start timer and wait for reconnection
                        // TODO: start the timer and notify all the client with the start... timers will run asynch, the main one is server
                    }
                }
            }
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
            this.notify(new NameRequestServer(ret == 0));
            try {
                object = in.readObject();
                ret = 0;
            } catch (Exception e) {
                printDisconnection();
                return -1; // thread terminate
            } finally {
                if(object instanceof ConnectionClient){
                    if(((ConnectionClient) object).name.length() <= 12) {
                        // complete the message with ip address and ServerClientHandler
                        ConnectionClient cc = (ConnectionClient) object;

                        // check if a player with same name exists
                        if(ret == 0){
                            for (VirtualView vv : server.getVirtualViews2()){
                                ret = checkVirtualView(cc, vv);
                                if (ret != 0) break;
                            }
                        }
                        if(ret == 0){
                            for (VirtualView vv : server.getVirtualViews2()){
                                ret = checkVirtualView(cc, vv);
                                if (ret != 0) break;
                            }
                        }
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
        server.getPendingPlayers().add(this.name);
        return ret;
    }

    /**
     * IT iterates over an the ArrayList to find if tmpName appear in one
     * @param cc ClientMessage received
     * @param vv the VirtualView
     * @return 1 if you need to load the lobby, 0 username is ok, -1 if duplicate
     */
    private int checkVirtualView(ConnectionClient cc, VirtualView vv) {
        if (vv.getConnectedPlayers().containsKey(cc.name)) {
            if (vv.getConnectedPlayers().get(cc.name) == null) {
                // TODO if you are a loser you go watching
                System.out.println("MUST LOAD THE MATCH...");
                virtualView = vv;
                return 1;
            } else { // its a duplicate
                return -1;
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
            this.notify(new ModeRequestServer());
            try {
                object = in.readObject();
            } catch (Exception e) {
                printDisconnection();
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
        }while(!(object instanceof ModeChoseClient));
        virtualView.notify((ClientMessage) object);
        return 1;
    }

    /**
     * It loads a game if necessary
     * @return 1 if ok
     */
    public int loadGame(){
        ClientMessage object = new ReConnectionClient(this.name,this);
        ArrayList<String> names = new ArrayList<>();
        //for(String name : virtualView.getConnectedPlayers().keySet())
        //    names.add(name);
        virtualView.getConnectedPlayers().keySet().addAll(Collections.singleton(name));
        LobbyServer ls = new LobbyServer(names);
        ls.loaded = true;
        this.notify(ls);
        virtualView.notify(object);
        return 1;
    }

    private void printDisconnection(){
        System.out.println("[DISCONNECTED USER] - " + socket.getRemoteSocketAddress().toString());
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
