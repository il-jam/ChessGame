package chess.model;

import chess.model.pieces.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import static chess.model.Constants.BOARD_LENGTH;

/**
 * Board class to calculate movement on
 */
public class Board {

    private MoveHistory moveHistory;

    private Tile lastEndTile;

    public boolean validSemanticMove = false;

    private Tile castlingRookTile;

    private Tile castlingRookEndTile;

    private boolean isEnPassant = false;

    private Piece lastMovedPiece;

    public int maxDepth;

    /**
     * The tile array, which represents the chessboard
     */
    private SimpleObjectProperty<Tile>[][] boardTiles;



    private SimpleListProperty<Piece> removedPieces;

    /**
     * getter removedPieces Property
     * @return removedPieces
     */
    public ListProperty<Piece> removedPiecesProperty(){
        return removedPieces;
    }

    /**
     * The constructor of the board
     */
    public Board() {
        boardTiles = new SimpleObjectProperty[8][8];
        initBoard();
        fillBoard();


        ObservableList<Piece> observableList = FXCollections.observableArrayList(new ArrayList<Piece>());
        removedPieces = new SimpleListProperty<Piece>(observableList);


    }

    /**
     * method which adds a Piece to our removedPieces Property
     * @param piece piece we want to add
     */
    private void addRemovedPieceToList(Piece piece){
        removedPieces.add(piece);
    }
    /**
     * Creates a copy of a board. Create new tiles. keep piece references
     * @param toClone the Board we want to copy
     */
    public Board(Board toClone) {
        boardTiles = new SimpleObjectProperty[8][8];
        copyBoard(toClone.boardTiles);

        lastMovedPiece = toClone.lastMovedPiece;

        isEnPassant = toClone.isEnPassant;


    }


    /**
     * This method initialized the 8X8 chess board tiles
     */
    private void initBoard() {

        for(int i=0; i<BOARD_LENGTH; i++) {

            for(int j=0; j<BOARD_LENGTH; j++) {

                SimpleObjectProperty<Tile> tile1;
                tile1 = new SimpleObjectProperty<>(new Tile(i, j));
                boardTiles[i][j] = tile1;
            }
        }
    }

    /**
     * This method copies the given tile array into a new tile array
     * @param tilesToCopy the Tiles we want to copy
     */
    private void copyBoard(SimpleObjectProperty<Tile>[][] tilesToCopy) {

        for(int i=0; i<BOARD_LENGTH; i++) {

            for(int j=0; j<BOARD_LENGTH; j++) {

                boardTiles[i][j] = new SimpleObjectProperty<>(new Tile(tilesToCopy[i][j].getValue()));
            }
        }
    }

    /**
     * Method to move a piece from one tile to another
     * @param startTile Tile which has the piece to move on it
     * @param endTile Tile where the piece should go to
     */
    public void movePieceFromStartToEndTile(Tile startTile, Tile endTile){
        endTile.setPiece(startTile.getPiece());
    }

    /**
     * Method which removes a piece on a specific tile
     * @param removePieceTile That tile
     */
    public void removePiece(Tile removePieceTile){
        removePieceTile.setPiece(null);
    }

    /**
     * the method which first of all, just moves a piece from start to entitle, but also removes the piece from the starttile afterwords so it doesn't copy itself
     * @param startTile the starttile with the piece on it, which should be moved
     * @param endTile the tile where the piece should go
     * @param promotionLetter  the string for pawn promotion
     */

