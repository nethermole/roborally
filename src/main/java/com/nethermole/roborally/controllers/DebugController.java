package com.nethermole.roborally.controllers;

import com.nethermole.roborally.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/debug")
    public void debug() throws Exception {
        System.out.println();
    }
}
