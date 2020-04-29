package it.polimi.ingsw.commons;

public enum Status {

    /**
     * This enumeration is used to managed the turn:
     * - NAME_CHOICE lobby
     * - CARD_CHOICE first pahse of the match, the challenger chose 2 or 3 cards and the others select from these
     * - WORKER_CHOICE players select initial position of workers
     * - START initial phase of the turn, check for win/lose condition and ask for worker choice
     * - CHOSEN this state is used only if current card ability needs a question before move
     * - QUESTION_M this state is used for asking move position
     * - CHOSEN this state is used only if current card ability needs a question before build
     * - QUESTION_B this state is used for asking build position
     * - BUILT this state checks for after build ability
     * - END handling of end phase of the Match
     */
    NAME_CHOICE(false),
    CARD_CHOICE(true),
    WORKER_CHOICE(true),
    START(true),
    CHOSEN(false),
    QUESTION_M(false),
    MOVED(false),
    QUESTION_B(false),
    BUILT(false),
    END(false);

    private final boolean notify;

    Status(boolean notify) {
        this.notify = notify;
    }

    public boolean getNotify(){return notify;}

}
