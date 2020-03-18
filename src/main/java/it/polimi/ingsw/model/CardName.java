package it.polimi.ingsw.model;

public enum CardName {

    APPOLLO("", true),
    ARTHEMIS("", true),
    ATHENA("", false),
    ATLAS("", false),
    DEMETER("", false),
    HEPHASTUS("", false),
    MINOTAUR("", true),
    PAN("", true),
    PROMETHEUS("", false);

    private final String description;
    private boolean ability;

    CardName(String description, boolean ability) {
        this.description = description;
        this.ability = ability;
    }

    /*public List<Cell> checkMove(Worker w, Board b, boolean isAtenaActive){
        List<Cell> avaiable = new ArrayList<>();

        switch (this){
            case ARTHEMIS:
                break;
            case APPOLLO:
                break;
            case ATHENA:
                break;
            case ATLAS:
                break;
            case DEMETER:
                break;
            case HEPHASTUS:
                break;
            case MINOTAUR:
                break;
            case PAN:
                break;
            case PROMETHEUS:
                break;
            default:
                return new ArrayList<>();
        }

        return avaiable;
    }

    public List<Cell> checkBuild(Worker w, Board b, Cell denied){
        List<Cell> avaiable = new ArrayList<>();

        switch (this){
            case ARTHEMIS:
                break;
            case APPOLLO:
                break;
            case ATHENA:
                break;
            case ATLAS:
                break;
            case DEMETER:
                break;
            case HEPHASTUS:
                break;
            case MINOTAUR:
                break;
            case PAN:
                break;
            case PROMETHEUS:
                break;
            default:
                return new ArrayList<>();
        }

        return avaiable;*/
    }

