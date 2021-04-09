package com.nethermole.roborally.game.turn;

import com.nethermole.roborally.game.board.Coordinate;
import com.nethermole.roborally.game.board.Direction;
import com.nethermole.roborally.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RobotMoveEvent extends Event{
    Player player;
    Coordinate startPosition;
    Coordinate endPosition;
    Direction startFacing;
    Direction endFacing;
    MovementMethod movementMethod;

    @Override
    public String toString(){
        StringBuilder log = new StringBuilder();
        String playerName = player.getName();
        if(!startFacing.equals(endFacing)){
            log.append(playerName + "'s robot turned from " + startFacing + " to " + endFacing + "\n");
        }
        if(!startPosition.equals(endPosition)){
            log.append(playerName + "'s robot moved from " + startPosition + " to " + endPosition);
        }
        return log.toString();
    }
}
