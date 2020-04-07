package it.polimi.ingsw.network;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static Logger LOGGER = Logger.getLogger("SERVER");
    private SocketServer socketServer;

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    // @SuppressWarnings("squid:S106")
    public static void main(String[] args) {
        Server server = new Server();
        try {
            // TODO: chose a singular port
            server.start(1234);
        } catch (RemoteException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Method to start socket server
     * @param socketPort socket port
     * @throws RemoteException error of connection
     */
    private void start(int socketPort) throws RemoteException {
        //socketServer.startServer(socketPort);
        //socketServer.start();

        //socketServerProbeHandler.startServer(7777);
        //socketServerProbeHandler.start();
    }

}
