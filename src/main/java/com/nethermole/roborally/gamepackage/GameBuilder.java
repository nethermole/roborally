package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.CountingBot;
import com.nethermole.roborally.gameservice.GameLog;
import com.nethermole.roborally.gameservice.GameReport;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameBuilder {

    @Getter
    Random random;

    Map<Integer, Player> players;
    GameLog gameLog;
    RulesFollowedVerifier rulesFollowedVerifier;
    Board board;

    @Getter
    Beacon startBeacon;

    List<Checkpoint> checkpoints;

    boolean hasCalledBoardLayout;

    GameBuilder(Long seed) {
        this.random = new Random(seed);
    }

    public void players(int numHumanPlayers, int numBots) {
        this.players = new HashMap<>();
        while (players.size() < numHumanPlayers) {
            int playerId = players.size();
            players.put(playerId, new HumanPlayer(playerId));
        }
        while (players.size() < numHumanPlayers + numBots) {
            int playerId = players.size();
            players.put(playerId, new CountingBot(playerId));
        }
    }

    public void gameLog(GameLog gameLog) {
        this.gameLog = gameLog;
    }

    public void gameRules(RulesFollowedVerifier rulesFollowedVerifier) {
        this.rulesFollowedVerifier = rulesFollowedVerifier;
    }

    public void board(Board board) {
        this.board = board;
        hasCalledBoardLayout = true;
    }

    /*
    returns startPosition
     */
    public Position generateStartBeacon() {
        if (!hasCalledBoardLayout) {
            throw new UnsupportedOperationException("Must call boardLayout before generateStartBeacon");
        }

        Position startPosition = board.getRandomEmptySquare(random);
        startBeacon = Beacon.startBeacon();
        board.addElement(startBeacon, startPosition);
        for (Player player : players.values()) {
            player.setBeacon(startBeacon);
            player.setPosition(startPosition);
        }
        return startPosition;
    }

    public void generateCheckpoints(int count) {
        checkpoints = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Checkpoint checkpoint = new Checkpoint(i);
            checkpoints.add(checkpoint);

            Position position = board.getRandomEmptySquare(random);
            board.addElement(checkpoint, position);
        }
    }

    public Game buildGame() {
        GameReport gameReport = new GameReport();

        Game game = new Game(random);
        game.setGameLog(gameLog);
        game.setGameReport(gameReport);
        game.setBoard(board);
        game.setStartBeacon(startBeacon);
        game.setCheckPoints(checkpoints);

        game.initializeFields(players);
        return game;
    }

}
