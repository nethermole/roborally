package com.nethermole.roborally.game.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DirectionTest {

    @Test
    public void turnRight() {
        Direction subject = Direction.UP;

        Direction right = Direction.turnRight(subject);
        Direction down = Direction.turnRight(right);
        Direction left = Direction.turnRight(down);
        Direction up = Direction.turnRight(left);

        assertThat(right).isEqualTo(Direction.RIGHT);
        assertThat(down).isEqualTo(Direction.DOWN);
        assertThat(left).isEqualTo(Direction.LEFT);
        assertThat(up).isEqualTo(Direction.UP);
    }

    @Test
    public void turnLeft() {
        Direction subject = Direction.UP;

        Direction left = Direction.turnLeft(subject);
        Direction down = Direction.turnLeft(left);
        Direction right = Direction.turnLeft(down);
        Direction up = Direction.turnLeft(right);

        assertThat(left).isEqualTo(Direction.LEFT);
        assertThat(down).isEqualTo(Direction.DOWN);
        assertThat(right).isEqualTo(Direction.RIGHT);
        assertThat(up).isEqualTo(Direction.UP);
    }

}