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

    public Conveyor(Direction inputDirection, Direction outputDirection) {
        this(inputDirection, outputDirection, 1);
    }

    public Conveyor(Direction inputDirection, Direction outputDirection, int speed) {
        this.inputDirection = inputDirection;
        this.outputDirection = outputDirection;
        this.speed = speed;
    }

    @Override
    public ElementEnum getElementEnum() {
        String enumName = "CONVEYOR_" + speed + "_" + inputDirection + "_" + outputDirection;
        return ElementEnum.valueOf(enumName);
    }

    public RuntimeException createRuntimeExceptionForElementEnum() {
        return new RuntimeException("invalid elementEnum for conveyor: " + speed + "," + inputDirection + "," + outputDirection);
    }
}
