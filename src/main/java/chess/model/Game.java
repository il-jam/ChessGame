package chess.model;


/**
 * The class Game, where the main game information are saved and handled
 */
public class Game {

    private final MoveHistory moveHistory;

    /**
     * the Board is needed to access further game logic
     */
    private final Board board;

    public boolean german = false;

    private ChessColor currentPlayer;

    private boolean aIGame = false;

    private boolean networkGame = false;

    private ChessColor networkColor = ChessColor.Black;

    private ChessColor aIColor = ChessColor.Black;

    private String sendToNetworkString;

    private String ip;



    /**
     * The constructor of the Game
     */
    public Game() {
        this.board = new Board();
        currentPlayer = ChessColor.White;
        GameState.setGameState(GameState.GAME_RUNNING);
        this.moveHistory = new MoveHistory();
        board.setMoveHistory(moveHistory);

    }


    /**
     * method which initializes the next turn
     * and sets the current GameState
     */
    public void nextTurn() {
        Board clonedBoard = new Board(board);
        CheckHandler checkHandler = new CheckHandler(clonedBoard);
        if (board.validSemanticMove) {
            currentPlayer = (currentPlayer == ChessColor.White) ? ChessColor.Black : ChessColor.White;
            if (checkHandler.isCheck(ChessColor.Black)) {
                GameState.setGameState(GameState.BLACK_IN_CHECK);
            } else if (checkHandler.isCheck(ChessColor.White)) {
                GameState.setGameState(GameState.WHITE_IN_CHECK);
            }
            isFinished();

        } else {
            System.out.println("!Move not allowed");
        }
    }


    /**
     * Method which checks if the Game is over and sets the GameStates
     */
    public void isFinished() {
        if (!gameOverDueToCM()) {
            gameOverDueToSM();
        }
    }

    private boolean gameOverDueToCM() {
        Board clonedBoard = new Board(board);
        CheckHandler checkHandlerOfClonedBoard = new CheckHandler(clonedBoard);
        if (GameState.getCurrentGameState() == GameState.BLACK_IN_CHECK && checkHandlerOfClonedBoard.isCheckMate(ChessColor.Black) && currentPlayer == ChessColor.Black) {
            System.out.println("White Wins!!!");
            GameState.setGameState(GameState.WHITE_WINS);
            return true;
        } else if (GameState.getCurrentGameState() == GameState.WHITE_IN_CHECK && checkHandlerOfClonedBoard.isCheckMate(ChessColor.White) && currentPlayer == ChessColor.White) {
            System.out.println("Black Wins!!!");
            GameState.setGameState(GameState.BLACK_WINS);
            return true;
        }
        return false;
    }

    private void gameOverDueToSM() {
        Board clonedBoard = new Board(board);
        CheckHandler checkHandlerOfClonedBoard = new CheckHandler(clonedBoard);
        if (checkHandlerOfClonedBoard.isStalemate(ChessColor.Black) && currentPlayer == ChessColor.Black) {
            System.out.println("Is Stalemate");
            GameState.setGameState(GameState.STALEMATE);

        } else if (checkHandlerOfClonedBoard.isStalemate(ChessColor.White) && currentPlayer == ChessColor.White) {
            System.out.println("Is Stalemate");
            GameState.setGameState(GameState.STALEMATE);
        }
    }

    /**
     * getter for the Board class
     *
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * getter for the current players color
     *
     * @return currentPlayer
     */
    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ChessColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setAIColor(ChessColor aIColor) {
        this.aIColor = aIColor;
    }

    public ChessColor getAIColor() {
        return aIColor;
    }

    public ChessColor getNetworkColor() {
        return networkColor;
    }

    public void setNetworkColor(ChessColor networkColor) {
        this.networkColor = networkColor;
    }

    /**
     * setter for aIGame
     *
     * @param aIGame boolean
     */
    public void setAIGame(boolean aIGame) {
        this.aIGame = aIGame;
    }

    /**
     * getter vor aIGame
     *
     * @return the boolean of aIGame
     */
    public boolean isAIGame() {
        return aIGame;
    }

    public boolean isNetworkGame() {
        return networkGame;
    }

    public void setNetworkGame(boolean networkGame) {
        this.networkGame = networkGame;
    }

    public String getSendToNetworkString() {
        return sendToNetworkString;
    }

    public void setSendToNetworkString(String sendToNetworkString) {
        this.sendToNetworkString = sendToNetworkString;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }




}


