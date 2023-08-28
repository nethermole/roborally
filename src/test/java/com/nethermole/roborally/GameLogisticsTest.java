package com.nethermole.roborally;

import com.nethermole.roborally.exceptions.InvalidSubmittedHandException;
import com.nethermole.roborally.gamepackage.GameConfig;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GameLogisticsTest {

    GameLogistics gameLogistics;
    Player player0;

    @BeforeEach
    public void setup() {
        gameLogistics = new GameLogistics(0L, new GameConfig(2,3));
    }


    @Test
    public void isGameAlreadyStarted_gameIsNull_returnsFalse() {
        assertThat(gameLogistics.getGame()).isNull();
    }

    @Test
    public void startGame() throws Exception {
        gameLogistics.startGameWithDefaultBoard();
        assertThat(gameLogistics.getGame().getPlayers()).hasSize(5);
    }


//todo:
//    @Test
//    public void submitPlayerHand_humanPlayerSubmittingInvalidCard_throwsInvalidHandException() {
//        assertThrows(InvalidSubmittedHandException.class, () -> game.submitPlayerHand(0, game.getHand(1).subList(0, 5)));
//    }

}