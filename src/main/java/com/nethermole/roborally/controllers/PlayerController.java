package com.nethermole.roborally.controllers;

import com.nethermole.roborally.Gamemaster;
import com.nethermole.roborally.game.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.smartcardio.Card;
import java.util.List;
import java.util.Random;

@RestController
public class PlayerController {

    @Autowired
    Gamemaster gamemaster;

    @PostMapping
    public void giveCards(List<Card> cards){

    }

    @PutMapping("/direction")
    public void updateDirection(){
        int direction = (new Random()).nextInt(4);
        Direction direction1 = Direction.values()[direction];
        gamemaster.getPlayers().get(0).getRobot().setFacing(direction1);
    }
}
