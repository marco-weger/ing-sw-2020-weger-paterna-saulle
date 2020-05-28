package it.polimi.ingsw.view;

public enum TextFormatting {

    /**
     * Symbol imported for the CLI
     */
    INPUT("► "),

    /**
     * Text colors imported for the CLI
     */
    COLOR_BLACK("\u001B[30m"),
    COLOR_RED("\u001B[31m"),
    COLOR_YELLOW("\u001B[33m"),
    COLOR_CYAN("\u001B[36m"),
    COLOR_BRIGHT_RED("\u001B[91m"),
    COLOR_BRIGHT_GREEN("\u001B[92m"),
    COLOR_BRIGHT_WHITE("\u001B[97m"),


    /**
     * Background colors imported for the CLI
     */
    BACKGROUND_RED("\u001B[41m"),
    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_BLUE("\u001B[44m"),
    BACKGROUND_CYAN("\u001B[46m"),
    BACKGROUND_WHITE("\u001B[47m"),
    BACKGROUND_BRIGHT_RED("\u001B[101m"),
    BACKGROUND_BRIGHT_YELLOW("\u001B[103m"),
    BACKGROUND_BRIGHT_BLUE("\u001B[104m"),
    BACKGROUND_BRIGHT_PURPLE("\u001B[105m"),
    BACKGROUND_BRIGHT_WHITE("\u001B[107m"),


    /**
     * Text Formatting
     */
    BOLD("\u001B[1m"),
    UNDERLINE("\u001B[4m"),
    RESET("\u001B[0m");

    private final String escape;

    //FIXME
    TextFormatting(String escape)
    {
        this.escape = escape;
    }

    //FIXME
    @Override
    public String toString()
    {
        return escape;
    }

    //FIXME
    public static String initialize()
    {
        return TextFormatting.BACKGROUND_BRIGHT_YELLOW.toString() + TextFormatting.COLOR_BRIGHT_RED + TextFormatting.BOLD;
    }


    /**
     * Style for Win Screen
     * @return the Text Formatting Chosen
     */
    public static String winner()
    {
        return TextFormatting.BACKGROUND_GREEN.toString() + TextFormatting.COLOR_BRIGHT_WHITE + TextFormatting.BOLD;
    }


    /**
     * Style for Lose Screen
     * @return the Text Formatting Chosen
     */
    public static String loser()
    {
        return TextFormatting.BACKGROUND_RED.toString() + TextFormatting.COLOR_BRIGHT_WHITE + TextFormatting.BOLD;
    }


    /**
     * Style for Classic Input: Symbol + TextFormatting
     * @return Symbol Escape + The Text Formatting Chosen
     */
    public static String input(){
        return TextFormatting.INPUT + TextFormatting.COLOR_BRIGHT_GREEN.toString();
    }
}
