package com.nethermole.roborally.game.player;

import com.nethermole.roborally.game.board.Direction;

public class HumanPlayer extends Player{

    public HumanPlayer(int id){
        //todo player ids are managed weird... look into that
        this.id = id;

        //below is temporary until managed by a different object
        this.setFacing(Direction.UP);
    }
}
