package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandProcessor {

    Map<MovementCard, Integer> playerIdByMovementCard;

    public PlayerHandProcessor() {
        playerIdByMovementCard = new HashMap<>();
    }

    public List<List<MovementCard>> submitHands(Map<Integer, List<MovementCard>> inputHands) {
        List<List<MovementCard>> movementCardsInTurn = new ArrayList<>();
        for (int i = 0; i < GameConstants.PHASES_PER_TURN; i++) {
            List<MovementCard> movementCardsInPhase = new ArrayList<>();
            for (Integer player : inputHands.keySet()) {
                MovementCard movementCard = inputHands.get(player).get(i);
                movementCardsInPhase.add(movementCard);

                if(playerIdByMovementCard.containsKey(movementCard)){ throw new IllegalStateException("Duplicate card detected on card submission"); }
                playerIdByMovementCard.put(movementCard, player);
            }
            movementCardsInPhase.sort(Comparator.comparing(MovementCard::getPriority).reversed());
            movementCardsInTurn.add(movementCardsInPhase);
        }
        return movementCardsInTurn;
    }

    public int getPlayerWhoSubmittedCard(MovementCard movementCard){
        return playerIdByMovementCard.get(movementCard);
    }

}
