package com.nethermole.roborally.controllers;

import com.nethermole.roborally.GameLogistics;
import com.nethermole.roborally.game.player.HumanPlayer;
import com.nethermole.roborally.game.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StartController {

    @Autowired
    GameLogistics gameLogistics;

    @PostMapping("/start")
    public void startGame() {
        if (!gameLogistics.isGameAlreadyStarted()) {
            Map<Integer, Player> players = new HashMap<>();
            players.put(0, new HumanPlayer(0));
            gameLogistics.startGame(players);
            System.out.println("New game started");
        } else {
            System.out.println("Game in progress, no new game started");
        }
    }

}