package com.nethermole.roborally.gamepackage.player;

import com.nethermole.roborally.gamepackage.board.Direction;

public class HumanPlayer extends Player {

    public HumanPlayer(String id) {
        super(id);

        //below is temporary until managed by a different object
        this.setDirection(Direction.UP);
        this.setDisplayName("Human");
    }
}
