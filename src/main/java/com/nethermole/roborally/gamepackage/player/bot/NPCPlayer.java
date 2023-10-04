package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;

import java.util.List;

public abstract class NPCPlayer extends Player {

    public NPCPlayer(String id) {
        super(id);
    }

    //restrict to public information at some point
    //add more parameters as necessary
    public abstract List<MovementCard> chooseCards(List<MovementCard> movementCards, Board board);

}
