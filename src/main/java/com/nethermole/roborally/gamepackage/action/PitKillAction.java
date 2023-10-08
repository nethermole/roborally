package com.nethermole.roborally.gamepackage.action;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.Data;

@Data
public class PitKillAction extends BoardAction {

    String playerId;
    Position fallPosition;
    Position respawnPosition;

    public PitKillAction(String playerId, Position fallPosition, Position respawnPosition) {
        super("PitKillAction");
        this.playerId = playerId;
        this.fallPosition = fallPosition;
        this.respawnPosition = respawnPosition;
    }
    //todo: implement facing question. current just maintains orientation
}
