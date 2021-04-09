package com.nethermole.roborally.game.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private int x;
    private int y;

    public boolean isAboveAdjacentTo(Coordinate coordinate){
        if(this.getX() != coordinate.getX()){
            return false;
        }
        if(this.getY() - coordinate.getY() != 1){
            return false;
        }
        return true;
    }

    public boolean isRightAdjacentTo(Coordinate coordinate){
        if(this.getY() != coordinate.getY()){
            return false;
        }
        if(this.getX() - coordinate.getX() != 1){
            return false;
        }
        return true;
    }

    public boolean isBelowAdjacentTo(Coordinate coordinate){
        return coordinate.isAboveAdjacentTo(this);
    }
    public boolean isLeftAdjacentTo(Coordinate coordinate){
        return coordinate.isRightAdjacentTo(this);
    }


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
