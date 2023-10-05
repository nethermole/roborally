package com.nethermole.roborally.gamepackage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.exceptions.ThisShouldntHappenException;
import com.nethermole.roborally.gameReportStorage.GameReportRepository;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.gameservice.GameReport;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameLogistics {

    private static Logger log = LogManager.getLogger(GameLogistics.class);

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

    private GameConfig gameConfig;

    Map<String, Player> connectedPlayerMap;

    public GameLogistics(GameConfig gameConfig) {
        gameLog = new GameLog();        //todo: this line will be removed
        this.gameConfig = gameConfig;

        connectedPlayerMap = new HashMap<>();

        //todo: handle the exception nonsense
        try {
            log.info("GameLogistics - Creating new game. GameConfig=" + (new ObjectMapper().writeValueAsString(gameConfig)));
        } catch (JsonProcessingException e) {
            log.error("GameLogistics() log failure 71 remove this log probably");
        }
        gamestateVerifier = new GamestateVerifier();
        rulesFollowedVerifier = new RulesFollowedVerifier();
    }

    public String addPlayer() {
        if (connectedPlayerMap.size() < gameConfig.humanPlayers) {
            String connectedPlayerId = UUID.randomUUID().toString();
            connectedPlayerMap.put(connectedPlayerId, new HumanPlayer(""+connectedPlayerMap.size()));
            return connectedPlayerId;
        } else {
            return "addPlayer - Unable to connect. Lobby may be full";
        }
    }

    public boolean isGameAlreadyStarted() {
        return (game != null);
    }

    public void createGameWithDefaultBoard() {
        BoardFactory boardFactory = new BoardFactory();
        createGame(gameConfig.getGameSeed(), boardFactory.board_exchange());
    }

    public void createGame(Long seed, Board board) {
        GameBuilder gameBuilder = new GameBuilder(seed);
        gameBuilder.players(gameConfig.humanPlayers, gameConfig.botPlayers);
        gameBuilder.gameLog(gameLog);
        gameBuilder.board(board);

        BoardFactory boardFactory = new BoardFactory();

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(i!=0&&j!=0){
                    board.addBoard(boardFactory.board_exchange(), i, j);
                }
            }
        }

        gameBuilder.gameRules(new RulesFollowedVerifier());

        gameBuilder.generateStartBeacon();
        gameBuilder.generateCheckpoints(20);

        game = gameBuilder.buildGame();
        game.setGameConfig(gameConfig);

        log.info("New game created with uuid: " + game.getUuid());
        startInfo = new StartInfo(game.getPlayers().values().stream().map(player -> player.snapshot()).collect(Collectors.toList()), game.getStartPosition());
    }

    public void startGame() {
        game.setupForNextTurn();
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

    public List<MovementCard> getHand(String connectedPlayerId) throws InvalidPlayerStateException, ThisShouldntHappenException {
        if (game == null) {
            throw new ThisShouldntHappenException("getHand - How would a client have the gameId for a null game?");
        }

        String playerId = connectedPlayerMap.get(connectedPlayerId).getId();
        return game.getHand(playerId);
    }

    public void tryProcessTurn(GameReportRepository gameReportRepository) {
        if (gamestateVerifier.isGameReadyToProcessTurn(game)) {
            game.processTurn(gameReportRepository);
            game.setupForNextTurn();
        }
    }

    public void submitHand(String connectedPlayerId, List<MovementCard> movementCardList) throws InvalidSubmittedHandException, InvalidPlayerStateException, ThisShouldntHappenException {
        if (game == null) {
            throw new ThisShouldntHappenException("submitHand - How would a client have the gameId for a null game?");
        }

        String playerId = connectedPlayerMap.get(connectedPlayerId).getId();

        if (rulesFollowedVerifier.isValidHand(playerId, movementCardList, game)) {
            game.submitPlayerHand(playerId, movementCardList);
        } else {
            throw new InvalidSubmittedHandException(game.getHand(playerId), movementCardList, playerId, game);
        }
    }
}
