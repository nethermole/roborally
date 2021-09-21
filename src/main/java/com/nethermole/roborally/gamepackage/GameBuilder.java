package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Beacon;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gameservice.GameLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameBuilder {

    //fields
    Map<Integer, Player> players;
    GameLog gameLog;
    Board board;
    Beacon startBeacon;
    List<Checkpoint> checkpoints;

    boolean hasCalledBoardLayout;

    public void players(Map<Integer, Player> players) {
        this.players = players;
    }

    public void gameLog(GameLog gameLog) {
        this.gameLog = gameLog;
    }

    public void board(Board board) {
        this.board = board;
        hasCalledBoardLayout = true;
    }

    public void generateStartBeacon() {
        if (!hasCalledBoardLayout) {
            throw new UnsupportedOperationException("Must call boardLayout before generateStartBeacon");
        }

        Position startPosition = board.getRandomEmptySquare();
        startBeacon = Beacon.startBeacon();
        board.addElement(startBeacon, startPosition);
        for (Player player : players.values()) {
            player.setBeacon(startBeacon);
            player.setPosition(startPosition);
        }
    }

    public void generateCheckpoints(int count) {
        checkpoints = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Checkpoint checkpoint = new Checkpoint(i);
            checkpoints.add(checkpoint);

            Position position = board.getRandomEmptySquare();
            board.addElement(checkpoint, position);
        }
    }

    public Game buildGame() {
        Game game = new Game();
        game.setPlayers(players);
        game.setGameLog(gameLog);
        game.setBoard(board);
        game.setStartBeacon(startBeacon);
        game.setCheckPoints(checkpoints);

        game.initializeFields();
        return game;
    }

}