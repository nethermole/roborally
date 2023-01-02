package com.nethermole.roborally.gamepackage.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    private int x;
    private int y;

    public Position(Position original) {
        this.x = original.x;
        this.y = original.y;
    }

    public Position moveForward(Direction facing, int distance) {
        Position endPosition = new Position(x, y);
        switch (facing) {
            case UP:
                endPosition.setY(endPosition.y + distance);
                break;
            case RIGHT:
                endPosition.setX(endPosition.x + distance);
                break;
            case DOWN:
                endPosition.setY(endPosition.y - distance);
                break;
            case LEFT:
                endPosition.setX(endPosition.x - distance);
                break;
        }
        return endPosition;
    }

    public Position moveBackward1(Direction facing) {
        Position endPosition = new Position(x, y);
        switch (facing) {
            case UP:
                endPosition.setY(endPosition.y - 1);
                break;
            case RIGHT:
                endPosition.setX(endPosition.x - 1);
                break;
            case DOWN:
                endPosition.setY(endPosition.y + 1);
                break;
            case LEFT:
                endPosition.setX(endPosition.x + 1);
                break;
        }
        return endPosition;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Position){
            Position otherPosition = (Position) other;
            if(x != otherPosition.x){
                return false;
            }
            if(y != otherPosition.y){
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
