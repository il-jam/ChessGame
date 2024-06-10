package chess.gui;

import chess.model.*;
import chess.model.pieces.Pawn;
import chess.model.pieces.Piece;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this Class contains the elements for the chess Board window and functions for it within the Gamescreen
 */
public class BoardView {

    private final SimpleObjectProperty<Tile>[][] tileProperties = new SimpleObjectProperty[8][8];
    private final List<StackPane> possibleStackPaneTiles = new ArrayList<>();
    private boolean piecePicked = false;
    private int lastClickedPaneRow;
    private int lastClickedPaneCol;
    public GameScreen gameScreen;
    public GridPane gridPane;
    public Game game;
    private StackPane stackPaneForDeletingImageOnSourcePane;
    public BoardViewHelper boardViewHelper = new BoardViewHelper(this);

    /**
     * Initializes the tile properties and connects them with the tile properties form the logic
     */
    public void initTileProperties(){
        SimpleObjectProperty<Tile>[][] logicTiles = game.getBoard().getTiles();
        for (int i = 0; i <8; i++){
            for (int j = 0; j < 8; j++ ){
                tileProperties[i][j] = new SimpleObjectProperty<>();
                tileProperties[i][j].bindBidirectional(logicTiles[i][j]);
                int finalI = i;
                int finalJ = j;

                //int finalI1 = i;
                tileProperties[i][j].getValue().pieceProperty().addListener(new ChangeListener<Piece>() {
                    @Override
                    public void changed(ObservableValue<? extends Piece> observableValue, Piece piece, Piece t1) {
                        // neuer Wert = null -> delete methode für das aktuelle Tile
                        // neuer Wert != null -> printmethode

                        if(piece == null){
                            deleteImageOnSourcePane();
                        }
                        else{
                            deleteImageEndPane(finalI, finalJ);
                            deleteImageOnSourcePane();
                        }
                        setSpecificImage(finalI, finalJ, t1);

                    }
                });
            }
        }
    }

    /**
     * This methods draws an image on a specifiec Stackpane
     * @param i the row of the stackpane
     * @param j the column of the stackpane
     * @param t1 the piece that is printed
     */
    private void setSpecificImage(int i, int j, Piece t1){
        String index = "#" + i + "" + j;
        StackPane stackPane = (StackPane) gridPane.lookup(index);

        //wenn t1 ungleich null ist, dann wurde ein piece auf ein tile gesettet
        if(t1 != null){
            char pieceChar = t1.getConsoleChar(t1.getColor());
            String path = boardViewHelper.charToPath(pieceChar);

            stackPane.getChildren().add(new ImageView(new Image(String.valueOf(getClass().getResource(path)))));
        }
    }


    /**
     * this method deletes the image of the occupied piece on the board
     * @param i row of the stackpane
     * @param j column of the stackpane
     */
    private void deleteImageEndPane(int i, int j) {
        String index = "#" + i + "" + j;
        StackPane stackPane = (StackPane) gridPane.lookup(index);
        ObservableList observableList = stackPane.getChildren();
        observableList.clear();
    }


    /**
     * This methods delets the image of the moved piece on the startTile
     */
    private void deleteImageOnSourcePane(){
        ObservableList observableList = stackPaneForDeletingImageOnSourcePane.getChildren();
        observableList.clear();
    }

