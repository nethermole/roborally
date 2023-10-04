package com.nethermole.roborally.gamepackage.turn;

import com.nethermole.roborally.gamepackage.ViewStep;
import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import com.nethermole.roborally.gamepackage.player.Player;
import lombok.Data;

@Data
public class RobotMoveViewStep extends ViewStep {
    String robotId;
    Position startPosition;
    Position endPosition;
    Direction startFacing;
    Direction endFacing;
    MovementMethod movementMethod;

    public RobotMoveViewStep(Player player, Position startPosition, Position endPosition, Direction startFacing, Direction endFacing, MovementMethod movementMethod) {
        this.setTypeName("RobotMoveViewStep");

        this.robotId = player.getId();
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.startFacing = startFacing;
        this.endFacing = endFacing;
        this.movementMethod = movementMethod;
    }

    @Override
    public String toString() {
        StringBuilder log = new StringBuilder();
        if (!startFacing.equals(endFacing)) {
            log.append(robotId + "'s robot turned from " + startFacing + " to " + endFacing + "\n");
        }
        if (!startPosition.equals(endPosition)) {
            log.append(robotId + "'s robot moved from " + startPosition + " to " + endPosition);
        }
        return log.toString();
    }
}
