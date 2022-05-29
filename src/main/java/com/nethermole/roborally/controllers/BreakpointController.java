package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BreakpointController {

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/debug")
    public void debug(){
        System.out.println();
    }

}