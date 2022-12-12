package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.gamepackage.player.Player;

public class GameEndViewStep extends ViewStep {

    Player winningPlayer;

    GameEndViewStep(Player winningPlayer){
        this.setTypeName(this.getClass().getName());

        this.winningPlayer = winningPlayer;
    }

}
