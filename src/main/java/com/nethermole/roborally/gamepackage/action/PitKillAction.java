package com.nethermole.roborally.gamepackage.action;

import com.nethermole.roborally.gamepackage.board.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PitKillAction extends BoardAction {
    String playerId;
    Position fallPosition;
    Position respawnPosition;
    //todo: implement facing question. current just maintains orientation
}
