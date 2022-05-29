package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopController {

    private static Logger log = LogManager.getLogger(StopController.class);

    @Autowired
    GameLogistics gameLogistics;

    @PutMapping("stop")
    public void stop() {
        log.info("stop() called");
        gameLogistics.stopGame();
    }


}
