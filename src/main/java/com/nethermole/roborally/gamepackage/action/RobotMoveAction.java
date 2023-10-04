package com.nethermole.roborally.gamepackage.action;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RobotMoveAction extends BoardAction{
    private String playerId;
    private Position startPosition;
    private Position endPosition;
}
