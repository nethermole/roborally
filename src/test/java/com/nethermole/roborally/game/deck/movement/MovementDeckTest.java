package com.nethermole.roborally.game.deck.movement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MovementDeckTest {

    MovementDeck movementDeck;

    @BeforeEach
    public void setup() {
        movementDeck = new MovementDeck();
    }

    @Test
    public void drawCard() {
        MovementCard card = movementDeck.drawCard();
        assertThat(card).isNotNull();

        MovementCard secondCard = movementDeck.drawCard();
        assertThat(card).isNotEqualTo(secondCard);
    }

}