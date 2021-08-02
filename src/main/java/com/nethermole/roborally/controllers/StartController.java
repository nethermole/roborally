package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gameservice.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StartController {

    @Autowired
    GameLogistics gameLogistics;

    @PostMapping("/start")
    public void startGame() {
        int playerCount = 1;

        if (!gameLogistics.isGameAlreadyStarted()) {
            Map<Integer, Player> players = new HashMap<>();
            for (int i = 0; i < playerCount; i++) {
                players.put(i, new HumanPlayer(i));
            }

            gameLogistics.startGame(players);
            System.out.println("New game started");
        } else {
            System.out.println("Game in progress, no new game started");
        }
    }

    @PostMapping("/debugStart")
    public void debugStart(){
        Map<Integer, Player> players = new HashMap<>();
        players.put(0, new HumanPlayer(0));
        players.put(1, new RandomBot(1));
        gameLogistics.startGame(players);
        System.out.println("New game started");
    }

}