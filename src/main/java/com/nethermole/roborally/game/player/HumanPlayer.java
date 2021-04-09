package com.nethermole.roborally.game.player;

import com.nethermole.roborally.game.board.Direction;

public class HumanPlayer extends Player{

    public HumanPlayer(int id){
        this.id = id;
        this.setFacing(Direction.UP);
    }

    @Override
    public void giveCards() {

    }

    @Override
    public void getCards() {

    }
}
