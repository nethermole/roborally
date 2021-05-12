package com.nethermole.roborally;

import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.game.Game;
import com.nethermole.roborally.game.board.Board;
import com.nethermole.roborally.game.deck.movement.MovementCard;
import com.nethermole.roborally.game.player.Player;
import com.nethermole.roborally.logs.GameEventLogger;
import com.nethermole.roborally.view.AbstractView;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameLogistics {

    @Getter
    private Game game;

    @Getter //todo remove getter when unity view is functional
    private Map<Integer, Player> players;
    private List<AbstractView> viewers;
    private GameEventLogger gameEventLogger;

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    public void startGame(Map<Integer, Player> players) {
        this.players = players;
        this.gameEventLogger = new GameEventLogger();
        game = new Game(new ArrayList<>(players.values()), 8, 8, gameEventLogger);

        //constructBoard(boardElements, boardHeight, boardWidth, startLocation, gameEventLogger);
        game.constructBoard("Empty");        //boardconstructor needs to be broken out into separate class
        game.distributeCards();

        //POCView pocView = POCView.startPOCView(this);
        viewers = new ArrayList<>();
        //viewers.add(pocView);
    }

    public void stopGame() {
        for (AbstractView view : viewers) {
            view.stopViewing();
        }
        game = null;
    }

    //todo remove once unity view is functional
    public Board getBoard() throws GameNotStartedException {
        if (game == null) {
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }

    public List<MovementCard> getHand(int playerId) {
        if (game == null) {
            return null;
        }
        Player player = players.get(playerId);
        return game.getHand(player);
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList) {
        if (game == null) {
            throw new UnsupportedOperationException("game not started yet. cant submit hand");
        }
        Player player = players.get(playerId);
        game.submitPlayerHand(player, movementCardList);
        if (game.isReadyToProcessTurn()) {
            startProcessingTurn();
        }
    }

    public void startProcessingTurn() {
        game.processTurn();
    }
}
