package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.network.VirtualView;

public class FactoryCard {

    /**
     * Factory pattern used to manage safely card generation
     * @param cardName name of the card you want to instance
     * @return istance of the card
     */
    public static Card getCard(CardName cardName, VirtualView vv){
        // TODO: add JavaDoc to describe every card
        switch (cardName)
        {
            case APOLLO:
                return new Apollo(CardName.APOLLO,false,false,false,Status.CHOSEN, vv);
            case ARTEMIS:
                return new Artemis(CardName.ARTEMIS, false, false, false, Status.CHOSEN, vv);
            case ATHENA:
                return new Athena(CardName.ATHENA,false,true,false,Status.CHOSEN, vv);
            case ATLAS:
                return new Atlas(CardName.ATLAS,false,false,true, Status.MOVED, vv);
            case DEMETER:
                return new Demeter(CardName.DEMETER,false,false,true, Status.BUILT, vv);
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
