package com.nethermole.roborally.gameservice;

import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.board.Board;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GamePoolService {

    public Map<String, GameLogistics> gamePool;
    int count = 0;

    @PostConstruct
    public void init() {
        gamePool = new HashMap<>();
    }

    public String addGameLogistics(GameLogistics gameLogistics) {
        String id = "" + count;
        gamePool.put(id, gameLogistics);

        return id;
    }

    public GameLogistics getGameLogistics(String gameId) {
        return gamePool.get(gameId);
    }

    public Board getBoard(String gameId) throws GameNotStartedException {
        return gamePool.get(gameId).getBoard();
    }

}
