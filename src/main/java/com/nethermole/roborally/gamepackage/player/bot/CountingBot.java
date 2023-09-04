package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.DirectionObject;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.Movement;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nethermole.roborally.gamepackage.deck.movement.Movement.MOVE1;
import static com.nethermole.roborally.gamepackage.deck.movement.Movement.MOVE2;
import static com.nethermole.roborally.gamepackage.player.bot.BotUtils.moveLookup;

public class CountingBot extends NPCPlayer {

    private static final Logger log = LogManager.getLogger(CountingBot.class);
    private BotUtils botUtils;

    public CountingBot(int id) {
        super(id);
        this.setDisplayName("C-" + id);
        this.botUtils = new BotUtils();
    }

    @Override
    public List<MovementCard> chooseCards(List<MovementCard> cardsToChooseFrom, Board board) {
        List<MovementCard> cardsToSubmit = new ArrayList<>();

        Position assumedPosition = new Position(this.getPosition());
        DirectionObject assumedFacing = new DirectionObject(getFacing());
        Position targetPosition = new Position(board.getPositionOfCheckpoint(getMostRecentCheckpointTouched() + 1));

        while (cardsToSubmit.size() < 5) {
            int xDiff = Math.abs(targetPosition.getX() - assumedPosition.getX());
            int yDiff = Math.abs(targetPosition.getY() - assumedPosition.getY());

            Direction targetDirection = botUtils.getFurthestDirectionOfTarget(assumedPosition, targetPosition);

            boolean turnSuccess = turnTowardsTarget(targetDirection, assumedFacing, cardsToChooseFrom, cardsToSubmit, assumedFacing, assumedPosition);
            if (!turnSuccess) {
                tryToNotMove(cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
            } else {
                //add movement cards
                int diff = xDiff;
                if (targetDirection == Direction.UP || targetDirection == Direction.DOWN) {
                    diff = yDiff;
                }
                boolean wentExactDistance = tryToGoExactDistance(diff, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                if (!wentExactDistance || (wentExactDistance && diff == 0)) {
                    tryToNotMove(cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                    break;
                }
            }
        }


        if (cardsToSubmit.size() == 5) {
            return cardsToSubmit;
        } else {
            throw new IllegalStateException("CountingBot chose " + cardsToSubmit.size() + " cards");
        }
    }

    public boolean tryToGoExactDistance(int distance, List<MovementCard> cardsToSubmit, List<MovementCard> cardsToChooseFrom, DirectionObject assumedFacing, Position assumedPosition) {
        //get movement cards sorted by priority, descending
        List<MovementCard> moveCards = cardsToChooseFrom.stream()
                .filter(card -> !card.isTurn())
                .sorted(Comparator.comparing(MovementCard::getPriority))
                .collect(Collectors.toList());
        Collections.reverse(moveCards);

        boolean keepLooking = true;
        for (MovementCard movementCard; cardsToSubmit.size() < 5 && moveCards.size() > 0 && distance > 0 && keepLooking; ) {
            movementCard = null;

            switch (distance) {
                case 1:
                    for (MovementCard card : moveCards) {
                        if (card.getMovement() == MOVE1) {
                            movementCard = card;
                            break;
                        }
                    }
                    break;
                case 2:
                    for (MovementCard card : moveCards) {
                        if (card.getMovement() == MOVE2) {
                            movementCard = card;
                            break;
                        }
                    }
                    break;
                default:
                    movementCard = moveCards.get(0);
                    log.trace("moveCards.get(0) is " + movementCard);
            }

            if (movementCard != null && cardsToSubmit.size() < 5) {
                distance -= moveLookup.get(movementCard.getMovement());

                submitCard(movementCard, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                moveCards.remove(movementCard);

            } else {
                keepLooking = false;
            }
        }
        return distance <= 0 || cardsToSubmit.size() == 5;
    }

    public void tryToNotMove(List<MovementCard> cardsToSubmit, List<MovementCard> cardsToChooseFrom, DirectionObject assumedFacing, Position assumedPosition) {
        //spin as much as possible
        List<MovementCard> turns = cardsToChooseFrom.stream().filter(MovementCard::isTurn).collect(Collectors.toList());
        while ((!turns.isEmpty()) && cardsToSubmit.size() < 5) {
            MovementCard card = turns.remove(0);
            log.trace("tryToNotMove().submitCard() : " + card);
            submitCard(card, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
        }

        //order remaining cards to go the shortest distance possible
        List<MovementCard> moveCards = cardsToChooseFrom.stream()
                .filter(card -> !card.isTurn())
                .sorted(Comparator.comparing(MovementCard::getPriority))
                .collect(Collectors.toList());

        //add movement cards
        while (!moveCards.isEmpty() && cardsToSubmit.size() < 5) {
            MovementCard card = moveCards.remove(0);
            log.trace("tryToNotMove().submitCard2() : " + card);
            submitCard(card, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
        }
    }

    //returns whether or not we ended up facing the target
    public boolean turnTowardsTarget(Direction targetDirection, DirectionObject currentDirection, List<MovementCard> cardsToChooseFrom, List<MovementCard> cardsToSubmit, DirectionObject assumedFacing, Position assumedPosition) {
        //no turn required
        if (targetDirection == currentDirection.getDirection()) {
            return true;
        }

        //uturn required
        if (targetDirection == Direction.getOpposite(currentDirection.getDirection())) {
            Optional<MovementCard> uturn = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.UTURN).findFirst();

            //uturn card
            if (uturn.isPresent()) {
                MovementCard _uturn = uturn.get();
                log.trace("turnTowardsTarget().submitCard() : " + _uturn);
                submitCard(_uturn, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                return true;
            }

            //two turn rights
            List<MovementCard> turnRights = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_RIGHT).collect(Collectors.toList());
            if (turnRights.size() >= 2) {
                MovementCard turnRight1 = turnRights.get(0);
                MovementCard turnRight2 = turnRights.get(1);

                log.trace("turnTowardsTarget().submitCard() : " + turnRight1);
                log.trace("turnTowardsTarget().submitCard() : " + turnRight2);

                submitCard(turnRight1, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                submitCard(turnRight2, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                return true;
            }

            //two turn lefts
            List<MovementCard> turnLefts = cardsToChooseFrom.stream().filter(card -> card.getMovement() == Movement.TURN_LEFT).collect(Collectors.toList());
            if (turnLefts.size() >= 2) {
                MovementCard turnLeft1 = turnLefts.get(0);
                MovementCard turnLeft2 = turnLefts.get(1);

                log.trace("turnTowardsTarget().submitCard() : " + turnLeft1);
                log.trace("turnTowardsTarget().submitCard() : " + turnLeft2);

                submitCard(turnLeft1, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                submitCard(turnLeft2, cardsToSubmit, cardsToChooseFrom, assumedFacing, assumedPosition);
                return true;
            }
        }

        //turn right required
        if (Direction.turnRight(currentDirection.getDirection()) == targetDirection) {
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
        if (Direction.turnLeft(currentDirection.getDirection()) == targetDirection) {
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

    private void submitCard(MovementCard card, List<MovementCard> cardsToSubmit, List<MovementCard> cardsToChooseFrom, DirectionObject assumedFacing, Position assumedPosition) {
        if (cardsToSubmit.size() == 5) {
            log.trace("Hand submission full, not submitting " + card);
            return;
        }

        boolean containedFlag = cardsToChooseFrom.remove(card);
        if (!containedFlag) {
            throw new IllegalStateException("CountingBot submitCard containedFlag error");
        }

        cardsToSubmit.add(card);
        adjustMovement(assumedFacing, assumedPosition, moveLookup.getOrDefault(card.getMovement(), 0));
    }

    private void adjustMovement(DirectionObject assumedFacing, Position assumedPosition, int dist) {
        switch (assumedFacing.getDirection()) {
            case UP:
                assumedPosition.setY(assumedPosition.getY() + dist);
                break;
            case RIGHT:
                assumedPosition.setX(assumedPosition.getX() + dist);
                break;
            case DOWN:
                assumedPosition.setY(assumedPosition.getY() - dist);
                break;
            case LEFT:
                assumedPosition.setX(assumedPosition.getX() - dist);
                break;
        }
    }
}
