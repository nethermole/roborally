package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.BoardFactory;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.CountingBot;
import com.nethermole.roborally.gamepackage.player.bot.MinimalTurnBot;
import com.nethermole.roborally.gamepackage.player.bot.OvershootBot;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gamepackage.player.bot.TurnRateLimiterBot;
import com.nethermole.roborally.gameservice.GamePoolService;
import com.nethermole.roborally.view.BasicView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/debugStart")
    public void debugStart(@RequestParam(required = false) Long seedIn) {
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        players.put(0, new HumanPlayer(0));
        players.put(1, new OvershootBot(1));
        players.put(2, new CountingBot(2));
        players.put(3, new RandomBot(3));
        GameLogistics gameLogistics = new GameLogistics(players);


        gameLogistics.startGameWithDefaultBoard(seed);

        gamePoolService.addGameLogistics(gameLogistics);
    }

    @PostMapping("/botStart")
    public String botStart(@RequestParam(required = false) Long seedIn) {
        int numplayers = 4;

        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        for (int i = 0; i < numplayers; i++) {
            players.put(i, new CountingBot(i));
        }
        players.put(numplayers, new TurnRateLimiterBot(numplayers));
        GameLogistics gameLogistics = new GameLogistics(players);
        gamePoolService.addGameLogistics(gameLogistics);

        gameLogistics.startGameWithDefaultBoard(seed);

//        try {
//            BasicView basicView = new BasicView(5);
//            basicView.setBoard(gameLogistics.getBoard());
//            for (Player player : players.values()) {
//                basicView.getWindow().getCanvas().addPlayer(player);
//            }
//            Thread thread = new Thread(basicView);
//            thread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        while (gameLogistics.getGame().isReadyToProcessTurn()) {
            gameLogistics.getGame().processTurn();
            gameLogistics.getGame().setupForNextTurn();
        }

        return gameLogistics.getUuid();
    }

    @PostMapping("/botcivstart")
    public String botCivStart(@RequestParam(required = false) Long seedIn){
        int numplayers = 80;

        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        for(int i = 0; i < 80; i++){
            players.put(i, new MinimalTurnBot(i));
        }
        /*for (int i = 0; i < 20; i++) {
            players.put(i, new CountingBot(i));
            players.get(i).setColor(Color.red);
        }
        for (int i = 20; i < 40; i++) {
            players.put(i, new MinimalTurnBot(i));
            players.get(i).setColor(Color.blue);
        }
        for (int i = 40; i < 60; i++) {
            players.put(i, new OvershootBot(i));
            players.get(i).setColor(new Color(0,153,0));
        }
        for (int i = 60; i < 80; i++) {
            players.put(i, new RandomBot(i));
            players.get(i).setColor(Color.MAGENTA);
        }*/
        players.put(numplayers, new TurnRateLimiterBot(numplayers));
        GameLogistics gameLogistics = new GameLogistics(players);
        gamePoolService.addGameLogistics(gameLogistics);

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

        for(Player player : players.values()){
            board.addPlayer(player);
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

        while (gameLogistics.getGame().isReadyToProcessTurn()) {
            gameLogistics.getGame().processTurn();
            gameLogistics.getGame().setupForNextTurn();
        }

        return gameLogistics.getUuid();
    }


}