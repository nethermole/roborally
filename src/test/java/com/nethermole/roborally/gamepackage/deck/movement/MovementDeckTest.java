package com.nethermole.roborally.gamepackage.deck.movement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MovementDeckTest {

    MovementDeck movementDeck;

    @BeforeEach
    public void setup() {
        movementDeck = new MovementDeck(new Random());
    }

    @Test
    public void drawCard_happyPath() {
        MovementCard card = movementDeck.drawCard();
        assertThat(card).isNotNull();
    }

    @Test
    public void drawCard_doesntReturnDuplicates() {
        Set<MovementCard> cardsAlreadyDrawn = new HashSet<>();
        while(movementDeck.cardsLeftInDeck() > 0){
            MovementCard card = movementDeck.drawCard();

            assertThat(cardsAlreadyDrawn).doesNotContain(card);
            cardsAlreadyDrawn.add(card);
        }
    }

}