package chess.model;

/**
 * GamesState Class which contains all the GameStates
 */
public enum GameState {

    BLACK_IN_CHECK, WHITE_IN_CHECK, BLACK_MOVE_OUT_OF_CHECK, WHITE_MOVE_OUT_OF_CHECK, GAME_RUNNING, BLACK_WINS, WHITE_WINS, STALEMATE;

    private static GameState currentState;

    /**
     * method which sets The current GameState
     * @param state the GameState we would like to be set
     */
    public static void setGameState(GameState state){
        currentState = state;
    }

    /**
     * method which gets the current GameState
     * @return the current GameState
     */
    public static GameState getCurrentGameState(){
        return currentState;
    }

}
