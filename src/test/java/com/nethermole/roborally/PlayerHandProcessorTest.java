package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerHandProcessorTest {

    PlayerHandProcessor playerHandProcessor;

    @BeforeEach
    public void setup(){
        playerHandProcessor = new PlayerHandProcessor();
    }

    @Test
    void submitHands_1Player() {
        List<MovementCard> hand = Arrays.asList(
                new MovementCard(Movement.TURN_RIGHT, 400),
                new MovementCard(Movement.MOVE3, 820),
                new MovementCard(Movement.TURN_RIGHT, 100),
                new MovementCard(Movement.UTURN, 30),
                new MovementCard(Movement.TURN_RIGHT, 100)
        );
        Map<Integer, List<MovementCard>> inputHands = new HashMap<>();
        inputHands.put(0, hand);

        List<List<MovementCard>> actualMovementCardsInTurn = playerHandProcessor.submitHands(inputHands);
        List<List<MovementCard>> expectedMovementCardsInTurn = Arrays.asList(
                Arrays.asList(new MovementCard(Movement.TURN_RIGHT, 400)),
                Arrays.asList(new MovementCard(Movement.MOVE3, 820)),
                Arrays.asList(new MovementCard(Movement.TURN_RIGHT, 100)),
                Arrays.asList(new MovementCard(Movement.UTURN, 30)),
                Arrays.asList(new MovementCard(Movement.TURN_RIGHT, 100))
        );

        assertThat(actualMovementCardsInTurn).isEqualTo(expectedMovementCardsInTurn);
    }

    @Test
    void submitHands_handles2Players() {
        List<MovementCard> hand0 = Arrays.asList(
                new MovementCard(Movement.TURN_RIGHT, 400),
                new MovementCard(Movement.MOVE3, 820),
                new MovementCard(Movement.TURN_RIGHT, 100),
                new MovementCard(Movement.MOVE3, 640),
                new MovementCard(Movement.TURN_RIGHT, 100)
        );

        List<MovementCard> hand1 = Arrays.asList(
                new MovementCard(Movement.TURN_LEFT, 290),
                new MovementCard(Movement.TURN_LEFT, 110),
                new MovementCard(Movement.MOVE3, 830),
                new MovementCard(Movement.UTURN, 30),
                new MovementCard(Movement.MOVE1, 620)
        );

        Map<Integer, List<MovementCard>> inputHands = new HashMap<>();
        inputHands.put(0, hand0);
        inputHands.put(1, hand1);

        List<List<MovementCard>> actualMovementCardsInTurn = playerHandProcessor.submitHands(inputHands);
        List<List<MovementCard>> expectedMovementCardsInTurn = Arrays.asList(
                Arrays.asList(new MovementCard(Movement.TURN_RIGHT, 400), new MovementCard(Movement.TURN_LEFT, 290)),
                Arrays.asList(new MovementCard(Movement.MOVE3, 820), new MovementCard(Movement.TURN_LEFT, 110)),
                Arrays.asList(new MovementCard(Movement.MOVE3, 830),new MovementCard(Movement.TURN_RIGHT, 100)),
                Arrays.asList(new MovementCard(Movement.MOVE3, 640),new MovementCard(Movement.UTURN, 30)),
                Arrays.asList( new MovementCard(Movement.MOVE1, 620), new MovementCard(Movement.TURN_RIGHT, 100))
        );

        assertThat(actualMovementCardsInTurn).isEqualTo(expectedMovementCardsInTurn);
    }

    @Test
    void getPlayerWhoSubmittedCard_duplicateCard_throwsException() {
        List<MovementCard> hand0 = Arrays.asList(
                new MovementCard(Movement.TURN_RIGHT, 400),
                new MovementCard(Movement.MOVE3, 820),          //duplicate
                new MovementCard(Movement.TURN_RIGHT, 100),
                new MovementCard(Movement.MOVE3, 640),
                new MovementCard(Movement.TURN_RIGHT, 100)
        );

        List<MovementCard> hand1 = Arrays.asList(
                new MovementCard(Movement.TURN_LEFT, 290),
                new MovementCard(Movement.TURN_LEFT, 110),
                new MovementCard(Movement.MOVE3, 820),          //duplicate
                new MovementCard(Movement.UTURN, 30),
                new MovementCard(Movement.MOVE1, 620)
        );

        Map<Integer, List<MovementCard>> inputHands = new HashMap<>();
        inputHands.put(0, hand0);
        inputHands.put(1, hand1);

        assertThrows(IllegalStateException.class, () -> playerHandProcessor.submitHands(inputHands), "Duplicate card detected on card submission");
    }
}