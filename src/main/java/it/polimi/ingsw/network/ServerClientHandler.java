package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.serverMessages.NameRequestServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable  {

    Socket socket;
    Server server;
    ObjectInputStream in;
    ObjectOutputStream out;
    private static Logger LOGGER = Logger.getLogger("ServerClientHandler");


    public ServerClientHandler (Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            Object object = null;

            // username req
            String name = "";
            String ip = socket.getInetAddress().toString();

            // TODO: if ip address has an active match lets go to the match
            while(!(object instanceof ConnectionClient)){
                this.notify(new NameRequestServer());
                object = in.readObject();
                if(object instanceof ConnectionClient){
                    ConnectionClient cc = (ConnectionClient) object;
                    if(cc.name.equals("") || server.isInWaitingRoom(cc.name)){
                        object = null;
                    }
                    else{
                        name = cc.name;
                        cc.ip = socket.getInetAddress().toString();
                        cc.sch = this;
                        server.addPlayer((ConnectionClient) object);
                    }
                }
            }
            while(socket.isConnected()){
                object = in.readObject();
                VirtualView vv = server.getVirtualViews().get(name+ip);
                if(vv != null && object != null)
                    vv.notify((ClientMessage) object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
