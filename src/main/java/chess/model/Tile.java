package chess.model;
import chess.model.pieces.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The Tile Class which specifies a single Tile on the Board
 */
public class Tile {


    /**
     * Value of the row of the tile
     */
    public int rowInt;

    /**
     * Value of the column of the tile
     */
    public int colInt;

    public char rowChar;

    public char columnChar;

    private ObjectProperty<Piece> piece = new SimpleObjectProperty<>();




    /**
     * Define a getter for the property itself
     * @return the piece Property
     */
    public ObjectProperty<Piece> pieceProperty() {
        return piece;
    }


    /**
     * Constructor of the Tile class
     * @param i row iteration number
     * @param j column iteration number
     */
    public Tile(int i ,int j){
        rowInt = i;
        colInt = j;
        this.rowChar = (char) (56 - i);
        this.columnChar = (char) (97 + j);
    }

    /**
     * Copy constructor of Tile class
     * @param toClone the tile we want to Clone
     */
    public Tile(Tile toClone){
        rowInt = toClone.rowInt;
        colInt = toClone.colInt;
        Piece clonedPiece = clonePiece(toClone);
        piece = new SimpleObjectProperty<>(clonedPiece);
        rowChar= toClone.rowChar;
        columnChar = toClone.columnChar;
    }

    /**
     * Method To help coping the Piece on current Tile
     * @param toClone the current Tile we want to clone
     * @return the cloned Piece
     */
    private Piece clonePiece(Tile toClone){
        Piece clonedPiece = null;
        if (toClone.getPiece() instanceof Queen){
            clonedPiece = new Queen(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        else if (toClone.getPiece() instanceof King){
            clonedPiece = new King(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        else if (toClone.getPiece() instanceof Pawn){
            clonedPiece = new Pawn(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        else if (toClone.getPiece() instanceof Bishop){
            clonedPiece = new Bishop(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        else if (toClone.getPiece() instanceof Knight){
            clonedPiece = new Knight(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        else if (toClone.getPiece() instanceof Rook){
            clonedPiece = new Rook(toClone.getPiece().getColor());
            clonedPiece.moveCount = toClone.getPiece().moveCount;
        }
        return clonedPiece;
    }

    /**
     * Method to check if a tile is occupied with a piece
     * @return the boolean
     */
    public boolean isOccupied(){
        return piece.getValue() != null;
    }

    /**
     * The getter for the row value
     * @return the row of the tile as int
     */
    public int getRow(){
        return rowInt;
    }

    /**
     * The getter for the column value
     * @return the column of the tile as int
     */
    public int getColumn(){
        return colInt;
    }

    public char getRowChar() {
        return rowChar;
    }

    public char getColumnChar() {
        return columnChar;
    }
    // Define a getter for the property's value
    public final Piece getPiece(){
        return piece.getValue();
    }
    // Define a setter for the property's value
    public final void setPiece(Piece piece){
        this.piece.set(piece);
    }
}
