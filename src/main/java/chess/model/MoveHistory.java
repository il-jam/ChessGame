package chess.model;


import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * class which contains The moveHistory
 */
public class MoveHistory {




    public SimpleListProperty<String> moveHistoryList;

    /**
     * Constructor of the MoveHistory
     */
    public MoveHistory(){
        ObservableList<String> observableList = FXCollections.observableArrayList(new ArrayList<String>());
        this.moveHistoryList = new SimpleListProperty<String>(observableList);

    }

    /**
     * method that adds Moves to the MoveHistory
     * @param startTile the StartTile of Move
     * @param endTile the EndTile of Move
     * @param promotionLetter the PromotionLetter of Move
     * @return a SimpleListProperty which contains Strings
     */
    public SimpleListProperty<String> addToMoveHistory(Tile startTile, Tile endTile, String promotionLetter){
        String startTileRow = Character.toString(startTile.getRowChar());
        String startTileColumn = Character.toString(startTile.getColumnChar());
        String endTileRow = Character.toString(endTile.getRowChar());
        String endTileColumn = Character.toString(endTile.getColumnChar());

        String history = startTileColumn + startTileRow + "-" + endTileColumn + endTileRow + promotionLetter;
        moveHistoryList.getValue().add(history);

        return this.moveHistoryList;
    }


    public void setMoveHistoryList(SimpleListProperty<String> moveHistoryList) {
        this.moveHistoryList =  moveHistoryList;
    }

    /**
     * getter for the ListProperty
     * @return current moveHistoryList
     */
    public ListProperty<String> moveHistoryListProperty(){
        return moveHistoryList;
    }

}
