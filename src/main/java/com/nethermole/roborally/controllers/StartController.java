package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.CountingBot;
import com.nethermole.roborally.gamepackage.player.bot.OvershootBot;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gameservice.GamePoolService;
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


        gameLogistics.startGame(seed);

        gamePoolService.addGameLogistics(gameLogistics);
    }

    @PostMapping("/botStart")
    public void botStart(@RequestParam(required = false) Long seedIn) {
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            players.put(i, new CountingBot(i));
        }
        GameLogistics gameLogistics = new GameLogistics(players);
        gamePoolService.addGameLogistics(gameLogistics);
        gameLogistics.startGame(seed);

        while (gameLogistics.getGame().isReadyToProcessTurn()) {
            gameLogistics.getGame().processTurn();
            gameLogistics.getGame().setupForNextTurn();
        }
    }

}