package it.polimi.ingsw.cards;

public class FactoryCard {

    public Card getCard(CardName cardName){
        switch (cardName)
        {
            case APOLLO:
                return new Apollo();
            case ARTHEMIS:
            case ATHENA:
            case ATLAS:
            case DEMETER:
            case HEPHASTUS:
            case MINOTAUR:
            case PAN:
            case PROMETHEUS:
            default:
                return null;
        }
    }

}
