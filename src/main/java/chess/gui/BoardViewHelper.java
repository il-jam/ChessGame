package chess.gui;

import chess.model.Tile;
import javafx.collections.ObservableList;

/**
 * Helper Class for the BoardView. it contains a method to parse the Piece chars into File paths and a method for the Pawn promotion
 */
public class BoardViewHelper {

    BoardView boardView;

    String promotionLetter = "";

    /**
     * Constructor of BoardVieHelper
     * @param boardView the BoardView it should Help :)
     */
    public BoardViewHelper(BoardView boardView){
        this.boardView = boardView;
    }

    /**
     * This method translates a char to a path of an image
     * @param pieceChar the char of the piece
     * @return String of the path to the image
     */
    String charToPath(char pieceChar) {
        if (Character.isUpperCase(pieceChar)){
            return charToPathWhite(pieceChar);
        }
        else if (Character.isLowerCase(pieceChar)){
            return charToPathBlack(pieceChar);
        }
        else {
            return null;
        }

    }

    private String charToPathWhite(char pieceChar) {
        switch (pieceChar) {
            case 'P':
                return "/chess/whitePawn.png";
            case 'K':
                return "/chess/whiteKing.png";
            case 'Q':
                return "/chess/whiteQueen.png";
            case 'R':
                return "/chess/whiteRook.png";
            case 'B':
                return "/chess/whiteBishop.png";
            case 'N':
                return "/chess/whiteKnight.png";
        }
        return null;
    }
    private String charToPathBlack(char pieceChar) {
        switch (pieceChar) {
            case 'p':
                return "/chess/blackPawn.png";
            case 'k':
                return "/chess/blackKing.png";
            case 'q':
                return "/chess/blackQueen.png";
            case 'r':
                return "/chess/blackRook.png";
            case 'b':
                return "/chess/blackBishop.png";
            case 'n':
                return "/chess/blackKnight.png";
        }
        return null;
    }
    /**
     * method to set the PromotionLetter on the click of new Pane for pawnPromotionBlack
     * @param startTile StartTile for the move
     * @param secondClickedTile endTile for the move
     */
    public void setPromotionLetterBlack(Tile startTile, Tile secondClickedTile){
        boardView.gameScreen.blackPromotionPane.setVisible(true);
        System.out.println("show black promotion");

        boardView.gameScreen.queenButtonB.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "Q";
            doGuiPromotionBlack(startTile, secondClickedTile);
        });

        boardView.gameScreen.bishopButtonB.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "B";
            doGuiPromotionBlack(startTile, secondClickedTile);
        });

        boardView.gameScreen.knightButtonB.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "N";
            doGuiPromotionBlack(startTile, secondClickedTile);
        });

        boardView.gameScreen.rookButtonB.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "R";
            doGuiPromotionBlack(startTile, secondClickedTile);
        });
    }

    /**
     * method to set the PromotionLetter on the click of new Pane for pawnPromotionWhite
     * @param startTile StartTile for the move
     * @param secondClickedTile endTile for the move
     */
    public void setPromotionLetterWhite(Tile startTile, Tile secondClickedTile){
        boardView.gameScreen.whitePromotionPane.setVisible(true);

        System.out.println("show white Promotion");

        boardView.gameScreen.queenButton.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "Q";
            doGuiPromotionWhite(startTile, secondClickedTile);
        });

        boardView.gameScreen.bishopButton.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "B";
            doGuiPromotionWhite(startTile, secondClickedTile);
        });

        boardView.gameScreen.knightButton.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "N";
            doGuiPromotionWhite(startTile, secondClickedTile);
        });

        boardView.gameScreen.rookButton.setOnMouseClicked(mouseEvent -> {
            promotionLetter = "R";
            doGuiPromotionWhite(startTile, secondClickedTile);
        });
    }

    /**
     * method for making a move and resetting PromotionLetter White and closing the Pane
     * @param startTile StartTile for the move
     * @param secondClickedTile endTile for the move
     */
    private void doGuiPromotionWhite(Tile startTile, Tile secondClickedTile){
        boardView.game.getBoard().movePiece(startTile, secondClickedTile, promotionLetter);
        ObservableList<String> moveHistoryList = boardView.game.getMoveHistory().moveHistoryListProperty().getValue();
        String move = moveHistoryList.get(moveHistoryList.size() - 1);
        boardView.gameScreen.stringToSendToNetworkPartner = move;
        promotionLetter = "";
        boardView.gameScreen.whitePromotionPane.setVisible(false);
    }
    /**
     * method for making a move and resetting PromotionLetter Black and closing the Pane
     * @param startTile StartTile for the move
     * @param secondClickedTile endTile for the move
     */
    private void doGuiPromotionBlack(Tile startTile, Tile secondClickedTile){
        boardView.game.getBoard().movePiece(startTile, secondClickedTile, promotionLetter);
        ObservableList<String> moveHistoryList = boardView.game.getMoveHistory().moveHistoryListProperty().getValue();
        String move = moveHistoryList.get(moveHistoryList.size() - 1);
        boardView.gameScreen.stringToSendToNetworkPartner = move;
        promotionLetter = "";
        boardView.gameScreen.blackPromotionPane.setVisible(false);
    }

}
