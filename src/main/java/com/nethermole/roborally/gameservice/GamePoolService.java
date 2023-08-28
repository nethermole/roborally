package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.gamepackage.GameConfig;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.view.BasicView;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Service
public class GamePoolService {
    private static Logger log = LogManager.getLogger(GamePoolService.class);

    public Map<String, GameLogistics> gamePool;
    public static int count = 0;

    @PostConstruct
    public void init() {
        gamePool = new HashMap<>();

        Thread thread = new Thread(new GameThread(gamePool));
        thread.start();
    }

    public GameLogistics getGameLogistics(String gameId) {
        return gamePool.get(gameId);
    }

    public String startPlayerGame(Long seedIn, GameConfig gameConfig) {
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startPlayerGame(" + seed + ") called");

        GameLogistics gameLogistics = new GameLogistics(seed, gameConfig);
        gameLogistics.startGameWithDefaultBoard();

        String gameId = addGameLogisticsToPool(gameLogistics);
        return gameId;
    }

    public String startBotGame(Long seedIn) {
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startBotGame(" + seed + ") called");

        GameLogistics gameLogistics = new GameLogistics(seed, GameConfig.standardBot4Player);
        gameLogistics.startGameWithDefaultBoard();

        String gameId = addGameLogisticsToPool(gameLogistics);
        return gameId;
    }

    private String addGameLogisticsToPool(GameLogistics gameLogistics) {
        //todo: something something threadsafe count?
        String id = "" + count;
        count++;

        gamePool.put(id, gameLogistics);

        return id;
    }

    //todo: make this live near the views
    public void addBasicView(String gameId) {
        Board board = gamePool.get(gameId).getGame().getBoard();
        try {
            BasicView basicView = new BasicView(5);
            basicView.setBoard(board);
            for (Player player : gamePool.get(gameId).getGame().getPlayers().values()) {
                basicView.getWindow().getCanvas().addPlayer(player);
            }
            Thread thread = new Thread(basicView);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GameThread implements Runnable {

        private int MAX_DURATION_MINS;

        @Setter
        private Map<String, GameLogistics> gamePool;

        public GameThread(Map<String, GameLogistics> gamePool) {
            this.gamePool = gamePool;
            this.MAX_DURATION_MINS = 5;
        }

        @Override
        public void run() {
            Long start = System.currentTimeMillis();
            Iterator<GameLogistics> gameLogisticsIterator = null;

            while (System.currentTimeMillis() < start + MAX_DURATION_MINS * 60 * 1000) {
                if (System.currentTimeMillis() % 250 == 0) {
                    if (gameLogisticsIterator == null) {
                        gameLogisticsIterator = gamePool.values().iterator();
                    }

                    if (gameLogisticsIterator.hasNext()) {
                        GameLogistics gameLogistics = gameLogisticsIterator.next();
                        gameLogistics.tryProcessTurn();
                    } else {
                        gameLogisticsIterator = null;
                    }
                }
            }
        }
    }

}
