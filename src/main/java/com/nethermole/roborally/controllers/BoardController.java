package com.nethermole.roborally.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    GameLogistics gameLogistics;

    private static Logger log = LogManager.getLogger(BoardController.class);

    @GetMapping("/board")
    public String getBoard() throws Exception {
        log.debug("getBoard() called");
        try {
            return (new ObjectMapper().writeValueAsString(gameLogistics.getBoard()));
        } catch (GameNotStartedException e) {
            return "GameNotStartedYet";
        }
    }

}
