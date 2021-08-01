package com.nethermole.roborally.gamepackage.board.element;

public class Laser extends Element {

    Orientation orientation;
    int damage;

    public Laser(Orientation orientation) {
        this(orientation, 1);
    }

    public Laser(Orientation orientation, int damage) {
        this.orientation = orientation;
        this.damage = damage;
    }

    @Override
    public ElementEnum getElementEnum() {

        if (orientation == Orientation.VERTICAL) {
            return ElementEnum.LASER_V;
        } else if (orientation == Orientation.HORIZONTAL) {
            return ElementEnum.LASER_H;
        } else {
            throw new RuntimeException("Unknown orientation: " + orientation);
        }
    }
}
