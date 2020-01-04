package com.nethermole.roborally.game;

public class HumanPlayer extends Player{

    public HumanPlayer(int id){
        this.id = id;
        robot = new Robot();
        robot.setLocation(new Coordinate(4, 4));
        robot.setFacing(Direction.RIGHT);
    }

    @Override
    public void giveCards() {

    }

    @Override
    public void getCards() {

    }
}
