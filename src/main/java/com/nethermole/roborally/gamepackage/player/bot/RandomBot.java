package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;

import java.util.List;

public class RandomBot extends NPCPlayer {

    public RandomBot(int id) {
        super(id);
        this.setName("RB-" + id);
    }

    @Override
    public List<MovementCard> chooseCards(List<MovementCard> movementCards, Board board) {
        return movementCards.subList(0, 5);
    }
}
