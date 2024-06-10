package chess.gui;

import chess.model.ai.AIType;
import chess.model.*;
import chess.model.pieces.Piece;
import chess.model.network.ConnectionType;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class contains the informations for the game screen
 * Due to the use of FXML we have a tight coupling between the View and the Controller
 */
@SuppressWarnings("PMD.TooManyFields")
public class GameScreen {
    public GridPane gridPane;

    BoardView boardView;

    @FXML
    public Parent root;
    @FXML
    public Pane gameScreenPane;
    @FXML
    public Text menuText;
    @FXML
    public MenuButton gameButton;
    @FXML
    public MenuButton settings;
    @FXML
    public MenuButton language;
    @FXML
    public MenuItem english;
    @FXML
    public MenuItem german;
    @FXML
    public MenuItem saveGameControl;
    @FXML
    public MenuItem returnStartScreen;
    @FXML
    public MenuItem closeControl;
    @FXML
    public CheckMenuItem possibleMoves;
    @FXML
    public CheckMenuItem playerInCheck;
    @FXML
    public CheckMenuItem rotateBoard;
    @FXML
    public CheckMenuItem changeSelection;
    @FXML
    public Label lastMovesText;
    @FXML
    public ListView<String> historyList;
    @FXML
    public GridPane graveyard;
    @FXML
    public Pane menuPane;
    @FXML
    public ToggleButton burgerButton;
    @FXML
    public Pane checkPaneWhite;
    @FXML
    public Pane checkPaneBlack;
    @FXML
    public Pane whiteWinsPane;
    @FXML
    public Pane blackWinsPane;
    @FXML
    public Pane staleMatePane;
    @FXML
    public Pane whiteFrame;
    @FXML
    public Pane blackFrame;
    @FXML
    public ToggleButton queenButton;
    @FXML
    public ToggleButton bishopButton;
    @FXML
    public ToggleButton knightButton;
    @FXML
    public ToggleButton rookButton;
    @FXML
    public ToggleButton queenButtonB;
    @FXML
    public ToggleButton bishopButtonB;
    @FXML
    public ToggleButton knightButtonB;
    @FXML
    public ToggleButton rookButtonB;
    @FXML
    public Pane whitePromotionPane;
    @FXML
    public Pane blackPromotionPane;
    @FXML
    public Text whiteIsChecked;
    @FXML
    public Text blackIsChecked;
    @FXML
    public Text whiteWins;
    @FXML
    public Text blackWins;
    @FXML
    public Text stalemate;
    @FXML
    public Text whitePromotionTitle;
    @FXML
    public Text whitePromotionChooseText;
    @FXML
    public Text blackPromotionTitle;
    @FXML
    public Text blackPromotionChooseText;
    @FXML
    public Pane gameOverPane;

    private Game game;
    private AIType aiType;
    public ConnectionType connectionType;

    private Stage stage;
    public ResourceBundle bundle;
    public boolean englishFlag = true;
    String stringToSendToNetworkPartner;


    /**
     * This method saves the current game state
     */
    @FXML
    public void saveGame(){

        try {
            File savedGamesDir = new File(System.getProperty("user.home"), "ChessAI/saved_games");
            if (! savedGamesDir.exists()) {
                savedGamesDir.mkdirs();
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialDirectory(savedGamesDir);

            File selectedFile = fileChooser.showSaveDialog(stage);
            Path path = selectedFile.toPath();
            String s = path.toString();
            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            saveLoadManager.saveGame(s);
        }
        catch (Exception e){
            System.out.println("There went something wrong. Maybe there was nothing to save? Play some moves!");
        }
    }

    /**
     * The setter for the Stage object
     * @param stage the window of the whole application
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * This method loads and shows the start screen fxml
     * @throws IOException e
     */
    @FXML
    public void switchToStartScreen() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/startScreen.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) gameScreenPane.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setWidth(1150);
        stage.setHeight(800);
        stage.setResizable(false);

        stage.setScene(scene);


