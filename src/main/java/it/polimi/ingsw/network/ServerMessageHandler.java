package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.serverMessages.*;

public interface ServerMessageHandler extends ServerMessage {

    void handleMessage(CheckMoveServer cms);
    void handleMessage(CheckBuildServer cbs);
    //void handleMessage(CheckMoveServer cms);
    //void handleMessage(CheckMoveServer cms);
}
