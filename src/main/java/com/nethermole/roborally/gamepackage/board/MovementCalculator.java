package com.nethermole.roborally.gamepackage.board;

import com.nethermole.roborally.gamepackage.turn.MovementMethod;
import com.nethermole.roborally.gamepackage.viewStep.RobotMoveViewStep;
import com.nethermole.roborally.gamepackage.viewStep.ViewStep;
import com.nethermole.roborally.gamepackage.player.Player;

import java.util.ArrayList;
import java.util.List;

public class MovementCalculator {

    public List<ViewStep> move1(Player player, Board board){
        List<ViewStep> viewSteps = new ArrayList<>();

        Position startPosition = player.getPosition();
        Direction facing = player.getFacing();
        Position endPosition = player.getPosition().moveForward(facing, 1);

        boolean successfulMoveForward = tryMoveInto(facing, endPosition, board);
        if(successfulMoveForward){
            player.setPosition(endPosition);
            ViewStep viewStep = new RobotMoveViewStep(
                    player,
                    startPosition,
                    endPosition,
                    player.getFacing(),
                    player.getFacing(),
                    MovementMethod.MOVE
            );
            viewSteps.add(viewStep);
        }
        return viewSteps;
    }

    public List<ViewStep> move2(Player mover, Board board){
        move1(mover, board);
        move1(mover, board);
        return new ArrayList<>();
    }

    public List<ViewStep> move3(Player mover, Board board){
        move1(mover, board);
        move1(mover, board);
        move1(mover, board);
        return new ArrayList<>();
    }

    public boolean tryMoveInto(Direction direction, Position newPosition, Board board){
        if(board.tileContainsAnyPlayer(newPosition)){
            boolean canPush = board.tileCanBePushedInto(newPosition.moveForward(direction,1));
            if(canPush){
                board.pushPushables(newPosition, newPosition.moveForward(direction,1));
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

}
