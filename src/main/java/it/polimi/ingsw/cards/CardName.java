package it.polimi.ingsw.cards;

public enum CardName {

    APOLLO("Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated!"),
    ARTEMIS(""),
    ATHENA(""),
    ATLAS(""),
    DEMETER("Your Worker may build one additional time, but not on the same space!"),
    HEPHASTUS(""),
    MINOTAUR("Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level!"),
    PAN(""),
    PROMETHEUS("");

    private final String description;

    CardName(String description) {
        this.description = description;
    }

}

