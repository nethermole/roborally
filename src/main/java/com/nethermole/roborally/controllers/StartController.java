package com.nethermole.roborally.controllers;

import com.nethermole.roborally.Gamemaster;
import com.nethermole.roborally.game.player.HumanPlayer;
import com.nethermole.roborally.game.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class StartController {

    @Autowired
    Gamemaster gamemaster;

    @PostMapping("/start")
    public void startGame(){
        if(gamemaster.getGame() == null) {
            List<Player> players = new ArrayList();
            players.add(new HumanPlayer(0));
            gamemaster.startGame(new HashMap<>(), players);
            System.out.println("started");
        }
    }

}