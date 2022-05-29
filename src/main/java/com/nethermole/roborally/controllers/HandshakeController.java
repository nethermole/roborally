package com.nethermole.roborally.controllers;

import com.nethermole.roborally.PlayerUpdate;
import com.nethermole.roborally.UpdateCheck;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandshakeController {

    @Autowired
    GameLogistics gameLogistics;

    @PostMapping("/checkUpdate")
    public PlayerUpdate checkForUpdate(UpdateCheck updateCheck){
        if(gameLogistics.isWaitingOnPlayer(updateCheck.getPlayerId())){
            return new PlayerUpdate("Waiting for hand submission");
        }

        return new PlayerUpdate("No update");
    }
}
