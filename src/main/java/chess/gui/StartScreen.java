package chess.gui;

        import chess.model.SaveLoadManager;
        import chess.model.ai.AIType;


        import chess.model.ChessColor;
        import chess.model.Constants;
        import chess.model.Game;


        import chess.model.network.ConnectionType;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.Pane;

        import javafx.scene.text.Text;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;
        import java.io.IOException;
        import java.nio.file.Path;
        import java.util.Locale;
        import java.util.ResourceBundle;

/**
 * This class contains the informations for the start screen
 * Due to the use of FXML we have a tight coupling between the View and the Controller
 */
@SuppressWarnings({"PMD.TooManyFields"})
public class StartScreen {

    @FXML
    public Menu gameMenu;
    @FXML
    public MenuItem closeControl;
    @FXML
    public Menu language;
    @FXML
    public Menu help;
    @FXML
    public MenuItem about;
    @FXML
    public MenuItem english;
    @FXML
    public MenuItem german;
    @FXML
    public Text selectGameModeText;
    @FXML
    public ToggleButton togglePlayer;
    @FXML
    public ToggleButton toggleComputer;
    @FXML
    public ToggleButton toggleNetwork;
    @FXML
    public Text selectAiText;
    @FXML
    public ToggleButton buttonSimpleAi;
    @FXML
    public ToggleButton buttonMinMaxAi;
    @FXML
    public RadioButton radioWhiteButton;
    @FXML
    public RadioButton radioBlackButton;
    @FXML
    public Text chooseColorText;
    @FXML
    public Text enterNetworkText;
    @FXML
    public TextField enterNetworkGame;
    @FXML
    public Button startGameButton;
    @FXML
    public Button loadGameButton;
    @FXML
    public Pane networkGamePane;
    @FXML
    public Pane selectAiPane;
    @FXML
    public Pane colorPane;
    @FXML
    public Parent root;
    @FXML
    public Pane iPAddressPane;
    @FXML
    public ToggleButton serverToggle;
    @FXML
    public ToggleButton clientToggle;
    @FXML
    public Pane selectAiDepthPane;
    @FXML
    public Text selectAiDepthText;
    @FXML
    public Slider depthSlider;



    Game game = new Game();
    AIType aiType;
    ConnectionType connectionType;
    BoardView boardView = new BoardView();

    public boolean englishFlag = true;

    private ResourceBundle bundle;
    private Stage stage;
    public boolean simpleAI = true;
    public boolean isServer = true;

