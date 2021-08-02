package com.nethermole.roborally.gamepackage.player;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;

import java.util.List;

public abstract class NPCPlayer extends Player {

    //restrict to public information at some point
    //add more parameters as necessary
    public abstract void chooseCards(List<MovementCard> movementCards);

}