    public void movePiece(Tile startTile, Tile endTile, String promotionLetter){
        //TODO: hab hier mal das endTile als lastEndTile gespeichert, damit wir das speichern können
        lastEndTile = endTile;
        // Piece on the start tile, we want to move
        Piece pieceWeWantToMove = startTile.getPiece();
        // Piece we want to remove from end tile if beaten
        Piece toRemovePiece = endTile.getPiece();
        // VIP set enPassant back to default
        setEnPassant(false);
        // the List of all possible tile our pieceWeWantToMove can reach
        Position startPosition = new Position(startTile.getRow(), startTile.getColumn());
        List<Tile> list = pieceWeWantToMove.possibleTiles(this, startPosition);
        // first we need to confirm that the end tile of our move is in the reach of the pieceWeWantToMove
        if (list.contains(endTile)) {
            // now we ask if the endTile of our move is occupied
            if (!endTile.isOccupied()) {
                // EnPassant only possible if end tile is empty
                if (isEnPassant) {
                    //enPassant is possible, but the pawn just wants to go normal move
                    if(startTile.getColumn() != endTile.getColumn()){
                        Tile enPassantEnemyTile = getEnPassantEnemyTile(startTile);
                        addRemovedPieceToList(enPassantEnemyTile.getPiece());
                        removePiece(enPassantEnemyTile);
                    }
                }
                // castling only possible if end tile is empty as well
                else if (isCastling(pieceWeWantToMove, startTile, endTile)) {
                    // move rook
                    movePieceFromStartToEndTile(castlingRookTile, castlingRookEndTile);
                    removePiece(castlingRookTile);
                }
            }
            else {
                addRemovedPieceToList(endTile.getPiece());
                removePiece(endTile);
            }

            if (isPawnPromotion(startTile, endTile)) {
                // replace the pieceWeWantToMove with the new promoted piece
                Piece promotedPiece = replacePawnToPromote(promotionLetter, pieceWeWantToMove);
                //We change the pawn to the promotion piece already here. And it should work because the board will be drawn not until the method cycle is over.
                removePiece(startTile);
                startTile.setPiece(promotedPiece);
            }

            movePieceHelperMethod(startTile, endTile, pieceWeWantToMove);
            // if player tries a move but is in check
            Board clonedBoard = new Board(this);
            CheckHandler checkHandler = new CheckHandler(clonedBoard);
            if (checkHandler.isCheck(pieceWeWantToMove.getColor())) {
                // revert the move, due to illegal move
                resetMoveAfterCheck(pieceWeWantToMove,toRemovePiece,startTile,endTile);
            }
            else {
                // set the lastMovePiece for EnPassant
                setLastMovedPiece(pieceWeWantToMove);
                // set valid move flag, so we can switch the turn and print the move
                validSemanticMove = true;
                GameState.setGameState(GameState.GAME_RUNNING);
                moveHistory.addToMoveHistory(startTile, endTile, promotionLetter);
            }
        }
        else{
            // not possible move
            validSemanticMove = false;
        }
    }

    private void movePieceHelperMethod(Tile startTile, Tile endTile, Piece pieceWeWantToMove){
        // set the piece for enPassant
        setLastMovedPiece(pieceWeWantToMove);
        // call method for moving pieces
        movePieceFromStartToEndTile(startTile, endTile);
        // remove the piece from start tile to prevent cloning pieceWeWantToMove
        removePiece(startTile);
        // important,
        pieceWeWantToMove.increaseMoveCount();
    }



    /**
     * method to create new piece for Pawn promotion
     * @param promotionLetter the string which influences the outcome of promotion
     * @param pawnToPromote the pawn that gets promoted
     * @return a new Piece default is queen
     */
    private Piece replacePawnToPromote (String promotionLetter, Piece pawnToPromote) {

        if (promotionLetter.equals("")) {
            return new Queen(pawnToPromote.getColor());
        } else {
            switch (promotionLetter) {
                case "Q":
                    return new Queen(pawnToPromote.getColor());
                case "R":
                    return new Rook(pawnToPromote.getColor());
                case "B":
                    return new Bishop(pawnToPromote.getColor());
                case "N":
                    return new Knight(pawnToPromote.getColor());
            }
        }
        return null;
    }

    private boolean isCastling(Piece pieceWeWantToMove, Tile startTile, Tile endTile){

        Tile shortCastlingEndTileForKing = getSpecificTile(startTile.rowInt, startTile.colInt + 2);
        Tile longCastlingEndTileForKing = getSpecificTile(startTile.rowInt, startTile.colInt - 2);


        if(pieceWeWantToMove instanceof King && pieceWeWantToMove.moveCount == 0){
            if (shortCastlingEndTileForKing == endTile && isShortCastling(startTile)){
                return true;

            }
            return longCastlingEndTileForKing == endTile && isLongCastling(startTile);
        }
        return false;
    }

