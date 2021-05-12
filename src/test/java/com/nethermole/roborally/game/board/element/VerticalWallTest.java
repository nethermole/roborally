package com.nethermole.roborally.game.board.element;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerticalWallTest {

    @Test
    void getElementEnum() {
        VerticalWall verticalWall = new VerticalWall();
        assertThat(verticalWall.getElementEnum()).isEqualTo(ElementEnum.VERTICAL_WALL);
    }
}