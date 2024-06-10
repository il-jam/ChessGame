package chess.gui;

import javafx.scene.layout.StackPane;

/**
 * this is a functional interface which injects a function within a second Function
 */
@FunctionalInterface
public interface MatrixVisitor {
    /**
     * method which allows to use the iteratorMatrix function in BoardView
     * @param stackPane the StackPane we visit
     * @param i row id of StackPane
     * @param j column id of StackPane
     */
     void accept(StackPane stackPane, int i, int j);
}