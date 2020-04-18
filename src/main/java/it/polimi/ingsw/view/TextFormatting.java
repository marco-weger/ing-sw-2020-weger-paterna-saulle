package it.polimi.ingsw.view;

public enum TextFormatting {

    INPUT("► "),
    SEPARATOR("-"),

    COLOR_BLACK("\u001B[30m"),
    COLOR_RED("\u001B[31m"),
    COLOR_GREEN("\u001B[32m"),
    COLOR_YELLOW("\u001B[33m"),
    COLOR_BLUE("\u001B[34m"),
    COLOR_PURPLE("\u001B[35m"),
    COLOR_CYAN("\u001B[36m"),
    COLOR_WHITE("\u001B[37m"),
    COLOR_BRIGHT_BLACK("\u001B[90m"),
    COLOR_BRIGHT_RED("\u001B[91m"),
    COLOR_BRIGHT_GREEN("\u001B[92m"),
    COLOR_BRIGHT_YELLOW("\u001B[93m"),
    COLOR_BRIGHT_BLUE("\u001B[94m"),
    COLOR_BRIGHT_PURPLE("\u001B[95m"),
    COLOR_BRIGHT_CYAN("\u001B[96m"),
    COLOR_BRIGHT_WHITE("\u001B[97m"),

    BACKGROUND_BLACK("\u001B[40m"),
    BACKGROUND_RED("\u001B[41m"),
    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_YELLOW("\u001B[43m"),
    BACKGROUND_BLUE("\u001B[44m"),
    BACKGROUND_PURPLE("\u001B[45m"),
    BACKGROUND_CYAN("\u001B[46m"),
    BACKGROUND_WHITE("\u001B[47m"),
    BACKGROUND_BRIGHT_BLACK("\u001B[100m"),
    BACKGROUND_BRIGHT_RED("\u001B[101m"),
    BACKGROUND_BRIGHT_GREEN("\u001B[102m"),
    BACKGROUND_BRIGHT_YELLOW("\u001B[103m"),
    BACKGROUND_BRIGHT_BLUE("\u001B[104m"),
    BACKGROUND_BRIGHT_PURPLE("\u001B[105m"),
    BACKGROUND_BRIGHT_CYAN("\u001B[10m"),
    BACKGROUND_BRIGHT_WHITE("\u001B[107m"),

    BOLD("\u001B[1m"),
    UNDERLINE("\u001B[4m"),
    INVERSE("\u001B[7m"),
    RESET("\u001B[0m");

    private String escape;

    TextFormatting(String escape)
    {
        this.escape = escape;
    }

    @Override
    public String toString()
    {
        return escape;
    }

    public static String initialize()
    {
        return TextFormatting.BACKGROUND_BRIGHT_YELLOW.toString() + TextFormatting.COLOR_BRIGHT_RED + TextFormatting.BOLD;
    }

    public static String winner()
    {
        return TextFormatting.BACKGROUND_GREEN.toString() + TextFormatting.COLOR_BRIGHT_WHITE + TextFormatting.BOLD;
    }

    public static String loser()
    {
        return TextFormatting.BACKGROUND_RED.toString() + TextFormatting.COLOR_BRIGHT_WHITE + TextFormatting.BOLD;
    }

    public static String input(){
        return TextFormatting.INPUT + TextFormatting.COLOR_BRIGHT_GREEN.toString();
    }
}
