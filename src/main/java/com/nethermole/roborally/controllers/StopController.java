package com.nethermole.roborally.controllers;

import com.nethermole.roborally.Gamemaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopController {

    @Autowired
    Gamemaster gamemaster;

    @PutMapping("stop")
    public void stop(){
        gamemaster.stopGame();
    }


}
