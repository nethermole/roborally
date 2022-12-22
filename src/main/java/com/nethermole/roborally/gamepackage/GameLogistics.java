package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.deck.GameState;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.view.AbstractView;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameLogistics {

    @Getter
    private Game game;

    @Getter
    private Map<Integer, Player> players;
    private List<AbstractView> viewers;

    @Getter
    private StartInfo startInfo;

    @Autowired
    GameLog gameLog;

    private static Logger log = LogManager.getLogger(Game.class);

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    //todo extract clientUpdate logic
    public void startGame(Map<Integer, Player> players, Long seed) {
        this.players = players;

        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.board_exchange();
        board.addBoard(boardFactory.board_exchange(), 1,0);

        GameBuilder gameBuilder = new GameBuilder(seed);
        gameBuilder.players(players);
        gameBuilder.gameLog(gameLog);
        gameBuilder.board(board);
        gameBuilder.generateStartBeacon();
        gameBuilder.generateCheckpoints(2);

        game = gameBuilder.buildGame();
        game.setupForNextTurn();

        startInfo = new StartInfo(players.values().stream().map(player -> player.snapshot()).collect(Collectors.toList()), game.getStartPosition());

        viewers = new ArrayList<>();
    }

    public void stopGame() {
        for (AbstractView view : viewers) {
            view.stopViewing();
        }
        game = null;
    }

    public Board getBoard() throws GameNotStartedException {
        if (game == null) {
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }

    public List<ViewStep> getViewstepsByTurn(int turn) {
        //todo: this
//        if(game.getWinningPlayer() != null){
//            return Arrays.asList(new GameEndViewStep(game.getWinningPlayer()));
//        }

        if (game == null || turn >= game.getCurrentTurn()) {
            return new ArrayList<>();
        }
        return gameLog.getViewstepsByTurn(turn);
    }

    public List<MovementCard> getHand(int playerId) {
        if (game == null) {
            return null;
        }
        Player player = players.get(playerId);
        return game.getHand(player);
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException {
        if (game == null) {
            throw new UnsupportedOperationException("game not started yet. cant submit hand");
        }

        Player player = players.get(playerId);
        game.submitPlayerHand(player, movementCardList);

        if (game.isReadyToProcessTurn()) {
            log.info("All hands received, processing turn");
            game.processTurn();
            game.setupForNextTurn();
            game.incrementCurrentTurn();
            log.info("Turn processing complete");
        }
    }

    public boolean isWaitingOnPlayer(int playerId){
        return game.getGameState() == GameState.TURN_PREPARATION &&
                !game.getPlayerSubmittedHands().containsKey(playerId);
    }
}
