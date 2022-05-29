package com.nethermole.roborally.controllers;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.gamepackage.ViewUpdate;
import com.nethermole.roborally.gamepackage.GameLogistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewUpdateController {

    private static Logger log = LogManager.getLogger(StopController.class);

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/viewupdate/turn/{turn}")
    public ViewUpdate getViewUpdate(@PathVariable("turn") Integer turn) {
        log.info("getViewUpdate() called");

        StartInfo startInfo = null;
        if(gameLogistics.isGameAboutToStart()){
            startInfo = gameLogistics.getStartInfo();
        }
        ViewUpdate viewUpdate = new ViewUpdate();
        viewUpdate.setStartInfo(startInfo);
        viewUpdate.setViewSteps(gameLogistics.getViewstepsByTurn(turn));
        return viewUpdate;
    }


}
