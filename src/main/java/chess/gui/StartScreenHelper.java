package chess.gui;

import chess.model.ChessColor;
import chess.model.ai.MInMaxAI;
import chess.model.ai.SimpleAI;
import chess.model.network.Client;
import chess.model.network.Server;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Helper Class for StartScreen. it handles the creation of our NetworkType and our enemy AiType
 */
public class StartScreenHelper {

    private final StartScreen startScreen;

    /**
     * Constructor of StartScreenHelper
     * @param startScreen the StartScreen which needs some Help
     */
    public StartScreenHelper(StartScreen startScreen){
        this.startScreen = startScreen;
    }

    /**
     * This method creates a new AI based on the flags, which where set by the buttons in StartScreen
     */
    public void createAI(){
        startScreen.setAiType(null);
        ChessColor aiColor = startScreen.game.getAIColor();
        boolean isBlack = aiColor == ChessColor.Black;
        if (startScreen.simpleAI){
            startScreen.setAiType(new SimpleAI(startScreen.game.getBoard(), aiColor, isBlack));
        }
        else {
            int maxDepth = (int) startScreen.depthSlider.getValue();
            startScreen.game.getBoard().maxDepth = maxDepth - 1;
            startScreen.setAiType(new MInMaxAI(startScreen.game.getBoard(), aiColor, isBlack));
        }
    }

    /**
     * This method creates a new ConnectionType based on the flags, which where set by the buttons in StartScreen
     * @throws IOException e
     */
    public void connectionSetup() throws IOException {
        startScreen.setConnectionType(null);
        if (startScreen.isServer){
            startScreen.setConnectionType(new Server(startScreen.game));
        }
        else{
            startScreen.setConnectionType(new Client(startScreen.game));
        }

    }

    /**
     * making the firstTurn as AI if selected to play as Black
     * @param boardView the Current BordView
     */
    public void initFirstAITurn(BoardView boardView){
        startScreen.aiType.makeAIMove();
        startScreen.game.nextTurn();
        boardView.gridPane.setRotate(180);
        for (int i = 0; i <8; i++) {
            for (int j = 0; j < 8; j++) {
                String index = "#" + i + "" + j;
                StackPane stackPane = (StackPane) boardView.gridPane.lookup(index);
                stackPane.setRotate(180);
            }
        }
    }

    /**
     * making the firstTurn as ConnectionType if selected to play as Black
     * @param boardView the Current BordView
     * @throws IOException e
     */
    public void initFirstNetworkTurn(BoardView boardView) throws IOException {
        startScreen.connectionType.makeNetworkMove();
        startScreen.game.nextTurn();
        boardView.gridPane.setRotate(180);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String index = "#" + i + "" + j;
                StackPane stackPane = (StackPane) boardView.gridPane.lookup(index);
                stackPane.setRotate(180);
            }
        }
    }
}
