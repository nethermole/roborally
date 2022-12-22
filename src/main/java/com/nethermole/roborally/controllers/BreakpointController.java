package com.nethermole.roborally.controllers;

import com.nethermole.roborally.gamepackage.GameLogistics;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.board.element.Checkpoint;
import com.nethermole.roborally.gamepackage.board.element.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BreakpointController {

    @Autowired
    GameLogistics gameLogistics;

    private static Logger log = LogManager.getLogger(StartController.class);

    @GetMapping("/debug")
    public void debug(){
        if(gameLogistics.isGameAlreadyStarted() == false){
            log.info("game not started to get debugInfo from");
            return;
        }

        List<Map.Entry<Element, Position>> checkpointList = gameLogistics.getGame().getBoard().getAllElementsOfType(Checkpoint.class);
        log.info("Checkpoint List: " + checkpointList);
        System.out.println();
    }

}
