package com.nethermole.roborally.gamepackage.play;

import com.nethermole.roborally.gamepackage.action.BoardAction;
import com.nethermole.roborally.gamepackage.deck.movement.MovementCard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CardPlay extends Play {
    private String playerId;
    private MovementCard movementCard;

    private List<BoardAction> boardActions;

    public CardPlay(){
        boardActions = new ArrayList<>();
    }

    public void addBoardActions(List<BoardAction> boardActions){
        this.boardActions.addAll(boardActions);
    }

    public void addBoardAction(BoardAction boardAction){
        boardActions.add(boardAction);
    }
}
