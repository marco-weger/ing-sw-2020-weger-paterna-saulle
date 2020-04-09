package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.serverMessages.*;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLI implements ViewInterface {

    private static Logger LOGGER = Logger.getLogger("CLI");

    /**
     * Main to start the server
     * @param args usually it takes no args
     */
    // @SuppressWarnings("squid:S106")
    public static void main(String[] args) {
        /*Server server = new Server(1234);
        try {
            // TODO: chose a singular port
            // server.startServer();
        } catch (RemoteException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }*/
    }

    @Override
    public void handleMessage(CheckMoveServer message) {

    }

    @Override
    public void handleMessage(CheckBuildServer message) {

    }

    @Override
    public void handleMessage(CardChosenServer message) {

    }

    @Override
    public void handleMessage(WorkerChosenServer message) {

    }

    @Override
    public void handleMessage(QuestionAbilityServer message) {

    }

    @Override
    public void handleMessage(CurrentStatusServer message) {

    }

    @Override
    public void handleMessage(SomeoneLoseServer message) {

    }

    @Override
    public void handleMessage(AvailableCardServer message) {

    }

    @Override
    public void handleMessage(SomeoneWinServer message) {

    }

    @Override
    public void handleMessage(NameRequestServer message) {

    }

    @Override
    public void handleMessage(OpponentConnection message) {

    }
}
