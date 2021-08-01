package com.nethermole.roborally.gamepackage.board.element;

import com.nethermole.roborally.gamepackage.board.Direction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HorizontalWallBottomTest {

    @Test
    void getElementEnum_returnsHorizontalWall() {
        Wall horizontalWallBottom = new Wall(Direction.DOWN);
        assertThat(horizontalWallBottom.getElementEnum()).isEqualTo(ElementEnum.WALL_DOWN);
    }
}