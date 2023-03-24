package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nethermole.roborally.gamepackage.deck.movement.Movement.BACKUP;
import static com.nethermole.roborally.gamepackage.deck.movement.Movement.MOVE1;
import static com.nethermole.roborally.gamepackage.deck.movement.Movement.MOVE2;
import static com.nethermole.roborally.gamepackage.deck.movement.Movement.MOVE3;

public class BotUtils {

    public static final Map<Movement, Integer> moveLookup = new HashMap<>();

    Map<Integer, List<List<Movement>>> masterHandLookup = new HashMap<>();

    public BotUtils() {
        moveLookup.put(MOVE1, 1);
        moveLookup.put(MOVE2, 2);
        moveLookup.put(MOVE3, 3);
        moveLookup.put(BACKUP, -1);

        generateMasterHandLookup();
    }

    public Direction getFurthestDirectionOfTarget(Position start, Position end) {
        int xDiff = end.getX() - start.getX();
        int yDiff = end.getY() - start.getY();

        int xMagnitude = Math.abs(xDiff);
        int yMagnitude = Math.abs(yDiff);

        if (xMagnitude > yMagnitude) {
            if (xDiff < 0) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        } else {
            if (yDiff < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    public Direction getSecondaryDirectionOfTarget(Position start, Position end) {
        int xDiff = end.getX() - start.getX();
        int yDiff = end.getY() - start.getY();

        int xMagnitude = Math.abs(xDiff);
        int yMagnitude = Math.abs(yDiff);

        if (xMagnitude > yMagnitude) {
            if (yDiff < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } else {
            if (xDiff < 0) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        }
    }

    public List<MovementCard> noMoving(List<MovementCard> cards, int maxCards) {
        int finalMaxCards = Math.min(maxCards, new Long(cards.stream().filter(card -> card.isTurn()).count()).intValue());

        Optional<List<Movement>> perfect = masterHandLookup.get(0).stream()
                .filter(list -> handContainsMovements(cards, list))
                .filter(it -> it.size() == finalMaxCards)
                .findFirst();
        if (perfect.isPresent()) {
            return convertMovementsToCards(perfect.get(), cards);
        }

        Optional<List<Movement>> turnedRight = masterHandLookup.get(1).stream()
                .filter(list -> handContainsMovements(cards, list))
                .filter(it -> it.size() == finalMaxCards)
                .findFirst();
        if (turnedRight.isPresent()) {
            return convertMovementsToCards(turnedRight.get(), cards);
        }

        Optional<List<Movement>> turnedLeft = masterHandLookup.get(3).stream()
                .filter(list -> handContainsMovements(cards, list))
                .filter(it -> it.size() == finalMaxCards)
                .findFirst();
        if (turnedLeft.isPresent()) {
            return convertMovementsToCards(turnedLeft.get(), cards);
        }

        Optional<List<Movement>> uTurned = masterHandLookup.get(2).stream()
                .filter(list -> handContainsMovements(cards, list))
                .filter(it -> it.size() == finalMaxCards)
                .findFirst();
        if (uTurned.isPresent()) {
            return convertMovementsToCards(uTurned.get(), cards);
        }

        return null;
    }

    public List<MovementCard> convertMovementsToCards(List<Movement> movements, List<MovementCard> cardsIn) {
        List<MovementCard> cards = new ArrayList<>(cardsIn);
        List<MovementCard> returnList = new ArrayList<>();

        for (Movement movement : movements) {
            boolean found = false;
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getMovement() == movement) {
                    found = true;
                    returnList.add(cards.get(i));
                    cards.remove(i);
                    break;
                }
            }
            if (!found) {
                throw new IllegalStateException("BotUtils is fucked");
            }
        }
        return returnList;
    }

    private boolean handContainsMovements(List<MovementCard> handToCheck, List<Movement> cardsToCheckFor) {
        Map<Movement, Integer> handCount = new HashMap<>();
        for (MovementCard card : handToCheck) {
            int containedInHand = handCount.getOrDefault(card.getMovement(), 0);
            containedInHand += 1;
            handCount.put(card.getMovement(), containedInHand);
        }

        Map<Movement, Integer> checkCount = new HashMap<>();
        for (Movement movement : cardsToCheckFor) {
            int checkForCount = checkCount.getOrDefault(movement, 0);
            checkForCount += 1;
            checkCount.put(movement, checkForCount);
        }

        for (Map.Entry<Movement, Integer> check : checkCount.entrySet()) {
            int numInHand = handCount.getOrDefault(check.getKey(), 0);
            numInHand -= check.getValue();
            if (numInHand < 0) {
                return false;
            }
            handCount.put(check.getKey(), numInHand);
        }
        return true;
    }

    public List<MovementCard> greedilySelectMoveForwardCardsToGoExactDistance(List<MovementCard> movementCards, int distance, int maxCards) {
        List<MovementCard> selectedCards = new ArrayList<>();

        movementCards.sort(Comparator.comparingInt(MovementCard::getPriority).reversed());
        for (MovementCard movementCard : movementCards) {
            if (distance == 0 || movementCard.isTurn() || moveLookup.get(movementCard.getMovement()) <= 0 || selectedCards.size() == maxCards) {
                return selectedCards;
            } else if (moveLookup.get(movementCard.getMovement()) > distance) {
                continue;
            } else {
                selectedCards.add(movementCard);
                distance -= moveLookup.get(movementCard.getMovement());
            }
        }
        return selectedCards;
    }

    public int getExactDirectionDistanceToTarget(Position start, Position end, Direction direction) {
        switch (direction) {
            case UP:
                return end.getY() - start.getY();
            case DOWN:
                return (0 - getExactDirectionDistanceToTarget(start, end, Direction.UP));
            case RIGHT:
                return end.getX() - start.getX();
            case LEFT:
                return (0 - getExactDirectionDistanceToTarget(start, end, Direction.RIGHT));

            default:
                return 0;
        }
    }

    public List<MovementCard> getShortestTurnToFaceTargetDirection(Direction facing, Direction targetDirection, List<MovementCard> cards) {
        int turnsRight = 0;
        Direction tempFacing = facing;

        while (tempFacing != targetDirection) {
            tempFacing = Direction.turnRight(tempFacing);
            turnsRight++;
        }

        Optional<List<Movement>> shortestTurn = masterHandLookup.get(turnsRight).stream()
                .sorted(Comparator.comparingInt(List::size))
                .filter(it -> handContainsMovements(cards, it))
                .findFirst();

        if (shortestTurn.isPresent()) {
            return convertMovementsToCards(shortestTurn.get(), cards);
        }
        return null;
    }

    private void generateMasterHandLookup() {
        Map<Movement, Integer> turnValueLookup = new HashMap<>();
        turnValueLookup.put(Movement.TURN_RIGHT, 1);
        turnValueLookup.put(Movement.UTURN, 2);
        turnValueLookup.put(Movement.TURN_LEFT, 3);

        Map<List<Movement>, Integer> handLookup1 = new HashMap();
        for (Map.Entry<Movement, Integer> firstCard : turnValueLookup.entrySet()) {
            List<Movement> movementList = new ArrayList<>();
            movementList.add(firstCard.getKey());

            handLookup1.put(movementList, firstCard.getValue());
        }

        Map<List<Movement>, Integer> handLookup2 = new HashMap();
        for (Map.Entry<List<Movement>, Integer> addSecondCardToThis : handLookup1.entrySet()) {
            for (Map.Entry<Movement, Integer> secondCard : turnValueLookup.entrySet()) {
                List<Movement> newMovementList = new ArrayList<>(addSecondCardToThis.getKey());
                newMovementList.add(secondCard.getKey());

                handLookup2.put(newMovementList, handLookup1.get(addSecondCardToThis.getKey()) + secondCard.getValue());
            }
        }

        Map<List<Movement>, Integer> handLookup3 = new HashMap();
        for (Map.Entry<List<Movement>, Integer> addThirdCardToThis : handLookup2.entrySet()) {
            for (Map.Entry<Movement, Integer> thirdCard : turnValueLookup.entrySet()) {
                List<Movement> newMovementList = new ArrayList<>(addThirdCardToThis.getKey());
                newMovementList.add(thirdCard.getKey());

                handLookup3.put(newMovementList, handLookup2.get(addThirdCardToThis.getKey()) + thirdCard.getValue());
            }
        }

        Map<List<Movement>, Integer> handLookup4 = new HashMap();
        for (Map.Entry<List<Movement>, Integer> addFourthCardToThis : handLookup3.entrySet()) {
            for (Map.Entry<Movement, Integer> fourthCard : turnValueLookup.entrySet()) {
                List<Movement> newMovementList = new ArrayList<>(addFourthCardToThis.getKey());
                newMovementList.add(fourthCard.getKey());

                handLookup4.put(newMovementList, handLookup3.get(addFourthCardToThis.getKey()) + fourthCard.getValue());
            }
        }

        Map<List<Movement>, Integer> handLookup5 = new HashMap();
        for (Map.Entry<List<Movement>, Integer> addFifthCardToThis : handLookup4.entrySet()) {
            for (Map.Entry<Movement, Integer> fifthCard : turnValueLookup.entrySet()) {
                List<Movement> newMovementList = new ArrayList<>(addFifthCardToThis.getKey());
                newMovementList.add(fifthCard.getKey());

                handLookup5.put(newMovementList, handLookup4.get(addFifthCardToThis.getKey()) + fifthCard.getValue());
            }
        }

        Map<Integer, List<List<Movement>>> handsLookup = new HashMap<>();
        for (Map.Entry<List<Movement>, Integer> entry : handLookup1.entrySet()) {
            List<List<Movement>> waysToGetThere = handsLookup.getOrDefault(entry.getValue(), new ArrayList<>());
            waysToGetThere.add(entry.getKey());
            handsLookup.put(entry.getValue(), waysToGetThere);
        }
        for (Map.Entry<List<Movement>, Integer> entry : handLookup2.entrySet()) {
            List<List<Movement>> waysToGetThere = handsLookup.getOrDefault(entry.getValue(), new ArrayList<>());
            waysToGetThere.add(entry.getKey());
            handsLookup.put(entry.getValue(), waysToGetThere);
        }
        for (Map.Entry<List<Movement>, Integer> entry : handLookup3.entrySet()) {
            List<List<Movement>> waysToGetThere = handsLookup.getOrDefault(entry.getValue(), new ArrayList<>());
            waysToGetThere.add(entry.getKey());
            handsLookup.put(entry.getValue(), waysToGetThere);
        }
        for (Map.Entry<List<Movement>, Integer> entry : handLookup4.entrySet()) {
            List<List<Movement>> waysToGetThere = handsLookup.getOrDefault(entry.getValue(), new ArrayList<>());
            waysToGetThere.add(entry.getKey());
            handsLookup.put(entry.getValue(), waysToGetThere);
        }
        for (Map.Entry<List<Movement>, Integer> entry : handLookup5.entrySet()) {
            List<List<Movement>> waysToGetThere = handsLookup.getOrDefault(entry.getValue(), new ArrayList<>());
            waysToGetThere.add(entry.getKey());
            handsLookup.put(entry.getValue(), waysToGetThere);
        }

        for (Map.Entry<Integer, List<List<Movement>>> entry : handsLookup.entrySet()) {
            int totalTurnValue = entry.getKey();
            int modulusTotalTurnValue = (((totalTurnValue % 4) + 4) % 4);

            List<List<Movement>> waysToGetTurnInThatManyPhases = masterHandLookup.getOrDefault(modulusTotalTurnValue, new ArrayList<>());
            waysToGetTurnInThatManyPhases.addAll(entry.getValue());
            masterHandLookup.put(modulusTotalTurnValue, waysToGetTurnInThatManyPhases);
        }
    }

}
