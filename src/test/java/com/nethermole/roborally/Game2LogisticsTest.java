package com.nethermole.roborally;

import com.nethermole.roborally.gamepackage.Game;
import com.nethermole.roborally.gamepackage.player.HumanPlayer;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class Game2LogisticsTest {

    @InjectMocks
    GameLogistics gameLogistics = new GameLogistics();

    @Mock
    Game game;

    @Test
    public void isGameAlreadyStarted_gameIsNull_returnsFalse() {
        gameLogistics = new GameLogistics();
        assertThat(gameLogistics.getGame()).isNull();
    }

    @Test
    public void startGame() throws Exception {
        Player player0 = new HumanPlayer(0);
        Map<Integer, Player> players = new HashMap<>();
        players.put(0, player0);
        gameLogistics.startGame(players);

        assertThat(gameLogistics.getPlayers().containsValue(player0));
    }

}