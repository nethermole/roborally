package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandProcessor {

    Map<MovementCard, String> playerIdByMovementCard;

    public PlayerHandProcessor() {
        playerIdByMovementCard = new HashMap<>();
    }

    public List<List<MovementCard>> submitHands(Map<String, List<MovementCard>> inputHands) {
        List<List<MovementCard>> movementCardsInTurn = new ArrayList<>();
        for (int i = 0; i < GameConstants.PHASES_PER_TURN; i++) {
            List<MovementCard> movementCardsInPhase = new ArrayList<>();
            for (String playerId : inputHands.keySet()) {
                MovementCard movementCard = inputHands.get(playerId).get(i);
                movementCardsInPhase.add(movementCard);

                if (playerIdByMovementCard.containsKey(movementCard)) {
                    throw new IllegalStateException("Duplicate card detected on card submission");
                }
                playerIdByMovementCard.put(movementCard, playerId);
            }
            movementCardsInPhase.sort(Comparator.comparing(MovementCard::getPriority).reversed());
            movementCardsInTurn.add(movementCardsInPhase);
        }
        return movementCardsInTurn;
    }

    public String getPlayerWhoSubmittedCard(MovementCard movementCard) {
        return playerIdByMovementCard.get(movementCard);
    }

}
