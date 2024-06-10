package chess;


import chess.model.Tile;
import javafx.beans.property.SimpleObjectProperty;


/**
 * testHelperClass which contains methods we use in a lot of Tests
 */
public class TestHelper {


    /**
     * method which initials a new BoardTileArray
     * @return BoardTileArray Property
     */
    public SimpleObjectProperty<Tile>[][] createTestBoard(){
        SimpleObjectProperty<Tile>[][] board = new SimpleObjectProperty[8][8];
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {

                SimpleObjectProperty<Tile> tile1;
                tile1 = new SimpleObjectProperty<>(new Tile(i, j));
                board[i][j] = tile1;
            }
        }
        return board;
    }


}
