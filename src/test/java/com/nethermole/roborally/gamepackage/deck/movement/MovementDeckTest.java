package com.nethermole.roborally.gamepackage.deck.movement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MovementDeckTest {

    MovementDeck movementDeck;

    @BeforeEach
    public void setup() {
        movementDeck = new MovementDeck(new Random());
    }

    @Test
    public void drawCard() {
        MovementCard card = movementDeck.drawCard();
        assertThat(card).isNotNull();

        MovementCard secondCard = movementDeck.drawCard();
        assertThat(card).isNotEqualTo(secondCard);
    }

}