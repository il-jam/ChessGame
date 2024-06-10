package chess.model;

import java.util.Locale;

/**
 * This class contains constant values needed in the chess app
 */
@SuppressWarnings("PMD.TooManyFields") //we need the constants, because it s more efficient then to calculate thru the board
public class Constants {





    /**
     * This is the 8x8 int array that defines the chess board
     */
    private static final Integer[][] boardLength = new Integer[8][8];

    public static final Integer MOVE_LIMIT_ALL = boardLength.length;

    public static final Integer BOARD_LENGTH = boardLength.length;




    /**
     * Constant for one step on the board
     */
    public static final int MOVE_LIMIT_ONE = 2;




    /**
     * Constant for straight horizontal and vertical AND diagonal offsets
     */
    public  static int[][] KING_AND_QUEEN = {
            {0,1},
            {0,-1},
            {-1,0},
            {1,0},
            {1,1},
            {-1,1},
            {-1,-1},
            {1,-1}
    };

    public  static int[][] KING_SHORT = {
            {0, +2},
            {0,1},
            {0,-1},
            {-1,0},
            {1,0},
            {1,1},
            {-1,1},
            {-1,-1},
            {1,-1}
    };

    public  static int[][] KING_LONG = {
            {0, -2},
            {0,1},
            {0,-1},
            {-1,0},
            {1,0},
            {1,1},
            {-1,1},
            {-1,-1},
            {1,-1}
    };
    public  static int[][] KING_LONG_SHORT = {
            {0, +2},
            {0, -2},
            {0,1},
            {0,-1},
            {-1,0},
            {1,0},
            {1,1},
            {-1,1},
            {-1,-1},
            {1,-1}
    };
    /**
     * Constant for diagonal offsets
     */
    public  static int[][] BISHOP = {
            {1,1},
            {-1,1},
            {-1,-1},
            {1,-1}
    };

    /**
     * Constant for straight horizontal and vertical offsets
     */
    public  static int[][] ROOK = {
            {0,1},
            {0,-1},
            {-1,0},
            {1,0}
    };

    /**
     * Constant for knight offsets
     */
    public  static int[][] KNIGHT = {
            {1,2},
            {-1,2},
            {2,1},
            {-2,1},
            {-2,-1},
            {-1,-2},
            {2,-1},
            {1,-2}
    };


    /**
     * Constant for white pawn offsets
     */
    public static int[][] WHITE_PAWN = {
            //white pawn
            {-1,0},
            //if beating opponent is possible for white pawn
            {-1,1},
            {-1,-1},
    };

    /**
     * Constant for white pawn offsets
     */
    public static int[][] WHITE_PAWN_DOUBLE = {
            //white pawn
            {-1,0},
            //if first move of white pawn
            {-2,0},
            //if beating opponent is possible for white pawn
            {-1,1},
            {-1,-1},
    };

    public static int[][] BLACK_PAWN = {
            //black pawn
            {1,0},
            //if beating opponent is possible for black pawn
            {1,1},
            {1,-1},
    };

    public static int[][] BLACK_PAWN_DOUBLE = {
            //black pawn
            {1,0},
            //if first move black pawn
            {2,0},
            //if beating opponent is possible for black pawn
            {1,1},
            {1,-1},
    };

    public static Locale LANGUAGE = new Locale("en", "EN");

    public static String UNEXPECTED_VALUE = "Unexpected value :";

}
 