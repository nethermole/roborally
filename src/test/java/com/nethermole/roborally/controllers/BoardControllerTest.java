package com.nethermole.roborally.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.player.bot.RandomBot;
import com.nethermole.roborally.gameservice.GamePoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @InjectMocks
    BoardController boardController = new BoardController();

    @Spy
    ObjectMapper objectMapper;

    GamePoolService gamePoolService;

    GameLogistics gameLogistics;

    @BeforeEach
    public void setup() {
        gamePoolService = new GamePoolService();
        boardController.gamePoolService = gamePoolService;

        gamePoolService.init();

        Map<Integer, Player> players = new HashMap<>();
        players.put(0, new RandomBot(0));
        gameLogistics = new GameLogistics(players);

        boardController.gamePoolService = gamePoolService;
        gamePoolService.addGameLogistics(gameLogistics);
    }

    @Test
    public void getBoard_getsBoard() throws Exception {
        gameLogistics.startGameWithDefaultBoard((new Random()).nextLong());

        String result = boardController.getBoard(0);
        assertThat(result).contains("{\"players\":[],\"squares\":{\"0\":{\"0\":{\"elements\":[{");
    }

    @Test
    public void getBoard_gameNotStarted_throwsException() throws Exception {
        String result = boardController.getBoard(0);
        assertThat(result).contains("GameNotStartedYet");
    }
}