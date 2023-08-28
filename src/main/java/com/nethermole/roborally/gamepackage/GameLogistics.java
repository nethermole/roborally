package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.exceptions.ThisShouldntHappenException;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.gameservice.GameReport;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameLogistics {

    private static Logger log = LogManager.getLogger(GameLogistics.class);

    @Getter
    String uuid;

    @Getter
    private Game game;

    @Getter
    private StartInfo startInfo;

    GameLog gameLog;
    GameReport gameReport;


    @Getter
    GamestateVerifier gamestateVerifier;

    @Getter
    RulesFollowedVerifier rulesFollowedVerifier;

    //Start Info
    private Long seed;
    private GameConfig gameConfig;

    public GameLogistics(Long seed, GameConfig gameConfig) {
        gameLog = new GameLog();        //todo: this line will be removed

        this.seed = seed;
        this.gameConfig = gameConfig;

        this.uuid = UUID.randomUUID().toString();

        log.info("GameLogistics - Creating new game. Seed=" + seed + ", gameConfig=" + gameConfig);
        gamestateVerifier = new GamestateVerifier();
        rulesFollowedVerifier = new RulesFollowedVerifier();

        log.info("GameLogistics - Game created with UUID=" + uuid);
    }

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    public void startGameWithDefaultBoard() {
        BoardFactory boardFactory = new BoardFactory();
        startGame(seed, boardFactory.board_exchange());
    }

    public void startGame(Long seed, Board board) {
        GameBuilder gameBuilder = new GameBuilder(seed);
        gameBuilder.players(gameConfig.humanPlayers, gameConfig.botPlayers);
        gameBuilder.gameLog(gameLog);
        gameBuilder.board(board);
        gameBuilder.gameRules(new RulesFollowedVerifier());

        Position startPosition = gameBuilder.generateStartBeacon();
        gameBuilder.generateCheckpoints(20);

        game = gameBuilder.buildGame();

        log.info("New game started with startPosition: " + startPosition);

        game.setupForNextTurn();

        startInfo = new StartInfo(game.getPlayers().values().stream().map(player -> player.snapshot()).collect(Collectors.toList()), game.getStartPosition());
    }

    public Board getBoard() throws GameNotStartedException {
        if (game == null) {
            throw new GameNotStartedException();
        }

        return game.getBoard();
    }

    public List<ViewStep> getViewstepsByTurn(int turn) {
        if (game == null || turn >= game.getCurrentTurn()) {
            return new ArrayList<>();
        }
        return gameLog.getViewstepsByTurn(turn);
    }

    public List<MovementCard> getHand(int playerId) throws InvalidPlayerStateException, ThisShouldntHappenException {
        if (game == null) {
            throw new ThisShouldntHappenException("getHand - How would a client have the gameId for a null game?");
        }

        return game.getHand(playerId);
    }

    public void tryProcessTurn() {
        if (gamestateVerifier.isGameReadyToProcessTurn(game)) {
            game.processTurn();
            game.setupForNextTurn();
        }
    }

    public void submitHand(int playerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException, InvalidPlayerStateException, ThisShouldntHappenException {
        if (game == null) {
            throw new ThisShouldntHappenException("submitHand - How would a client have the gameId for a null game?");
        }

        if (rulesFollowedVerifier.isValidHand(playerId, movementCardList, game)) {
            game.submitPlayerHand(playerId, movementCardList);
        } else {
            throw new InvalidSubmittedHandException(game.getHand(playerId), movementCardList, playerId, game);
        }

        //todo: still not really where the turn queueing belongs. probably not in submitHand though...?
        tryProcessTurn();
    }
}
