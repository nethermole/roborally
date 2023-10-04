package com.nethermole.roborally.gamepackage.player;

import org.junit.jupiter.api.Test;

class HumanPlayerTest {

    HumanPlayer humanPlayer;

    @Test
    public void playerHasId() {
        humanPlayer = new HumanPlayer("1");
    }

}