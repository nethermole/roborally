package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Direction;

public class Wall extends Element {

    Direction side;
    boolean laserAttachment;

    public Wall(Direction direction) {
        this.side = direction;
        laserAttachment = false;
    }

    public Wall(Direction direction, boolean laserAttachment) {
        this.side = direction;
        this.laserAttachment = laserAttachment;
    }

    @Override
    public ElementEnum getElementEnum() {
        switch (side) {
            case UP:
                return ElementEnum.WALL_UP;
            case RIGHT:
                return ElementEnum.WALL_RIGHT;
            case DOWN:
                return ElementEnum.WALL_DOWN;
            case LEFT:
                return ElementEnum.WALL_LEFT;
            default:
                throw new RuntimeException("no elementEnum found for wall - this should never happen");
        }

    }
}
