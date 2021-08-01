package com.nethermole.roborally.gamepackage.board.element;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PitTest {

    @Test
    void getElementEnum() {
        Pit pit = new Pit();
        assertThat(pit.getElementEnum()).isEqualTo(ElementEnum.PIT);
    }
}