        StartScreen startScreen = fxmlLoader.getController();
        startScreen.englishFlag = englishFlag;
        startScreen.changeLanguageCorrection();
        startScreen.setStage(stage);
        stage.show();
    }



    /**
     * This method initializes the changelisteners for the graveyard
     */
    void initGraveyard(){
        //this part is for the load function. if the list isn't empty, it loads the list
        if(!game.getBoard().getRemovedPieces().isEmpty()){
            for(Piece p : game.getBoard().removedPiecesProperty().getValue()) {

                char pieceChar = p.getConsoleChar(p.getColor());
                String path = boardView.boardViewHelper.charToPath(pieceChar);
                ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource(path))));
                addToGraveyard(imageView);
            }
        }


        //this is the init part
        game.getBoard().removedPiecesProperty().addListener(new ListChangeListener<Piece>() {
            @Override
            public void onChanged(Change<? extends Piece> change) {
                while (change.next()) {
                    if(change.wasAdded()){
                        ObservableList<Piece> removedPiece = game.getBoard().removedPiecesProperty().getValue();
                        Piece p = removedPiece.get(removedPiece.size() - 1);

                        char pieceChar = p.getConsoleChar(p.getColor());

                        String path = boardView.boardViewHelper.charToPath(pieceChar);
                        ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource(path))));
                        addToGraveyard(imageView);
                    }
                }
            }
        });
    }



    /**
     * this methods adds the beaten pieces to the graveyard stackpanes
     * @param imageView the image that contains the image of the beaten piece
     */
    public void addToGraveyard(ImageView imageView){
        for (int i = 0; i < 2; i++) {
            for (int j = 10; j <= 25; j++) {

                //TODO: noch nach farben sortieren
                String index = "#" + i + "" + j;
                StackPane pane = (StackPane) graveyard.lookup(index);

                if(pane.getChildren().isEmpty()){
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    pane.getChildren().add(imageView);
                }
            }
        }
    }

    /**
     * method that initializes the MoveHistory, and keeps on watching if changes occur
     */
    public void initMoveHistory(){
        if(!game.getBoard().getMoveHistory().moveHistoryListProperty().isEmpty()){
            for(String s : game.getBoard().getMoveHistory().moveHistoryListProperty()) {
                historyList.getItems().add(s);
            }
        }

        game.getBoard().getMoveHistory().moveHistoryListProperty().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if(change.wasAdded()){
                        ObservableList<String> moveHistoryList = game.getMoveHistory().moveHistoryListProperty().getValue();
                        String move = moveHistoryList.get(moveHistoryList.size() - 1);

                        historyList.getItems().add(move);
                    }
                }
            }
        });
    }

    /**
     * This method sets the chosen language
     * @param event the event that initialize the language setting
     */
    @FXML
    public void changeLanguage(ActionEvent event){
        if (event.getSource().equals(this.english)){
            this.english.setDisable(true);
            this.german.setDisable(false);
            englishFlag = true;
            Constants.LANGUAGE = new Locale("en", "EN");
        } else if (event.getSource().equals(this.german)){
            this.english.setDisable(false);
            this.german.setDisable(true);
            englishFlag = false;
            Constants.LANGUAGE = new Locale("de", "DE");
        }
        this.bundle = ResourceBundle.getBundle("Language", Constants.LANGUAGE);
        this.initializeComponents();
    }

    /**
     * This method connects the language changes of the game and startscreen
     */
    public void changeLanguageCorrection(){
        if(englishFlag){
            Constants.LANGUAGE = new Locale("en", "EN");
        }else {
            Constants.LANGUAGE = new Locale("de", "DE");
        }
        this.bundle = ResourceBundle.getBundle("Language", Constants.LANGUAGE);
        this.initializeComponents();
    }

    /**
     * This method initializes the components on screen
     */
    public void initializeComponents(){
        menuText.setText(this.bundle.getString("key.menuText"));
        gameButton.setText(this.bundle.getString("key.game"));
        closeControl.setText(this.bundle.getString("key.close"));
        language.setText(this.bundle.getString("key.language"));
        settings.setText(this.bundle.getString("key.settings"));
        saveGameControl.setText(this.bundle.getString("key.saveGame"));
        returnStartScreen.setText(this.bundle.getString("key.returnStartScreen"));
        possibleMoves.setText(this.bundle.getString("key.possibleMoves"));
        playerInCheck.setText(this.bundle.getString("key.playerInCheck"));
        rotateBoard.setText(this.bundle.getString("key.rotateBoard"));
        changeSelection.setText(this.bundle.getString("key.changeSelection"));
        lastMovesText.setText(this.bundle.getString("key.lastMovesText"));
        whiteIsChecked.setText(this.bundle.getString("key.whiteIsChecked"));
        blackIsChecked.setText(this.bundle.getString("key.blackIsChecked"));
        whiteWins.setText(this.bundle.getString("key.whiteWins"));
        blackWins.setText(this.bundle.getString("key.blackWins"));
        stalemate.setText(this.bundle.getString("key.stalemate"));
        whitePromotionTitle.setText(this.bundle.getString("key.whitePromotionTitle"));
        whitePromotionChooseText.setText(this.bundle.getString("key.whitePromotionChooseText"));
        blackPromotionTitle.setText(this.bundle.getString("key.blackPromotionTitle"));
        blackPromotionChooseText.setText(this.bundle.getString("key.blackPromotionChooseText"));
        queenButton.setText(this.bundle.getString("key.queenButton"));
        bishopButton.setText(this.bundle.getString("key.bishopButton"));
        knightButton.setText(this.bundle.getString("key.knightButton"));
        rookButton.setText(this.bundle.getString("key.rookButton"));
        queenButtonB.setText(this.bundle.getString("key.queenButton"));
        bishopButtonB.setText(this.bundle.getString("key.bishopButton"));
        knightButtonB.setText(this.bundle.getString("key.knightButton"));
        rookButtonB.setText(this.bundle.getString("key.rookButton"));
    }

    /**
     * This method opens and closes the burger menu on the GameScreen
     */
    @FXML
    public void showMenu(){
        if (burgerButton.isSelected()){
            menuPane.setLayoutX(0);
            gameButton.setStyle(gameButton.getStyle()+"-fx-pref-width:210;");
            settings.setStyle(settings.getStyle()+"-fx-pref-width:210;");
            language.setStyle(language.getStyle()+"-fx-pref-width:210;");
        }else{
            menuPane.setLayoutX(-157);
            gameButton.setStyle(gameButton.getStyle()+"-fx-pref-width:170;");
            settings.setStyle(settings.getStyle()+"-fx-pref-width:170;");
            language.setStyle(language.getStyle()+"-fx-pref-width:170;");
        }
    }

    /**
     * method which does the AIMove for the GUI
     */
    void doAITurn(){
        if (GameState.getCurrentGameState() == GameState.WHITE_WINS || GameState.getCurrentGameState() == GameState.BLACK_WINS || GameState.getCurrentGameState() == GameState.STALEMATE){
            System.out.println("GAME OVER");
        }
        else {
            System.out.println("this is the Ai move");
            aiType.makeAIMove();
            rotateBoard.setVisible(false);
            game.nextTurn();
            boardView.setImages();
        }
    }

    /**
     * method which does the ConnectionMove in GUI
     * @throws IOException exception
     */
    public void doNetworkTurn() throws IOException {
        if (GameState.getCurrentGameState() == GameState.WHITE_WINS || GameState.getCurrentGameState() == GameState.BLACK_WINS || GameState.getCurrentGameState() == GameState.STALEMATE){
            System.out.println("GAME OVER");
        }
        else {
            connectionType.makeNetworkMove();
            rotateBoard.setVisible(false);
            game.nextTurn();
            boardView.setImages();
        }
    }

    /**
     * This method handles the option to show, if the current player is in check
     */
    @FXML
    public void showIfPlayerInCheck(){
        if (playerInCheck.isSelected()) {
            checkGameState();
        }else{
            checkPaneBlack.setVisible(false);
            checkPaneWhite.setVisible(false);
        }
    }

    /**
     * This methods checks the current Gamestate and displays it at the gamescreen
     */
    void checkGameState(){
        if(GameState.getCurrentGameState() == GameState.BLACK_IN_CHECK || GameState.getCurrentGameState() == GameState.BLACK_MOVE_OUT_OF_CHECK){
            checkPaneBlack.setVisible(true);
            checkPaneWhite.setVisible(false);
        }else if (GameState.getCurrentGameState() == GameState.WHITE_IN_CHECK || GameState.getCurrentGameState() == GameState.WHITE_MOVE_OUT_OF_CHECK){
            checkPaneWhite.setVisible(true);
            checkPaneBlack.setVisible(false);
        }
        else if (GameState.getCurrentGameState() == GameState.WHITE_WINS){
            whiteWinsPane.setVisible(true);
            checkPaneBlack.setVisible(false);
            checkPaneWhite.setVisible(false);
            gameOverPane.setVisible(true);
        }
        else if (GameState.getCurrentGameState() == GameState.BLACK_WINS){
            blackWinsPane.setVisible(true);
            checkPaneBlack.setVisible(false);
            checkPaneWhite.setVisible(false);
            gameOverPane.setVisible(true);
        }
        else if (GameState.getCurrentGameState() == GameState.STALEMATE){
            staleMatePane.setVisible(true);
            checkPaneBlack.setVisible(false);
            checkPaneWhite.setVisible(false);
            gameOverPane.setVisible(true);
            //hhhh
        }
        else {
            checkPaneBlack.setVisible(false);
            checkPaneWhite.setVisible(false);
        }
    }

    /**
     * This method closes the menu controls
     */
    @FXML
    public void close(){
        System.exit(1);
    }


    /**
     * This method handles the setting changes for the board rotation
     * @param event mouseclick on the checkItem in the settings
     */
    public void rotationCorrection(ActionEvent event){

        whiteFrame.setVisible(true);
        blackFrame.setVisible(false);

        if (!((CheckMenuItem) event.getSource()).isSelected()){
            boardView.gridPane.setRotate(0);
            boardView.iterateMatrix( (stackPane, i, j)->
                    {
                        stackPane.setRotate(0);
                    }
            );
        } else{
            if (game.getCurrentPlayer() == ChessColor.White){
                boardView.gridPane.setRotate(0);
                whiteFrame.setVisible(true);
                blackFrame.setVisible(false);
                boardView.iterateMatrix( (stackPane, i, j)->
                        {
                            stackPane.setRotate(0);
                        }
                );
            } else{
                boardView.gridPane.setRotate(180);
                whiteFrame.setVisible(false);
                blackFrame.setVisible(true);
                boardView.iterateMatrix( (stackPane, i, j)->
                        {
                            stackPane.setRotate(180);
                        }
                );
            }
        }
    }



    /**
     * The setter for the game object in GameScreen
     * @param game the game object
     */
    public void setGame(Game game){
        this.game = game;
    }

    /**
     * The getter for the game object in GameScreen
     * @return the game object
     */
    public Game getGame(){
        return game;
    }

    /**
     * The setter of the Ai-Type
     * @param aiType the type of the Artificial intelligence
     */
    public void setAiType(AIType aiType) {
        this.aiType = aiType;
    }
    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

}