    private boolean isShortCastling(Tile startTile){
        Tile shortCastlingRookTile = getSpecificTile(startTile.rowInt, startTile.colInt + 3);
        if (shortCastlingRookTile.getPiece() instanceof Rook && shortCastlingRookTile.getPiece().moveCount == 0){
            Tile tileBetweenKingAndRook1 = getSpecificTile(startTile.rowInt, startTile.colInt + 1);
            Tile tileBetweenKingAndRook2 = getSpecificTile(startTile.rowInt, startTile.colInt + 2);

            if(!tileBetweenKingAndRook1.isOccupied() && !tileBetweenKingAndRook2.isOccupied()){
                castlingRookTile = shortCastlingRookTile;
                castlingRookEndTile = tileBetweenKingAndRook1;
                return true;
            }
        }
        return false;
    }

    private boolean isLongCastling(Tile startTile){
        Tile longCastlingRookTile = getSpecificTile(startTile.rowInt, startTile.colInt - 4);
        if(longCastlingRookTile.getPiece() instanceof Rook && longCastlingRookTile.getPiece().moveCount == 0){
            Tile tileBetweenKingAndRook1 = getSpecificTile(startTile.rowInt, startTile.colInt - 1);
            Tile tileBetweenKingAndRook2 = getSpecificTile(startTile.rowInt, startTile.colInt - 2);
            Tile tileBetweenKingAndRook3 = getSpecificTile(startTile.rowInt, startTile.colInt - 3);

            if(!tileBetweenKingAndRook1.isOccupied() && !tileBetweenKingAndRook2.isOccupied() && !tileBetweenKingAndRook3.isOccupied()){
                castlingRookTile = longCastlingRookTile;
                castlingRookEndTile = tileBetweenKingAndRook1;
                return true;
            }
        }
        return false;
    }

    /**
     * This method fills/sets the board before initialized tiles with the chess pieces
     */
    public void fillBoard(){

        int i;

        //pawns
        for (i = 0; i < BOARD_LENGTH; i++){
            boardTiles[6][i].getValue().setPiece(new Pawn(ChessColor.White));
            boardTiles[1][i].getValue().setPiece(new Pawn(ChessColor.Black));
        }

        //kings
        boardTiles[7][4].getValue().setPiece(new King(ChessColor.White));
        boardTiles[0][4].getValue().setPiece(new King(ChessColor.Black));

        //queens
        boardTiles[7][3].getValue().setPiece(new Queen(ChessColor.White));
        boardTiles[0][3].getValue().setPiece(new Queen(ChessColor.Black));

        //bishops
        boardTiles[7][2].getValue().setPiece(new Bishop(ChessColor.White));
        boardTiles[7][5].getValue().setPiece(new Bishop(ChessColor.White));
        boardTiles[0][2].getValue().setPiece(new Bishop(ChessColor.Black));
        boardTiles[0][5].getValue().setPiece(new Bishop(ChessColor.Black));

        //knights
        boardTiles[7][1].getValue().setPiece(new Knight(ChessColor.White));
        boardTiles[7][6].getValue().setPiece(new Knight(ChessColor.White));
        boardTiles[0][1].getValue().setPiece(new Knight(ChessColor.Black));
        boardTiles[0][6].getValue().setPiece(new Knight(ChessColor.Black));

        //rooks
        boardTiles[7][0].getValue().setPiece(new Rook(ChessColor.White));
        boardTiles[7][7].getValue().setPiece(new Rook(ChessColor.White));
        boardTiles[0][0].getValue().setPiece(new Rook(ChessColor.Black));
        boardTiles[0][7].getValue().setPiece(new Rook(ChessColor.Black));

    }

    /**
     * Method to get us the tile on which the King is positioned
     * @param color the color of the King
     * @return the Tile with specific King Piece
     */
    public Tile findKingTile(ChessColor color) {
        for (SimpleObjectProperty<Tile>[] tiles : boardTiles){
            for(SimpleObjectProperty<Tile> tile : tiles){
                Piece piece = tile.getValue().getPiece();
                if (piece instanceof King && piece.getColor() == color){
                    return tile.getValue();
                }
            }
        }
        return null;
    }


    private Tile getEnPassantEnemyTile(Tile attackingPawnTile) {

        ChessColor attackColor = attackingPawnTile.getPiece().getColor();
        Tile nextToAttackingPawnTileLeft = getSpecificTile(attackingPawnTile.getRow(), attackingPawnTile.getColumn() - 1);
        Tile nextToAttackingPawnTileRight = getSpecificTile(attackingPawnTile.getRow(), attackingPawnTile.getColumn() + 1);

        if (getNextToAttackingPawnTileRight(nextToAttackingPawnTileRight,attackColor)){
            return nextToAttackingPawnTileRight;
        }
        else if (getNextToAttackingPawnTileLeft(nextToAttackingPawnTileLeft,attackColor)){
            return nextToAttackingPawnTileLeft;
        }
        else{
            return null;
        }

    }

