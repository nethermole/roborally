package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.player.Player;
import com.nethermole.roborally.gamepackage.viewStep.ViewStep;

public class GameEndViewStep extends ViewStep {

    Player winningPlayer;

    GameEndViewStep(Player winningPlayer){
        this.setTypeName(this.getClass().getName());

        this.winningPlayer = winningPlayer;
    }

}
