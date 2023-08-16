package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.viewStep.ViewStep;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.view.AbstractView;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameLogistics {

    private static Logger log;
    private List<AbstractView> viewers;

    @Getter
    String uuid;

    @Getter
    private Game game;

    @Getter
    private StartInfo startInfo;

    GameLog gameLog;
    Map<Integer, Player> players;

    public GameLogistics(Map<Integer, Player> players) {
        log = LogManager.getLogger(GameLogistics.class);
        this.players = players;
        this.uuid = UUID.randomUUID().toString();
        log.info("Created new bot game with UUID="+uuid);

        viewers = new ArrayList<>();
        gameLog = new GameLog();
    }

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    public void startGameWithDefaultBoard(Long seed){
        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.board_exchange();
        for(Player player : players.values()){
            board.addPlayer(player);
        }
        startGame(seed, board);
    }

    public void startGame(Long seed, Board board) {
        GameBuilder gameBuilder = new GameBuilder(seed);
        gameBuilder.players(players);
        gameBuilder.gameLog(gameLog);
        gameBuilder.board(board);
        Position startPosition = gameBuilder.generateStartBeacon();
        gameBuilder.generateCheckpoints(20);

        game = gameBuilder.buildGame();

        log.info("New game started with startPosition: " + startPosition);

        game.setupForNextTurn();

        startInfo = new StartInfo(players.values().stream().map(player -> player.snapshot()).collect(Collectors.toList()), game.getStartPosition());

        viewers = new ArrayList<>();
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

    public List<MovementCard> getHand(int playerId) throws InvalidPlayerStateException {
        if (game == null) {
            return null;
        }
        return game.getHand(playerId);
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException, InvalidPlayerStateException {
        if (game == null) {
            throw new UnsupportedOperationException("game not started yet. cant submit hand");
        }

        game.submitPlayerHand(playerId, movementCardList);

        if (game.isReadyToProcessTurn()) {
            log.info("All hands received, processing turn");
            game.processTurn();
            log.info("Turn processing complete. Setting up for next turn.");
            game.setupForNextTurn();
            log.info("Done setting up for next turn");
        }
    }
}
