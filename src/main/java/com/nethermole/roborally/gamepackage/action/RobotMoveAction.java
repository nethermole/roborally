package com.nethermole.roborally.gamepackage.action;

import com.nethermole.roborally.gamepackage.board.Direction;
import com.nethermole.roborally.gamepackage.board.Position;
import lombok.Data;

@Data
public class RobotMoveAction extends BoardAction {
    private String playerId;
    private Position endPosition;
    private Direction endDirection;

    public RobotMoveAction(String playerId, Position endPosition, Direction endDirection) {
        super("RobotMoveAction");

        this.playerId = playerId;
        this.endPosition = endPosition;
        this.endDirection = endDirection;
    }
}
