package it.polimi.ingsw.cards;

public class FactoryCard {

    public static Card getCard(CardName cardName){
        switch (cardName)
        {
            case APOLLO:
                return new Apollo();
            case ARTEMIS:
                return new Artemis();
            case ATHENA:
                return new Athena();
            case ATLAS:
                return new Atlas();
            case DEMETER:
                return new Demeter();
            case HEPHASTUS:
                return new Hephaestus();
            case MINOTAUR:
                return new Minotaur();
            case PAN:
                return new Pan();
            case PROMETHEUS:
                return new Prometheus();
            default:
                return null;
        }
    }

}
