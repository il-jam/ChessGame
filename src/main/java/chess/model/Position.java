package chess.model;

/**
 * Class of the Position with the coordinates on the Chess board, used to calculate possiblePositions of Pieces
 */
public class Position {

    int i;

    int j;

    /**
     * Constructor of Position Class
     * @param i int value which represents the row
     * @param j int value which represents the column
     */
    public Position(int i, int j){
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
