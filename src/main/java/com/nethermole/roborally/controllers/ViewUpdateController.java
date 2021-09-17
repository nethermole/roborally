package com.nethermole.roborally.controllers;

import com.nethermole.roborally.StartInfo;
import com.nethermole.roborally.gamepackage.ViewUpdate;
import com.nethermole.roborally.gameservice.GameLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewUpdateController {

    @Autowired
    GameLogistics gameLogistics;

    @GetMapping("/viewupdate/turn/{turn}")
    public ViewUpdate getViewUpdate(@PathVariable("turn") Integer turn) {
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
