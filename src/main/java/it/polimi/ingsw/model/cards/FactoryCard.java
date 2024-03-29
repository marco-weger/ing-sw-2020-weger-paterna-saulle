package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.network.VirtualView;

public class FactoryCard {


    /**
     * Factory pattern used to manage safely card generation
     * @param cardName name of the card you want to instance
     * @param vv the client's virtual view
     * @return istance of the card
     */
    public static Card getCard(CardName cardName, VirtualView vv){
        switch (cardName)
        {
            case APOLLO:
                return new Apollo(CardName.APOLLO,false,false,false,Status.CHOSEN, vv);
            case ARTEMIS:
                return new Artemis(CardName.ARTEMIS, false, false, true, Status.MOVED, vv);
            case ATHENA:
                return new Athena(CardName.ATHENA,false,true,false,Status.CHOSEN, vv);
            case ATLAS:
                return new Atlas(CardName.ATLAS,false,false,true, Status.MOVED, vv);
            case DEMETER:
                return new Demeter(CardName.DEMETER,false,false,true, Status.MOVED, vv);
            case HEPHAESTUS:
                return new Hephaestus(CardName.HEPHAESTUS,false,false,true, Status.MOVED, vv);
            case MINOTAUR:
                return new Minotaur(CardName.MINOTAUR,false,false,false, Status.CHOSEN, vv);
            case PAN:
                return new Pan(CardName.PAN,false,false,false, Status.MOVED, vv);
            case PROMETHEUS:
                return new Prometheus(CardName.PROMETHEUS,false,false,true, Status.CHOSEN, vv);
            default:
                return new Card(cardName,false,false,false,Status.START, vv);
        }
    }
}
