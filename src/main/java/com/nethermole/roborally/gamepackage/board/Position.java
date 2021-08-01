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

    public Position moveForward1(Direction facing) {
        Position endPosition = new Position(x, y);
        switch (facing) {
            case UP:
                endPosition.setY(endPosition.y + 1);
                break;
            case RIGHT:
                endPosition.setX(endPosition.x + 1);
                break;
            case DOWN:
                endPosition.setY(endPosition.y - 1);
                break;
            case LEFT:
                endPosition.setX(endPosition.x - 1);
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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
