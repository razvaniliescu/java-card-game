package constants;

public final class Constants {
    private Constants() {

    }
    // Misc
    public static final int BOARD_ROWS = 4;
    public static final int MAX_MANA_PER_ROUND = 10;
    public static final int INITIAL_HERO_HEALTH = 30;
    public static final int MAX_MINIONS_ON_ROW = 5;

    // Board indexes
    public static final int FIRST_ROW_INDEX = 0;
    public static final int SECOND_ROW_INDEX = 1;
    public static final int THIRD_ROW_INDEX = 2;
    public static final int FOURTH_ROW_INDEX = 3;

    // Place card errors
    public static final int NOT_ENOUGH_MANA = 1;
    public static final int TOO_MANY_MINIONS_ON_ROW = 2;

    // Use attack errors
    public static final int CARD_IS_FROZEN = 3;
    public static final int CARD_ALREADY_ATTACKED = 4;
    public static final int ATTACKED_CARD_IS_FRIENDLY = 5;
    public static final int ATTACKED_CARD_IS_NOT_TANK = 6;

    // Use ability errors
    public static final int ATTACKED_CARD_IS_NOT_FRIENDLY = 7;

    // Use hero ability errors
    public static final int HERO_ALREADY_USED_ABILITY = 8;
    public static final int SELECTED_ROW_IS_FRIENDLY = 9;
    public static final int SELECTED_ROW_IS_NOT_FRIENDLY = 10;
}
