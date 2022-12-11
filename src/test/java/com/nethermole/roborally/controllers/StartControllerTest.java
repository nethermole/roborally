package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartControllerTest {

    @InjectMocks
    StartController startController = new StartController();

    @Mock
    GameLogistics gameLogistics;

    @Test
    void startGame_startsNewGame() {
        when(gameLogistics.isGameAlreadyStarted()).thenReturn(false);
        startController.startGame(0L);
    }

    @Test
    void startGame_gameAlreadyStarted() {
        when(gameLogistics.isGameAlreadyStarted()).thenReturn(true);
        startController.startGame(0L);
    }
}