package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TurnRateLimiterBot extends NPCPlayer {

    private static final Logger log = LogManager.getLogger(TurnRateLimiterBot.class);

    public TurnRateLimiterBot(int id) {
        super(id);
        this.setDisplayName("RB-" + id);
    }

    @Override
    public List<MovementCard> chooseCards(List<MovementCard> movementCards, Board board) {
        try {
            Thread.sleep(10);
        } catch (Exception e){
            log.info("Exception caught in TurnRateLimiterBot chooseCards() in Thread.sleep()");
        }
        return null;
    }
}
