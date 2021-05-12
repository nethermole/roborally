package com.nethermole.roborally.game.board;

import com.nethermole.roborally.game.board.element.ElementEnum;
import com.nethermole.roborally.game.board.element.HorizontalWall;
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
        assertThat(tile.hasElement(ElementEnum.HORIZONTAL_WALL)).isFalse();
        tile.addElement(new HorizontalWall());
        assertThat(tile.hasElement(ElementEnum.HORIZONTAL_WALL)).isTrue();
    }
}