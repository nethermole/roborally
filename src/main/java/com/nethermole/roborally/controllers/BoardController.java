package com.nethermole.roborally.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import com.nethermole.roborally.gameservice.GamePoolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GamePoolService gamePoolService;

    private static Logger log = LogManager.getLogger(BoardController.class);

    @GetMapping("/board/{gameId}")
    public String getBoard(@PathVariable String gameId) throws Exception {
        log.debug("getBoard() called");
        try {
            return objectMapper.writeValueAsString(gamePoolService.getGameLogistics(gameId).getBoard());
        } catch (GameNotStartedException e) {
            return "GameNotStartedYet";
        } catch (NullPointerException e) {
            return "ugh";
        }
    }

}
