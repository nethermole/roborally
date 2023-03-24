package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BotUtilsTest {

    public BotUtils botUtils;

    @BeforeEach
    public void setup(){
        botUtils = new BotUtils();
    }

    @Test
    void getFurthestDirectionOfTarget() {
        Position startDOWN = new Position(-2,-3);
        Position endUP = new Position(1,20);

        assertThat(botUtils.getFurthestDirectionOfTarget(startDOWN, endUP)).isEqualTo(Direction.UP);
        assertThat(botUtils.getFurthestDirectionOfTarget(endUP, startDOWN)).isEqualTo(Direction.DOWN);

        Position startLeft = new Position(3, 5);
        Position endRIGHT = new Position(20,6);

        assertThat(botUtils.getFurthestDirectionOfTarget(startLeft, endRIGHT)).isEqualTo(Direction.RIGHT);
        assertThat(botUtils.getFurthestDirectionOfTarget(endRIGHT, startLeft)).isEqualTo(Direction.LEFT);
    }

    @Test
    public void convertMovementsToMovementCards(){
        List<Movement> movements = new ArrayList<>();
        movements.add(Movement.UTURN);
        movements.add(Movement.UTURN);

        List<MovementCard> cards = new ArrayList<>();
        cards.add(new MovementCard(Movement.BACKUP, 450));
        cards.add(new MovementCard(Movement.TURN_LEFT, 270));
        cards.add(new MovementCard(Movement.TURN_LEFT, 190));
        cards.add(new MovementCard(Movement.TURN_LEFT, 150));
        cards.add(new MovementCard(Movement.UTURN, 30));
        cards.add(new MovementCard(Movement.UTURN, 20));

        List<MovementCard> selectedCards = botUtils.convertMovementsToCards(movements, cards);
        Set<MovementCard> deduped = new HashSet<>(selectedCards);
        assertThat(selectedCards.size()).isEqualTo(deduped.size());
    }
}