package com.nethermole.roborally.controllers;

import com.nethermole.roborally.GameLogistics;
import com.nethermole.roborally.game.deck.movement.MovementCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    GameLogistics gameLogistics;

    @PostMapping("/player/{id}/submitHand")
    public void setCards(@PathVariable("id") Integer id, @RequestBody ArrayList<MovementCard> movementCardList) {
        gameLogistics.submitHand(id, movementCardList);
    }

    @GetMapping("/player/{id}/getHand")
    public List<MovementCard> getCards(@PathVariable("id") Integer id) {
        List<MovementCard> movementCards = gameLogistics.getHand(id);
        System.out.println(movementCards);
        return movementCards;
    }
}