    private boolean getNextToAttackingPawnTileRight(Tile nextToAttackingPawnTileRight, ChessColor attackColor){
        //nextToAttackingPawnTileLeft != null &&
        if (nextToAttackingPawnTileRight != null){
            Piece victimPieceRight = nextToAttackingPawnTileRight.getPiece();
            if (victimPieceRight != null){
                ChessColor victimColorRight = victimPieceRight.getColor();
                return victimPieceRight == lastMovedPiece && victimColorRight != attackColor && victimPieceRight.moveCount == 1;
            }
        }
        return false;
    }
    private boolean getNextToAttackingPawnTileLeft(Tile nextToAttackingPawnTileLeft, ChessColor attackColor){
        if (nextToAttackingPawnTileLeft != null){
            Piece victimPieceLeft = nextToAttackingPawnTileLeft.getPiece();
            if(victimPieceLeft != null){
                ChessColor victimColorLeft = victimPieceLeft.getColor();
                return victimPieceLeft == lastMovedPiece && victimColorLeft != attackColor && victimPieceLeft.moveCount == 1;
            }
        }
        return false;
    }

    private void resetMoveAfterCheck(Piece pieceWeWantToMove, Piece toRemovePiece, Tile startTile, Tile endTile){
        startTile.setPiece(pieceWeWantToMove);
        endTile.setPiece(toRemovePiece);
        pieceWeWantToMove.decreaseMoveCount();
        // not a allowed move
        validSemanticMove = false;
        if (pieceWeWantToMove.getColor().equals(ChessColor.White)){
            GameState.setGameState(GameState.WHITE_MOVE_OUT_OF_CHECK);
        }
        else{
            GameState.setGameState(GameState.BLACK_MOVE_OUT_OF_CHECK);
        }
        // otherwise could provoke array out of bounce
        if (getRemovedPieces().size() > 0 && getRemovedPieces().contains(toRemovePiece)){
            // last Piece/element of list
            getRemovedPieces().remove(getRemovedPieces().size()-1);
        }
    }
    /**
     * getter for the the Tile class
     * @return tile
     */
    public SimpleObjectProperty<Tile>[][] getTiles() {
        SimpleObjectProperty<Tile>[][] t;
        t = boardTiles;
        return t;
    }

    /**
     * Method that returns a specific tile for the movePiece method
     * @param rowInt row number
     * @param colInt column number
     * @return tile that is used to define the start or end tile of a move in movePiece
     */
    public Tile getSpecificTile(int rowInt, int colInt){
        if(rowInt >= 0 && rowInt < BOARD_LENGTH && colInt >=0 && colInt < BOARD_LENGTH){
            return boardTiles[rowInt][colInt].getValue();
        }
        return null;
    }

    public void setBoardTiles(SimpleObjectProperty<Tile>[][] boardTiles) {
        SimpleObjectProperty<Tile>[][] cloneOfBoard = boardTiles;
        this.boardTiles = cloneOfBoard;

    }


    public List<Piece> getRemovedPieces() {
        return removedPieces.getValue();
    }

    public void setRemovedPieces(SimpleListProperty<Piece> removedPieces) {
        this.removedPieces.set(removedPieces.getValue());
    }

    public void setLastMovedPiece(Piece lastMovedPiece) {
        this.lastMovedPiece = lastMovedPiece;
    }

    public Piece getLastMovedPiece() {
        return lastMovedPiece;
    }


    boolean isPawnPromotion(Tile startTile, Tile endTile){
        Piece pawnToPromote = startTile.getPiece();
        return pawnToPromote instanceof Pawn && endTile.rowInt == 0 || pawnToPromote instanceof Pawn && endTile.rowInt == 7;
    }



    /**
     * setter for is enPassant flag
     * @param enPassant the boolean for flag
     */
    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
    }

    public Tile getLastEndTile() {
        return lastEndTile;
    }

    public void setMoveHistory(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

}
