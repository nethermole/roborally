package com.nethermole.roborally.game.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private int x;
    private int y;

    public Coordinate moveForward1(Direction facing) {
        Coordinate endPosition = new Coordinate(x, y);
        switch(facing){
            case UP:
                endPosition.setY(endPosition.y-1);
                break;
            case RIGHT:
                endPosition.setX(endPosition.x+1);
                break;
            case DOWN:
                endPosition.setY(endPosition.y+1);
                break;
            case LEFT:
                endPosition.setX(endPosition.x-1);
                break;
        }
        return endPosition;
    }

    public Coordinate moveBackward1(Direction facing) {
        Coordinate endPosition = new Coordinate(x, y);
        switch(facing){
            case UP:
                endPosition.setY(endPosition.y+1);
                break;
            case RIGHT:
                endPosition.setX(endPosition.x-1);
                break;
            case DOWN:
                endPosition.setY(endPosition.y-1);
                break;
            case LEFT:
                endPosition.setX(endPosition.x+1);
                break;
        }
        return endPosition;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
