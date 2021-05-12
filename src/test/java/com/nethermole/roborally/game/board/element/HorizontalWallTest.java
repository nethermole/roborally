package com.nethermole.roborally.game.board.element;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HorizontalWallTest {

    @Test
    void getElementEnum_returnsHorizontalWall() {
        HorizontalWall horizontalWall = new HorizontalWall();
        assertThat(horizontalWall.getElementEnum()).isEqualTo(ElementEnum.HORIZONTAL_WALL);
    }
}