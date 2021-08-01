package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.board.element.ElementEnum;
import com.nethermole.roborally.gamepackage.board.element.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TileTest {

    Tile tile;

    @BeforeEach
    public void setup() {
        tile = new Tile();
    }

    @Test
    void addAndCheckForElementElement() {
        tile = new Tile();
        assertThat(tile.hasElement(ElementEnum.WALL_DOWN)).isFalse();
        tile.addElement(new Wall(Direction.DOWN));
        assertThat(tile.hasElement(ElementEnum.WALL_DOWN)).isTrue();
    }
}