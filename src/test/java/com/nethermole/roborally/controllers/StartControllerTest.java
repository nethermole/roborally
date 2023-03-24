package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gameservice.GamePoolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartControllerTest {

    @InjectMocks
    StartController startController = new StartController();

    @Mock
    GamePoolService gamePoolService;


    @Test
    void startGame_startsNewGame() {
        startController.debugStart(0L);
    }

    @Test
    void startGame_gameAlreadyStarted() {
        startController.debugStart(0L);
    }
}