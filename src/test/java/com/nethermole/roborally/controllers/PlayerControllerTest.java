package com.nethermole.roborally.controllers;

import com.nethermole.roborally.exceptions.InvalidPlayerStateException;
import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gameservice.GamePoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @InjectMocks
    PlayerController playerController = new PlayerController();

    @Mock
    GameLogistics spyGameLogistics;

    @BeforeEach
    public void setup() {
        playerController.gamePoolService = new GamePoolService();
        playerController.gamePoolService.init();

        Map<Integer, Player> players = new HashMap<>();
        players.put(0, new RandomBot(0));

        playerController.gamePoolService.addGameLogistics(spyGameLogistics);
    }

    @Test
    void setCards_throwsNoException() throws InvalidSubmittedHandException, InvalidPlayerStateException {
        spyGameLogistics.submitHand(1, new ArrayList<>());
    }

    @Test
    void getCards() throws InvalidPlayerStateException {
        ArrayList<MovementCard> movementCardList = new ArrayList<>();
        movementCardList.add(new MovementCard(Movement.MOVE1, 100));
        movementCardList.add(new MovementCard(Movement.MOVE2, 200));
        movementCardList.add(new MovementCard(Movement.MOVE3, 300));

        when(spyGameLogistics.getHand(1)).thenReturn(movementCardList);

        List<MovementCard> result = playerController.getCards(0, 1);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.indexOf(movementCardList.get(2))).isEqualTo(2);
    }
}