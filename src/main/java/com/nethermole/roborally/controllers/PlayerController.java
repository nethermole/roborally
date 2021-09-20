package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/player")
    public Collection<Player> getPlayers() {
        if(gameLogistics.getPlayers() != null && !gameLogistics.getPlayers().isEmpty()) {
            return gameLogistics.getPlayers().values();
        } else{
            return new ArrayList<>();
        }
    }

    @PostMapping("/player/{id}/submitHand")
    public void setCards(@PathVariable("id") Integer id, @RequestBody ArrayList<MovementCard> movementCardList) {
        for (MovementCard movementCard : movementCardList) {
            System.out.println("Player" + id + ": " + movementCard);
        }
        gameLogistics.submitHand(id, movementCardList);
    }

    @GetMapping("/player/{id}/getHand")
    public List<MovementCard> getCards(@PathVariable("id") Integer id) {
        List<MovementCard> movementCards = gameLogistics.getHand(id);
        return movementCards;
    }
}
