package com.nethermole.roborally.gamepackage.player.bot;

import com.nethermole.roborally.gamepackage.board.Board;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MinimalTurnBot extends NPCPlayer {

    private static final Logger log = LogManager.getLogger(MinimalTurnBot.class);

    private BotUtils botUtils;

    public MinimalTurnBot(int id) {
        super(id);
        this.setDisplayName("M-" + id);
        this.botUtils = new BotUtils();
    }

    @Override
    public List<MovementCard> chooseCards(List<MovementCard> cardsToChooseFrom, Board board) {
        Position nextCheckpointPosition = board.getPositionOfCheckpoint(this.getNextCheckpointIndex());

        Direction targetDirection = botUtils.getFurthestDirectionOfTarget(this.getPosition(), nextCheckpointPosition);
        Direction secondaryTargetDirection = botUtils.getSecondaryDirectionOfTarget(this.getPosition(), nextCheckpointPosition);

        List<MovementCard> selectedCards = new ArrayList<>();

        boolean correctlyFacing = true;
        if (this.getFacing() != targetDirection && this.getFacing() != secondaryTargetDirection) {
            correctlyFacing = false;
            List<MovementCard> shortestTurnToFaceTargetPrimaryDirection = botUtils.getShortestTurnToFaceTargetDirection(getFacing(), targetDirection, cardsToChooseFrom);
            List<MovementCard> shortestTurnToFaceTargetSecondaryDirection = botUtils.getShortestTurnToFaceTargetDirection(getFacing(), secondaryTargetDirection, cardsToChooseFrom);

            if (shortestTurnToFaceTargetPrimaryDirection != null) {
                correctlyFacing = true;

                selectedCards.addAll(shortestTurnToFaceTargetPrimaryDirection);
                cardsToChooseFrom.removeAll(shortestTurnToFaceTargetPrimaryDirection);
            } else if (shortestTurnToFaceTargetSecondaryDirection != null) {
                correctlyFacing = true;

                selectedCards.addAll(shortestTurnToFaceTargetSecondaryDirection);
                cardsToChooseFrom.removeAll(shortestTurnToFaceTargetSecondaryDirection);
            }
        }

        if (this.getFacing() == targetDirection || correctlyFacing) {
            int distance = botUtils.getExactDirectionDistanceToTarget(this.getPosition(), nextCheckpointPosition, targetDirection);
            List<MovementCard> moveCards = botUtils.greedilySelectMoveForwardCardsToGoExactDistance(cardsToChooseFrom, distance, 5 - selectedCards.size());
            if (moveCards != null) {
                selectedCards.addAll(moveCards);
                cardsToChooseFrom.removeAll(moveCards);
            }
        } else if (this.getFacing() == secondaryTargetDirection || correctlyFacing) {
            int distance = botUtils.getExactDirectionDistanceToTarget(this.getPosition(), nextCheckpointPosition, secondaryTargetDirection);
            List<MovementCard> moveCards = botUtils.greedilySelectMoveForwardCardsToGoExactDistance(cardsToChooseFrom, distance, 5 - selectedCards.size());
            if (moveCards != null) {
                selectedCards.addAll(moveCards);
                cardsToChooseFrom.removeAll(moveCards);
            }
        }

        List<MovementCard> idleCards = botUtils.noMoving(cardsToChooseFrom, 5 - selectedCards.size());
        if (idleCards != null) {
            selectedCards.addAll(idleCards);
            cardsToChooseFrom.removeAll(idleCards);
        }

        //fill with lowest movement if there's still cards
        cardsToChooseFrom = cardsToChooseFrom.stream().sorted(Comparator.comparing(MovementCard::getPriority)).collect(Collectors.toList());
        for (int i = 0; selectedCards.size() < 5; i++) {
            if (i > 500) {
                throw new IllegalStateException("Entered an infinite loop somehow");
            }
            selectedCards.add(cardsToChooseFrom.remove(0));
        }

        return selectedCards;
    }

}
