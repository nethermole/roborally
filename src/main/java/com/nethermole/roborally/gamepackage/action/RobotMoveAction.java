package com.nethermole.roborally.gamepackage.action;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RobotMoveAction extends BoardAction{
    private String playerId;
    private Position startPosition;
    private Position endPosition;

    public RobotMoveAction(String playerId, Position startPosition, Position endPosition) {
        super("RobotMoveAction");

        this.playerId = playerId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
}
