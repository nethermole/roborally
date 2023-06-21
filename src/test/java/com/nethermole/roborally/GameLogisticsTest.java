package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameLogisticsTest {

    GameLogistics gameLogistics;
    Player player0;

    @BeforeEach
    public void setup() {
        player0 = new HumanPlayer(0);

        Map<Integer, Player> players = new HashMap<>();
        players.put(0, player0);

        gameLogistics = new GameLogistics(players);
    }


    @Test
    public void isGameAlreadyStarted_gameIsNull_returnsFalse() {
        assertThat(gameLogistics.getGame()).isNull();
    }

    @Test
    public void startGame() throws Exception {
        gameLogistics.startGameWithDefaultBoard((new Random()).nextLong());
        assertThat(gameLogistics.getGame().getPlayers()).containsValue(player0);
    }

}