    /**
     * The setter for the Stage object
     * @param stage the window of the whole application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * This method starts the Game and loads the GameScreen, when the start button is clicked
     * @throws IOException exception
     */
    @FXML
    public void switchToGameScreen() throws IOException{
        StartScreenHelper guiHelper = new StartScreenHelper(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gameScreen.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setWidth(1150);
        stage.setHeight(800);
        stage.setScene(scene);

        GameScreen gameScreen = fxmlLoader.getController();
        gameScreen.englishFlag = englishFlag;
        gameScreen.changeLanguageCorrection();

        if(game.isAIGame()){
            guiHelper.createAI();
            gameScreen.setAiType(aiType);
            gameScreen.rotateBoard.setVisible(false);
        }
        if (game.isNetworkGame()){
            String ip = enterNetworkGame.getText();
            game.setIp(ip);
            guiHelper.connectionSetup();
            gameScreen.setConnectionType(connectionType);
            gameScreen.rotateBoard.setVisible(false);
        }
        gameScreen.setGame(game);
        gameScreen.setBoardView(boardView);
        boardView.setGridPane((GridPane) root.lookup("#gridPane"));


        boardView.setGameScreen(gameScreen);
        boardView.setGame(game);

        if (game.getAIColor().equals(ChessColor.White)){
            guiHelper.initFirstAITurn(boardView);
        }
        if (game.getNetworkColor().equals(ChessColor.White)){
            guiHelper.initFirstNetworkTurn(boardView);
        }
        boardView.initTileProperties();
        boardView.setImages();
        boardView.initStackpaneListeners();
        gameScreen.initGraveyard();
        gameScreen.initMoveHistory();
        gameScreen.setStage(stage);
        stage.show();
    }

    /**
     * This method changes the view of the StartScreen, to show the vs. Player options
     */
    @FXML
    public void playerVsPlayer() {
        selectAiDepthPane.setVisible(false);
        colorPane.setVisible(false);
        selectAiPane.setVisible(false);
        networkGamePane.setVisible(false);
        iPAddressPane.setVisible(false);
        game.setAIGame(false);
        game.setNetworkGame(false);
    }

    /**
     * This method changes the view of the StartScreen, to show the vs. AI options
     */
    @FXML
    public void playerVsComputer() {
        selectAiDepthPane.setVisible(false);
        colorPane.setVisible(true);
        selectAiPane.setVisible(true);
        networkGamePane.setVisible(false);
        iPAddressPane.setVisible(false);
        game.setAIGame(true);
        game.setNetworkGame(false);
    }

    /**
     * This method changes the view of the StartScreen, to show the network game options
     */
    @FXML
    public void networkGame(){
        colorPane.setVisible(false);
        selectAiPane.setVisible(false);
        networkGamePane.setVisible(true);
        selectAiDepthPane.setVisible(false);
        if (clientToggle.isSelected()){
            iPAddressPane.setVisible(true);
        }
        game.setAIGame(false);
        game.setNetworkGame(true);


    }

    /**
     * creates a simple AI
     */
    @FXML
    public void toggleSimpleAi(){
        selectAiDepthPane.setVisible(false);
        simpleAI = true;
    }

    /**
     * creates a advanced AI
     */
    @FXML
    public void toggleMinMaxAi(){
        simpleAI = false;
        selectAiDepthPane.setVisible(true);
    }

    /**
     * sets ai-color  black if player selects white
     */
    @FXML
    public void radioWhite(){
        game.setAIColor(ChessColor.Black);
    }

    /**
     * sets ai-color  black if player selects white
     */
    @FXML
    public void radioBlack(){
        game.setAIColor(ChessColor.White);
    }

    /**
     * This method changes the language of every content in the start screen to german or english
     * @param event on click
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
        }else{
            Constants.LANGUAGE = new Locale("de", "DE");
        }
        this.bundle = ResourceBundle.getBundle("Language", Constants.LANGUAGE);
        this.initializeComponents();
    }

    /**
     * This method initializes the components to change their language
     */
    public void initializeComponents(){
        gameMenu.setText(this.bundle.getString("key.game"));
        closeControl.setText(this.bundle.getString("key.close"));
        language.setText(this.bundle.getString("key.language"));
        help.setText(this.bundle.getString("key.help"));
        about.setText(this.bundle.getString("key.about"));
        selectGameModeText.setText(this.bundle.getString("key.selecttheGame-Mode"));
        togglePlayer.setText(this.bundle.getString("key.vsPlayer"));
        toggleComputer.setText(this.bundle.getString("key.vsAI"));
        toggleNetwork.setText(this.bundle.getString("key.networkGame"));
        selectAiText.setText(this.bundle.getString("key.selecttheAI"));
        buttonSimpleAi.setText(this.bundle.getString("key.simpleAI"));
        buttonMinMaxAi.setText(this.bundle.getString("key.minMaxAI"));
        radioWhiteButton.setText(this.bundle.getString("key.white"));
        radioBlackButton.setText(this.bundle.getString("key.black"));
        chooseColorText.setText(this.bundle.getString("key.chooseyourcolor"));
        startGameButton.setText(this.bundle.getString("key.startGame"));
        enterNetworkText.setText(this.bundle.getString("key.enteraddressfornetworkgame"));
        enterNetworkGame.setText(this.bundle.getString("key.enterIPAddress"));
        loadGameButton.setText(this.bundle.getString("key.loadGame"));
        selectAiDepthText.setText(this.bundle.getString("key.selectAiDepthText"));
    }


    /**
     * if selected server button in View, this method will set the Server Flag on true
     */
    @FXML
    public void server(){
        iPAddressPane.setVisible(false);
        isServer = true;
        game.setNetworkColor(ChessColor.Black);

    }
    /**
     * if selected client button in View, this method will set the Server Flag on flase
     */
    @FXML
    public void client(){
        iPAddressPane.setVisible(true);
        isServer = false;
        game.setNetworkColor(ChessColor.White);
    }

    /**
     * This method loads the option to load a saved game
     */
    @FXML
    public void loadGame() {

        try {
            File savedGamesDir = new File(System.getProperty("user.home"), "ChessAI/saved_games");
            if (! savedGamesDir.exists()) {
                savedGamesDir.mkdirs();
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialDirectory(savedGamesDir);


            File selectedFile = fileChooser.showOpenDialog(stage);
            Path path = selectedFile.toPath();
            String s = path.toString();

            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            Game loadedGame = saveLoadManager.loadGame(s);
            setGame(loadedGame);
            switchToGameScreen();
            if(game.getCurrentPlayer() == ChessColor.Black){
                boardView.boardRotation();
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong. Didn't you wanted to save the game?");
        }
    }

    /**
     * This method closes the whole Game, when the close button in the menu is clicked
     */
    @FXML
    public void close(){
        System.exit(1);
    }


    /**
     * The setter of the Ai-Type
     * @param aiType the type of the Artificial intelligence
     */
    public void setAiType(AIType aiType) {
        this.aiType = aiType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}


