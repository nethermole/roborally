package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gameservice.GameLogistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @InjectMocks
    PlayerController playerController = new PlayerController();

    @Mock
    GameLogistics gameLogistics;

    @Test
    void setCards_throwsNoException() {
        gameLogistics.submitHand(1, new ArrayList<>());
    }

    @Test
    void getCards() {
        ArrayList<MovementCard> movementCardList = new ArrayList<>();
        movementCardList.add(new MovementCard(Movement.MOVE1, 100));
        movementCardList.add(new MovementCard(Movement.MOVE2, 200));
        movementCardList.add(new MovementCard(Movement.MOVE3, 300));

        when(gameLogistics.getHand(1)).thenReturn(movementCardList);

        List<MovementCard> result = playerController.getCards(1);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.indexOf(movementCardList.get(2))).isEqualTo(2);
    }
}