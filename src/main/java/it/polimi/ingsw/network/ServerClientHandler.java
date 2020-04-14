package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.serverMessages.NameRequestServer;
import it.polimi.ingsw.model.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable  {

    private static Logger LOGGER = Logger.getLogger("ServerClientHandler");

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

    @Deprecated
    public ServerClientHandler (Socket socket, Server server, VirtualView virtualView){
        this.socket = socket;
        this.server = server;
        this.virtualView = virtualView;
        this.name = socket.getRemoteSocketAddress().toString();
    }

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
        try {
            // get streams for socket output and input
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Object object;
            // TODO: if ip address has an active match lets go to the match

            // the "if" manage name setup, it's a singular part of the game because it's necessary to guarantee unique names
            if(socket.isConnected()){
                String tmpName = "FIRST";

                do{
                    // send to client request for name an wait for answer
                    this.notify(new NameRequestServer(tmpName.equals("FIRST")));
                    object = in.readObject();

                    if(object instanceof ConnectionClient){
                        // complete the message with ip address and ServerClientHandler
                        ConnectionClient cc = (ConnectionClient) object;
                        tmpName = cc.name;
                        cc.ip = socket.getRemoteSocketAddress().toString();
                        cc.sch = this;

                        // check if a player with same name exists
                        for(VirtualView vv : server.getVirtualViews()){
                            for(ServerClientHandler sch : vv.getConnectedPlayers()){
                                if(sch.getName().equals(cc.name)){
                                    // TODO check for existent match or disconnected... reconnect!
                                    tmpName = "";
                                    break;
                                }
                            }
                        }
                    } else tmpName = "";
                }while(tmpName.isEmpty()); // loop until the name is invalid

                // username validated ... TODO check for persistence
                this.name=tmpName;

                // this part set up new match
                if(!server.getCurrentVirtualView().getCurrentStatus().equals(Status.NAME_CHOICE)){
                    server.newCurrentVirtualView();
                }
                virtualView = server.getCurrentVirtualView();
                virtualView.notify((ClientMessage) object);
            }

            // standard cycle to read
            while(socket.isConnected()){
                object = in.readObject();

                if(virtualView != null && object != null)
                    virtualView.notify((ClientMessage) object);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            // TODO disconnection event
        }
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
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
