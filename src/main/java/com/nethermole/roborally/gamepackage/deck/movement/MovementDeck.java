package com.nethermole.roborally.gamepackage.deck.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovementDeck {

    private List<MovementCard> deck;
    private Random random;

    public MovementDeck(Random random) {
        this.random = random;

        deck = createNewDeck();
    }

    public int cardsLeftInDeck(){
        return deck.size();
    }

    public MovementCard drawCard() {
        return deck.remove(random.nextInt(deck.size()));
    }

    private List<MovementCard> createNewDeck() {
        List<MovementCard> movementCardList = new ArrayList<>();

        movementCardList.add(new MovementCard(Movement.UTURN, 10));
        movementCardList.add(new MovementCard(Movement.UTURN, 20));
        movementCardList.add(new MovementCard(Movement.UTURN, 30));
        movementCardList.add(new MovementCard(Movement.UTURN, 40));
        movementCardList.add(new MovementCard(Movement.UTURN, 50));
        movementCardList.add(new MovementCard(Movement.UTURN, 60));

        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 70));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 90));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 110));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 130));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 150));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 170));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 190));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 210));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 230));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 250));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 270));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 290));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 310));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 330));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 350));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 370));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 390));
        movementCardList.add(new MovementCard(Movement.TURN_LEFT, 410));

        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 80));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 100));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 120));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 140));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 160));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 180));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 200));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 220));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 240));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 260));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 280));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 300));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 320));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 340));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 360));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 380));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 400));
        movementCardList.add(new MovementCard(Movement.TURN_RIGHT, 420));

        movementCardList.add(new MovementCard(Movement.BACKUP, 430));
        movementCardList.add(new MovementCard(Movement.BACKUP, 440));
        movementCardList.add(new MovementCard(Movement.BACKUP, 450));
        movementCardList.add(new MovementCard(Movement.BACKUP, 460));
        movementCardList.add(new MovementCard(Movement.BACKUP, 470));
        movementCardList.add(new MovementCard(Movement.BACKUP, 480));

        movementCardList.add(new MovementCard(Movement.MOVE1, 490));
        movementCardList.add(new MovementCard(Movement.MOVE1, 500));
        movementCardList.add(new MovementCard(Movement.MOVE1, 510));
        movementCardList.add(new MovementCard(Movement.MOVE1, 520));
        movementCardList.add(new MovementCard(Movement.MOVE1, 530));
        movementCardList.add(new MovementCard(Movement.MOVE1, 540));
        movementCardList.add(new MovementCard(Movement.MOVE1, 550));
        movementCardList.add(new MovementCard(Movement.MOVE1, 560));
        movementCardList.add(new MovementCard(Movement.MOVE1, 570));
        movementCardList.add(new MovementCard(Movement.MOVE1, 580));
        movementCardList.add(new MovementCard(Movement.MOVE1, 590));
        movementCardList.add(new MovementCard(Movement.MOVE1, 600));
        movementCardList.add(new MovementCard(Movement.MOVE1, 610));
        movementCardList.add(new MovementCard(Movement.MOVE1, 620));
        movementCardList.add(new MovementCard(Movement.MOVE1, 630));
        movementCardList.add(new MovementCard(Movement.MOVE1, 640));
        movementCardList.add(new MovementCard(Movement.MOVE1, 650));
        movementCardList.add(new MovementCard(Movement.MOVE1, 660));

        movementCardList.add(new MovementCard(Movement.MOVE2, 670));
        movementCardList.add(new MovementCard(Movement.MOVE2, 680));
        movementCardList.add(new MovementCard(Movement.MOVE2, 690));
        movementCardList.add(new MovementCard(Movement.MOVE2, 700));
        movementCardList.add(new MovementCard(Movement.MOVE2, 710));
        movementCardList.add(new MovementCard(Movement.MOVE2, 720));
        movementCardList.add(new MovementCard(Movement.MOVE2, 730));
        movementCardList.add(new MovementCard(Movement.MOVE2, 740));
        movementCardList.add(new MovementCard(Movement.MOVE2, 750));
        movementCardList.add(new MovementCard(Movement.MOVE2, 760));
        movementCardList.add(new MovementCard(Movement.MOVE2, 770));
        movementCardList.add(new MovementCard(Movement.MOVE2, 780));

        movementCardList.add(new MovementCard(Movement.MOVE3, 780));
        movementCardList.add(new MovementCard(Movement.MOVE3, 790));
        movementCardList.add(new MovementCard(Movement.MOVE3, 800));
        movementCardList.add(new MovementCard(Movement.MOVE3, 810));
        movementCardList.add(new MovementCard(Movement.MOVE3, 820));
        movementCardList.add(new MovementCard(Movement.MOVE3, 830));
        movementCardList.add(new MovementCard(Movement.MOVE3, 840));

        return movementCardList;
    }

}
