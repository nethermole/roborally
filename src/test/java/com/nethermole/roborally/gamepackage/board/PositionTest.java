package com.nethermole.roborally.gamepackage.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionTest {

    Position position;

    @Test
    public void moveForward1() {
        position = new Position(5, 5);

        Position up = position.moveForward1(Direction.UP);
        Position right = position.moveForward1(Direction.RIGHT);
        Position down = position.moveForward1(Direction.DOWN);
        Position left = position.moveForward1(Direction.LEFT);

        assertThat(up.getX()).isEqualTo(5);
        assertThat(up.getY()).isEqualTo(4);

        assertThat(right.getX()).isEqualTo(6);
        assertThat(right.getY()).isEqualTo(5);

        assertThat(down.getX()).isEqualTo(5);
        assertThat(down.getY()).isEqualTo(6);

        assertThat(left.getX()).isEqualTo(4);
        assertThat(left.getY()).isEqualTo(5);
    }

    @Test
    public void moveBackward1() {
        position = new Position(5, 5);

        Position up = position.moveBackward1(Direction.UP);
        Position right = position.moveBackward1(Direction.RIGHT);
        Position down = position.moveBackward1(Direction.DOWN);
        Position left = position.moveBackward1(Direction.LEFT);

        assertThat(up.getX()).isEqualTo(5);
        assertThat(up.getY()).isEqualTo(6);

        assertThat(right.getX()).isEqualTo(4);
        assertThat(right.getY()).isEqualTo(5);

        assertThat(down.getX()).isEqualTo(5);
        assertThat(down.getY()).isEqualTo(4);

        assertThat(left.getX()).isEqualTo(6);
        assertThat(left.getY()).isEqualTo(5);
    }

}