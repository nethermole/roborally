package com.nethermole.roborally.controllers;

import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @InjectMocks
    BoardController boardController = new BoardController();

    @Mock
    GameLogistics gameLogistics;

    @Test
    public void getBoard() throws Exception {
        GameLogistics gameLogistics = new GameLogistics();
        gameLogistics.startGame(new HashMap<>(), (new Random()).nextLong());
        when(this.gameLogistics.getBoard()).thenReturn(gameLogistics.getBoard());


        String result = boardController.getBoard();
        assertThat(result).contains("{\"players\":[],\"squares\":{\"0\":{\"0\":{\"elements\":[{");
    }

    @Test
    public void getBoard_gameNotStartedException() throws Exception {
        when(gameLogistics.getBoard()).thenThrow(new GameNotStartedException());

        String result = boardController.getBoard();
        assertThat(result).isEqualTo("GameNotStartedYet");
    }
}