package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Direction;

//Left of the tile
public class Conveyor extends Element {

    Direction inputDirection;
    Direction outputDirection;
    int speed;

    public Conveyor(Direction direction) {
        this(direction, 1);
    }

    public Conveyor(Direction direction, int speed) {
        inputDirection = Direction.getOpposite(direction);
        outputDirection = direction;
        this.speed = speed;
    }

    @Override
    public ElementEnum getElementEnum() {
        String enumName = "CONVEYOR_" + speed + "_" + inputDirection + "_" + outputDirection;
        return ElementEnum.valueOf(enumName);
    }
}
