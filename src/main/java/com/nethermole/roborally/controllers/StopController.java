package com.nethermole.roborally.controllers;

import com.nethermole.roborally.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopController {

    @Autowired
    GameLogistics gameLogistics;

    @PutMapping("stop")
    public void stop() {
        gameLogistics.stopGame();
    }


}
