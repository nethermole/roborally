package com.nethermole.roborally.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.gameservice.GameLogistics;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/board")
    public String getBoard() throws Exception {
        try {
            return (new ObjectMapper().writeValueAsString(gameLogistics.getBoard()));
        } catch (GameNotStartedException e) {
            return "GameNotStartedYet";
        }
    }

}
