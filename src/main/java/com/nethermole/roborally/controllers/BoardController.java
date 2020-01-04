package com.nethermole.roborally.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nethermole.roborally.Gamemaster;
import com.nethermole.roborally.exceptions.GameNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    Gamemaster gamemaster;

    @GetMapping("/board")
    public String getBoard() throws Exception{
        try {
            return (new ObjectMapper().writeValueAsString(gamemaster.getBoard()));
        } catch(GameNotStartedException e){
            return "GameNotStartedYet";
        }
    }

}
