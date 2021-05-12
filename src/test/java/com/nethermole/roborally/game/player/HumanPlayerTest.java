package com.nethermole.roborally.game.player;

import org.junit.jupiter.api.Test;

class HumanPlayerTest {

    HumanPlayer humanPlayer;

    @Test
    public void playerHasId() {
        humanPlayer = new HumanPlayer(1);
    }

}