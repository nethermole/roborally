package com.nethermole.roborally.gamepackage.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionTest {

    Position position;

    @Test
    public void moveForward1() {
        position = new Position(5, 5);

        Position facingUp = position.moveForward(Direction.UP, 1);
        Position facingRight = position.moveForward(Direction.RIGHT, 1);
        Position facingDown = position.moveForward(Direction.DOWN, 1);
        Position facingLeft = position.moveForward(Direction.LEFT, 1);

        assertThat(facingUp.getX()).isEqualTo(5);
        assertThat(facingUp.getY()).isEqualTo(6);

        assertThat(facingRight.getX()).isEqualTo(6);
        assertThat(facingRight.getY()).isEqualTo(5);

        assertThat(facingDown.getX()).isEqualTo(5);
        assertThat(facingDown.getY()).isEqualTo(4);

        assertThat(facingLeft.getX()).isEqualTo(4);
        assertThat(facingLeft.getY()).isEqualTo(5);
    }

    @Test
    public void moveBackward1() {
        position = new Position(5, 5);

        Position facingUp = position.moveBackward1(Direction.UP);
        Position facingRight = position.moveBackward1(Direction.RIGHT);
        Position facingDown = position.moveBackward1(Direction.DOWN);
        Position facingLeft = position.moveBackward1(Direction.LEFT);

        assertThat(facingUp.getX()).isEqualTo(5);
        assertThat(facingUp.getY()).isEqualTo(4);

        assertThat(facingRight.getX()).isEqualTo(4);
        assertThat(facingRight.getY()).isEqualTo(5);

        assertThat(facingDown.getX()).isEqualTo(5);
        assertThat(facingDown.getY()).isEqualTo(6);

        assertThat(facingLeft.getX()).isEqualTo(6);
        assertThat(facingLeft.getY()).isEqualTo(5);
    }

}