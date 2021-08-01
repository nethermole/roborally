package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gameservice.GameLogistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StopControllerTest {

    @InjectMocks
    StopController stopController = new StopController();

    @Mock
    GameLogistics gameLogistics;

    @Test
    void stop() {
        stopController.stop();
    }
}