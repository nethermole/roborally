package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameConfig;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.CountingBot;
import com.nethermole.roborally.gamepackage.player.bot.MinimalTurnBot;
import com.nethermole.roborally.gamepackage.player.bot.TurnRateLimiterBot;
import com.nethermole.roborally.gameservice.GamePoolService;
import com.nethermole.roborally.view.BasicView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class StartController {

    private static Logger log = LogManager.getLogger(StartController.class);

    @Autowired
    GamePoolService gamePoolService;

    @PostMapping("/playerStart")
    public String playerStart(@RequestParam(required = false) Long seedIn, @RequestBody GameConfig gameConfig) {
        String gameId = gamePoolService.startPlayerGame(seedIn, gameConfig);
        return gameId;
    }

    @PostMapping("/botStart")
    public String botStart(@RequestParam(required = false) Long seedIn) {
        String gameId = gamePoolService.startBotGame(seedIn);
        return gameId;
    }

    @PostMapping("/botcivstart")
    public String botCivStart(@RequestParam(required = false) Long seedIn){
        /*int numplayers = 80;

        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        for(int i = 0; i < 80; i++){
            players.put(i, new MinimalTurnBot(i));
        }
        players.put(numplayers, new TurnRateLimiterBot(numplayers));
        GameLogistics gameLogistics = new GameLogistics(players);
        gamePoolService.addGameLogisticsToPool(gameLogistics);

        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.board_empty();
        for(int x = 0; x < 1600/16; x++){
            for(int y = 0; y < 1200/16; y++){
                if(x == 0 && y == 0){
                    break;
                }
                board.addBoard(boardFactory.board_exchange(), x, y);
            }
        }

        gameLogistics.startGame(seed, board);

        try {
            BasicView basicView = new BasicView(5);
            basicView.setBoard(gameLogistics.getBoard());
            for (Player player : players.values()) {
                basicView.getWindow().getCanvas().addPlayer(player);
            }
            Thread thread = new Thread(basicView);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (gameLogistics.getGamestateVerifier().isGameReadyToProcessTurn(gameLogistics.getGame())) {
            gameLogistics.getGame().processTurn();
            gameLogistics.getGame().setupForNextTurn();
        }

        return gameLogistics.getUuid();*/
        return "";
    }


}