    /**
     * This method initializes the listeners on the stackpanes on the chessboard
     */
    public void initStackpaneListeners(){

        iterateMatrix( (stackPane, i, j)->
                {
                    stackPane.setOnMouseClicked(event -> {

                        Piece p = game.getBoard().getSpecificTile(i, j).getPiece();
                        if (!piecePicked) {
                            // currently no Piece is picked
                            if (p != null && game.getCurrentPlayer() == p.getColor()) {
                                // Piece exists and right color
                                showPossibleTilesOnFirstClick(i, j);
                                piecePicked = true;
                            }
                            lastClickedPaneRow = i;
                            lastClickedPaneCol = j;
                        } else {
                            // Ein Piece wurde im vorherigen Klick ausgewählt
                            if (possibleStackPaneTiles.contains(stackPane)) {
                                deShowPanes();


                                // Der aktuelle Klick bezieht sich auf ein possible Tile - es wird gemovet
                                try {
                                    movePieceInGui(i, j);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                piecePicked = false;
                                if (game.isAIGame() && GameState.getCurrentGameState() != GameState.WHITE_MOVE_OUT_OF_CHECK && GameState.getCurrentGameState() != GameState.BLACK_MOVE_OUT_OF_CHECK) {
                                    gameScreen.doAITurn();
                                }
                                if (game.isNetworkGame()&& GameState.getCurrentGameState() != GameState.WHITE_MOVE_OUT_OF_CHECK && GameState.getCurrentGameState() != GameState.BLACK_MOVE_OUT_OF_CHECK) {
                                    try {
                                        gameScreen.doNetworkTurn();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                deShowPanes();
                            }
                            else if (p != null && game.getCurrentPlayer() == p.getColor() && gameScreen.changeSelection.isSelected()) {
                                deShowPanes();

                                possibleStackPaneTiles.clear();

                                // Der aktuelle Klick bezieht sich auf ein Tile, auf welchem ein Piece der aktuellen Farbe steht - Possible Tiles werden gecoloret
                                showPossibleTilesOnFirstClick(i, j);
                                piecePicked = true;
                                lastClickedPaneRow = i;
                                lastClickedPaneCol = j;
                            }

                            else {
                                if(gameScreen.changeSelection.isSelected()){
                                    deShowPanes();
                                    possibleStackPaneTiles.clear();


                                    // Der aktuelle Klick bezieht sich auf ein Tile, auf dem kein Piece der richtigen Farbe steht (oder gar kein Piece)
                                    piecePicked = false;
                                }
                            }
                        }
                        if (gameScreen.playerInCheck.isSelected()) {
                            gameScreen.checkGameState();
                        }else{
                            gameScreen.whiteWinsPane.setVisible(false);
                            gameScreen.blackWinsPane.setVisible(false);
                            gameScreen.checkPaneBlack.setVisible(false);
                            gameScreen.checkPaneWhite.setVisible(false);
                        }
                    });
                }
        );


    }


    /**
     * This method deletes the highlighting of the possible tiles to move on
     */
    private void deShowPanes(){
        for (StackPane stackPane: possibleStackPaneTiles) {
            stackPane.setStyle(null);

            iterateMatrix( (pane, i, j)->
                    {
                        String whiteStyle = "-fx-background-color:  #eeeed3;",
                                greenStyle = "-fx-background-color: #779656;";

                        if (i % 2 == 0) {
                            if (j % 2 == 0) {
                                pane.setStyle(whiteStyle);
                            } else {
                                pane.setStyle(greenStyle);
                            }
                        } else {
                            if (j % 2 != 0) {
                                pane.setStyle(whiteStyle);
                            } else {
                                pane.setStyle(greenStyle);
                            }
                        }
                    }
            );

        }
    }

    /**
     * This method colors the possible tiles when a piece is chosen on the gridPane.
     * It is called from the initStackPaneListeneres method.
     * @param row the row of the clicked stackPane
     * @param col the column of the clicked stackPane
     */
    //TODO refactor
    private void showPossibleTilesOnFirstClick(int row, int col){
        //for deleting Image
        String s = "#" + row + "" + col;
        stackPaneForDeletingImageOnSourcePane = (StackPane) gridPane.lookup(s);

        //check if there is a piece and it has the right color
        Piece p = game.getBoard().getSpecificTile(row, col).getPiece();
        Position pos = new Position(row,col);
        List<Tile> possibleTiles = p.possibleTiles(game.getBoard(), pos);

        //in eine eigene Funktion packen???
        for(Tile tile : possibleTiles){
            int posRow = tile.getRow();
            int posCol = tile.getColumn();
            String index = "#"+posRow+""+posCol;
            StackPane stackPane= (StackPane) gridPane.lookup(index);

            if(gameScreen.possibleMoves.isSelected()){
                stackPane.setStyle(stackPane.getStyle() + "-fx-border-color: #302e2b; -fx-border-width: 3px; -fx-border-insets: 2px; -fx-border-radius: 5px;");
            }
            //in dieser Zeile werden die stackpanes zu den possTiles gespeichert, damit sie später in
            //der "deShow"-Methode aufgerufen werden können
            possibleStackPaneTiles.add(stackPane);
        }
    }

    /**
     * This method calls the move method in the logic when a possible tile stackPane is clicked
     * It is called from the "showPossibleTilesOnFirstClick" method
     * @param row the row of the position of the piece on the last clicked stackPane
     * @param col the column of the position of the piece on the last clicked stackPane
     * @throws IOException e
     */
    private void movePieceInGui(int row, int col) throws IOException {
        Tile secondClickedTile = game.getBoard().getSpecificTile(row, col);
        Tile startTile = game.getBoard().getSpecificTile(lastClickedPaneRow, lastClickedPaneCol);
        Piece piece = startTile.getPiece();
        if (!game.isNetworkGame()){
            if(piece instanceof Pawn && piece.getColor() == ChessColor.Black && secondClickedTile.getRow() == 7){
                boardViewHelper.setPromotionLetterBlack(startTile, secondClickedTile);
            }else if(piece instanceof  Pawn && piece.getColor() == ChessColor.White && secondClickedTile.getRow() == 0){
                boardViewHelper.setPromotionLetterWhite(startTile,secondClickedTile);
            }else {
                game.getBoard().movePiece(startTile, secondClickedTile, boardViewHelper.promotionLetter);
            }
        }
        else {
            game.getBoard().movePiece(startTile, secondClickedTile, boardViewHelper.promotionLetter);
        }
        helpCompleteTheTurn();

        setImages();
    }

    private void helpCompleteTheTurn() throws IOException {
        if (GameState.getCurrentGameState() != GameState.WHITE_MOVE_OUT_OF_CHECK && GameState.getCurrentGameState() != GameState.BLACK_MOVE_OUT_OF_CHECK){
            if (game.isNetworkGame()){
                ObservableList<String> moveHistoryList = game.getMoveHistory().moveHistoryListProperty().getValue();
                String move = moveHistoryList.get(moveHistoryList.size() - 1);
                game.setSendToNetworkString(move);
                gameScreen.connectionType.sendNetworkMove();
            }
            possibleStackPaneTiles.clear();
            piecePicked = false;

            game.nextTurn();

            if (gameScreen.rotateBoard.isSelected() && !game.isAIGame() && !game.isNetworkGame()){
                boardRotation();
            }
        }
    }

    /**
     * This method is for the rotation of the board
     */
    public void boardRotation(){
        Duration duration = Duration.millis(1000);
        ScaleTransition downsizeTransition = new ScaleTransition(duration, gridPane);
        ScaleTransition enlargeTransition = new ScaleTransition(duration, gridPane);
        RotateTransition rotate = new RotateTransition(duration, gridPane);
        downsizeTransition.setByX(-0.2);
        downsizeTransition.setByY(-0.2);
        enlargeTransition.setByX(0.2);
        enlargeTransition.setByY(0.2);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(180);
        rotate.setDuration(Duration.millis(1000));
        rotate.setNode(gridPane);
        downsizeTransition.play();

        downsizeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rotate.play();
            }
        });
        rotate.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                enlargeTransition.play();
                stackPaneRotation();
            }
        });

        if (game.getCurrentPlayer() == ChessColor.White){
            gameScreen.whiteFrame.setVisible(true);
            gameScreen.blackFrame.setVisible(false);
        }else{
            gameScreen.whiteFrame.setVisible(false);
            gameScreen.blackFrame.setVisible(true);
        }

    }



    /**
     * This method is for the rotation of the stackpanes
     */
    public void stackPaneRotation(){
        iterateMatrix( (stackPane, i, j)->
                {
                    RotateTransition rotate = new RotateTransition();

                    rotate.setDuration(Duration.millis(500));
                    rotate.setAxis(Rotate.Z_AXIS);
                    rotate.setByAngle(180);

                    rotate.setNode(stackPane);
                    rotate.play();
                }
        );



    }

    /**
     * This method sets the image.png of the pieces on the gridPane
     */
    void setImages() {

        iterateMatrix( (stackPane, i, j)->
                {
                    SimpleObjectProperty<Tile> tile = tileProperties[i][j];
                    Piece p = tile.getValue().getPiece();
                    if(p != null)
                    {
                        char pieceChar = p.getConsoleChar(p.getColor());
                        String path = boardViewHelper.charToPath(pieceChar);

                        stackPane.getChildren().add(new ImageView(new Image(String.valueOf(getClass().getResource(path)))));
                    }
                }
        );
    }



    void iterateMatrix(MatrixVisitor visitor){
        for (int i = 0; i <8; i++) {
            for (int j = 0; j < 8; j++) {
                String index = "#" + i + "" + j;
                StackPane stackPane = (StackPane) gridPane.lookup(index);
                visitor.accept(stackPane, i, j);
            }
        }
    }
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }




}
