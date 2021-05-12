package com.nethermole.roborally;

import com.nethermole.roborally.game.Game;
import com.nethermole.roborally.game.player.HumanPlayer;
import com.nethermole.roborally.game.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameLogisticsTest {

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