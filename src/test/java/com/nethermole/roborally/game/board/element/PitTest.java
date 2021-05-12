package com.nethermole.roborally.game.board.element;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PitTest {

    @Test
    void getElementEnum() {
        Pit pit = new Pit();
        assertThat(pit.getElementEnum()).isEqualTo(ElementEnum.PIT);
    }
}