package it.polimi.ingsw.model.cards;

public class FactoryCard {

    /**
     * Factory pattern used to manage safely card generation
     * @param cardName name of the card you want to instance
     * @return istance of the card
     */
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
