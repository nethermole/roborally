package com.nethermole.roborally.gamepackage.board;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public static Direction turnRight(Direction original) {
        switch (original) {
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            default:
                throw new RuntimeException("hit default case in direction turnRight");
        }
    }

    public static Direction getOpposite(Direction original){
        switch (original) {
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                throw new RuntimeException("hit default case in direction getOpposite");
        }
    }

    public static Direction turnLeft(Direction original) {
        switch (original) {
            case UP:
                return LEFT;
            case RIGHT:
                return UP;
            case DOWN:
                return RIGHT;
            case LEFT:
                return DOWN;
            default:
                throw new RuntimeException("hit default case in direction turnLeft");
        }
    }
}
