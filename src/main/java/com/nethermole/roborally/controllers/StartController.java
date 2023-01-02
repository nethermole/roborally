package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gamepackage.GameLogistics;
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
    GameLogistics gameLogistics;

    @PostMapping("/start")
    public void startGame(@RequestParam(required = false) Long seedIn) {
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");


        int playerCount = 1;

        if (!gameLogistics.isGameAlreadyStarted()) {
            Map<Integer, Player> players = new HashMap<>();
            for (int i = 0; i < playerCount; i++) {
                players.put(i, new HumanPlayer(i));
            }

            gameLogistics.startGame(players, seed);
        } else {
            log.info("Game in progress, no new game started");
        }
    }

    @PostMapping("/debugStart")
    public void debugStart(@RequestParam(required = false) Long seedIn){
        long seed = seedIn != null ? seedIn : (new Random()).nextLong();
        log.info("startGame(" + seed + ") called");

        Map<Integer, Player> players = new HashMap<>();
        players.put(0, new HumanPlayer(0));
        players.put(1, new RandomBot(1));
        gameLogistics.startGame(players, seed);
    }

}