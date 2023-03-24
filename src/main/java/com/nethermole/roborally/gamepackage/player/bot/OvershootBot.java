package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OvershootBot extends NPCPlayer {

    public OvershootBot(int id) {
        super(id);
        this.setName("OS-" + id);
    }

    @Override
    public List<MovementCard> chooseCards(List<MovementCard> cardsToChooseFrom, Board board) {
        List<MovementCard> cardsToSubmit = new ArrayList<>();

        Position currentPosition = this.getPosition();
        Position targetPosition = board.getPositionOfCheckpoint(getMostRecentCheckpointTouched() + 1);

        Direction targetDirection = getTargetDirection(currentPosition, targetPosition);
        Direction currentFacing = getFacing();

        boolean turnSuccess = turnTowardsTarget(targetDirection, currentFacing, cardsToChooseFrom, cardsToSubmit);
        if (!turnSuccess) {
            //spin as much as possible
            List<MovementCard> turns = cardsToChooseFrom.stream().filter(MovementCard::isTurn).collect(Collectors.toList());
            for (int i = 0; (!turns.isEmpty()) && i < 5; i++) {
                MovementCard card = turns.remove(0);
                submitCard(card, cardsToSubmit, cardsToChooseFrom);
            }

            //order remaining cards to go the shortest distance possible
            List<MovementCard> moveCards = cardsToChooseFrom.stream()
                    .filter(card -> !card.isTurn())
                    .sorted(Comparator.comparing(MovementCard::getPriority))
                    .collect(Collectors.toList());

            //add movement cards
            while (!moveCards.isEmpty() && cardsToSubmit.size() < 5) {
                submitCard(moveCards.get(0), cardsToSubmit, cardsToChooseFrom);
                moveCards.remove(0);
            }

        } else {
            //order remaining cards to go far!
            List<MovementCard> moveCards = cardsToChooseFrom.stream()
                    .filter(card -> !card.isTurn())
                    .sorted(Comparator.comparing(MovementCard::getPriority))
                    .collect(Collectors.toList());
            Collections.reverse(moveCards);

            //add movement cards
            while (!moveCards.isEmpty() && cardsToSubmit.size() < 5) {
                submitCard(moveCards.get(0), cardsToSubmit, cardsToChooseFrom);
                moveCards.remove(0);
            }
            if (cardsToSubmit.size() < 5) {
                //spin as much as possible
                List<MovementCard> turns = cardsToChooseFrom.stream().filter(MovementCard::isTurn).collect(Collectors.toList());
                for (int i = 0; cardsToSubmit.size() < 5; i++) {
                    MovementCard card = turns.remove(0);
                    submitCard(card, cardsToSubmit, cardsToChooseFrom);
                }
            }
        }


        if (cardsToSubmit.size() == 5) {
            return cardsToSubmit;
        } else {
            throw new IllegalStateException("OvershootBot chose " + cardsToSubmit.size() + " cards");
        }
    }

    //returns whether or not we ended up facing the target
    public boolean turnTowardsTarget(Direction targetDirection, Direction currentDirection, List<MovementCard> cardsToChooseFrom, List<MovementCard> cardsToSubmit) {
        //no turn required
        if (targetDirection == currentDirection) {
            return true;
        }

        //uturn required
        if (targetDirection == Direction.getOpposite(currentDirection)) {
            Optional<MovementCard> uturn = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.UTURN).findFirst();

            //uturn card
            if (uturn.isPresent()) {
                MovementCard _uturn = uturn.get();
                cardsToChooseFrom.remove(_uturn);
                cardsToSubmit.add(_uturn);
                return true;
            }

            //two turn rights
            List<MovementCard> turnRights = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_RIGHT).collect(Collectors.toList());
            if (turnRights.size() >= 2) {
                MovementCard turnRight1 = turnRights.get(0);
                MovementCard turnRight2 = turnRights.get(1);

                cardsToChooseFrom.remove(turnRight1);
                cardsToChooseFrom.remove(turnRight2);

                cardsToSubmit.add(turnRight1);
                cardsToSubmit.add(turnRight2);
                return true;
            }

            //two turn lefts
            List<MovementCard> turnLefts = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_LEFT).collect(Collectors.toList());
            if (turnLefts.size() >= 2) {
                MovementCard turnLeft1 = turnLefts.get(0);
                MovementCard turnLeft2 = turnLefts.get(1);

                cardsToChooseFrom.remove(turnLeft1);
                cardsToChooseFrom.remove(turnLeft2);

                cardsToSubmit.add(turnLeft1);
                cardsToSubmit.add(turnLeft2);
                return true;
            }
        }

        //turn right required
        if (Direction.turnRight(currentDirection) == targetDirection) {
            Optional<MovementCard> turnRight = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_RIGHT).findFirst();

            //turn right
            if (turnRight.isPresent()) {
                MovementCard _turnRight = turnRight.get();

                cardsToChooseFrom.remove(_turnRight);
                cardsToSubmit.add(_turnRight);
                return true;
            }

            //todo: left+uturn

            //todo: left x3
        }

        //turn left required
        if (Direction.turnLeft(currentDirection) == targetDirection) {
            Optional<MovementCard> turnLeft = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_LEFT).findFirst();

            //turn right
            if (turnLeft.isPresent()) {
                MovementCard _turnLeft = turnLeft.get();

                cardsToChooseFrom.remove(_turnLeft);
                cardsToSubmit.add(_turnLeft);
                return true;
            }

            //todo: right+uturn

            //todo: right x3
        }

        return false;
    }

    public Direction getTargetDirection(Position currentPosition, Position targetPosition) {
        int xDiff = targetPosition.getX() - currentPosition.getX();
        int yDiff = targetPosition.getY() - currentPosition.getY();

        Direction targetDir = Direction.RIGHT;
        int maxDiff = xDiff;

        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            maxDiff = yDiff;
            targetDir = Direction.UP;
        }
        if (maxDiff < 0) {
            targetDir = Direction.getOpposite(targetDir);
        }
        return targetDir;
    }

    private void submitCard(MovementCard card, List<MovementCard> cardsToSubmit, List<MovementCard> cardsToChooseFrom) {
        boolean containedFlag = cardsToChooseFrom.remove(card);
        if (!containedFlag) {
            throw new IllegalStateException("OvershootBot submitCard containedFlag error");
        }

        cardsToSubmit.add(card);
    }

    private void submitCards(List<MovementCard> cards, List<MovementCard> cardsToSubmit, List<MovementCard> cardsToChooseFrom) {
        for (MovementCard card : cards) {
            submitCard(card, cardsToSubmit, cardsToChooseFrom);
        }
    }
}
