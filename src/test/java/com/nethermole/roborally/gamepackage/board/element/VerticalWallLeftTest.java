package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Direction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerticalWallLeftTest {

    @Test
    void getElementEnum() {
        Wall verticalWallLeft = new Wall(Direction.LEFT);
        assertThat(verticalWallLeft.getElementEnum()).isEqualTo(ElementEnum.WALL_LEFT);
    }
}